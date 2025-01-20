// Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
// SPDX-FileCopyrightText: 2025 ICRC
//
// SPDX-License-Identifier: BSD-3-Clause

package org.openmrs.module.createusers;

import org.openmrs.GlobalProperty;
import org.openmrs.Person;
import org.openmrs.Provider;
import org.openmrs.VisitType;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.PatientService;
import org.openmrs.api.ProviderService;
import org.openmrs.api.VisitService;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.Daemon;
import org.openmrs.module.idgen.service.IdentifierSourceService;
import org.openmrs.module.patientflags.api.FlagService;
import org.openmrs.util.PrivilegeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.openmrs.api.context.Context.getVisitService;
import static org.openmrs.util.OpenmrsConstants.GP_CASE_SENSITIVE_DATABASE_STRING_COMPARISON;

/**
 * ICRC customization: - support for location and remove appointments Generator - Visits are created
 * in threads TODO: to be improved and maybe pushed back in demodat module. See
 * {@link org.openmrs.module.referencedemodata.ReferenceDemoDataActivator}
 */
public class ReferenceDemoDataCreator {
	
	Logger log = LoggerFactory.getLogger(ReferenceDemoDataCreator.class);
	
	// this property exists for testing and should only have a value during the started() function
	private static IdentifierSourceService iss = null;
	
	public void create() {
    final String prop = "referencedemodata.createDemoPatientsOnNextStartup";
    // basic idea: on start-up work out how many patients, if any we should generate
    // if there aren't any, we exit. Otherwise, we ensure some users and providers are set up and then start generating data
    try {
      AdministrationService as = Context.getAdministrationService();
      GlobalProperty gp = as.getGlobalPropertyObject(prop);
      if (gp == null || (gp.getPropertyValue().equals("0"))) {
        return;
      }

      int patientCount = 0;
      try {
        patientCount = Integer.parseInt(gp.getPropertyValue());
      } catch (NumberFormatException e) {
        log.error("Could not parse property as an integer");
        return;
      }
      log.info("Will create {} patients", patientCount);

      if (patientCount <= 0) {
        return;
      }

      try {
        linkAdminAccountToAProviderIfNecessary();

        // we temporarily set the database to be case-insensitive
        boolean valueBefore = Context.getAdministrationService().isDatabaseStringComparisonCaseSensitive();
        try {
          Context.getAdministrationService()
              .setGlobalProperty(GP_CASE_SENSITIVE_DATABASE_STRING_COMPARISON, "true");

          // attempt to set the log level so that details about the created patients are logged


          List<Integer> createdPatientIds = new DemoPatientGeneratorWithLocation(
              getIdentifierSourceService()).createDemoPatients(patientCount);


          log.info("Created {} patients", createdPatientIds.size());

          DemoProviderGeneratorWithLocation providerGeneratorWithLocation = new DemoProviderGeneratorWithLocation();
          providerGeneratorWithLocation.getRandomClinician();
          providerGeneratorWithLocation.getRandomLabTech();


          VisitService visitService = getVisitService();
          List<VisitType> visitTypes = visitService.getAllVisitTypes().stream()
              .filter(vt -> !"Offline Visit".equals(vt.getName())).collect(Collectors.toList());
          PatientService patientService = Context.getPatientService();
          Context.flushSession();
//          FlagService is not thread Safe so if have to initialize cache before first start.
          Context.getService(FlagService.class).getPrivileges();
          try {
            List<Thread> callableTasks = new ArrayList<>();
            for (Integer patientId : createdPatientIds) {
              log.info("Created callable for {}", patientId);
              callableTasks.add(Daemon.runInNewDaemonThread(new PatientCreatorRunnable(patientId, patientService, new Generators(providerGeneratorWithLocation), visitTypes, visitService)));
            }

            log.info("Created {} Threads", callableTasks.size());
            for (Thread t : callableTasks) {
              t.join();
            }
          } catch (Exception exception) {
            log.error("Failed to create visits", exception);
          }
        } catch (Exception ex) {
          log.error("Failed to create patients", ex);
        } finally {
          Context.flushSession();
          Context.clearSession();
          try {
            // Restore the value of Global Property
            Context.getAdministrationService()
                .setGlobalProperty(GP_CASE_SENSITIVE_DATABASE_STRING_COMPARISON,
                    String.valueOf(valueBefore));
          } catch (Exception ignored) {
          }
        }
      } catch (Exception e) {
        log.error("Exception caught while creating demo data", e);
      } finally {
        // don't hold a reference to the IdentifierSourceService
        iss = null;

        // Set the global property to zero so that we won't create demo patients next time.
        gp.setPropertyValue("0");
        as.saveGlobalProperty(gp);
      }
    } catch (Exception e) {
      log.error("Failed to load ReferenceDemoData module due to exception", e);
    }
  }
	
	private void linkAdminAccountToAProviderIfNecessary() {
		try {
			// If unknown provider isn't yet linked to admin, then do it
			Context.addProxyPrivilege(PrivilegeConstants.GET_PROVIDERS);
			Context.addProxyPrivilege(PrivilegeConstants.GET_PERSONS);
			Context.addProxyPrivilege(PrivilegeConstants.MANAGE_PROVIDERS);
			
			ProviderService ps = Context.getProviderService();
			Person adminPerson = Context.getPersonService().getPerson(1);
			
			Collection<Provider> possibleProvider = ps.getProvidersByPerson(adminPerson);
			if (possibleProvider.size() == 0) {
				List<Provider> providers = ps.getAllProviders(false);
				
				Provider provider;
				if (providers.size() == 0) {
					provider = new Provider();
					provider.setIdentifier("admin");
				} else {
					provider = providers.get(0);
				}
				
				provider.setPerson(adminPerson);
				ps.saveProvider(provider);
			}
		}
		finally {
			Context.removeProxyPrivilege(PrivilegeConstants.GET_PROVIDERS);
			Context.removeProxyPrivilege(PrivilegeConstants.GET_PERSONS);
			Context.removeProxyPrivilege(PrivilegeConstants.MANAGE_PROVIDERS);
		}
	}
	
	private IdentifierSourceService getIdentifierSourceService() {
		if (iss == null) {
			iss = Context.getService(IdentifierSourceService.class);
		}
		
		return iss;
	}
	
}
