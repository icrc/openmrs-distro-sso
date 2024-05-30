/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.createusers;

import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.APIException;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.idgen.service.IdentifierSourceService;
import org.openmrs.module.referencedemodata.patient.DemoPersonGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.openmrs.module.referencedemodata.ReferenceDemoDataConstants.OPENMRS_ID_NAME;

public class DemoPatientGeneratorWithLocation {
	
	Logger log = LoggerFactory.getLogger(DemoPatientGeneratorWithLocation.class);
	
	private final IdentifierSourceService iss;
	
	public DemoPatientGeneratorWithLocation(IdentifierSourceService iss) {
		this.iss = iss;
	}
	
	/**
	 * Main entry point to create patients. Creates the specified number of patients and returns a
	 * list of database IDs for those patients.
	 * 
	 * @param patientCount number of patients to create
	 * @return a list of the primary keys for each patient created
	 */
	public List<Integer> createDemoPatients(int patientCount) {
    List<Integer> patientIds = new ArrayList<>(patientCount);

    PatientService ps = Context.getPatientService();
    List<Location> locations = Context.getLocationService().getRootLocations(false);
    int nbPatientsPerLocation = (int) Math.ceil((double) patientCount / locations.size());
    PatientIdentifierType patientIdentifierType = ps.getPatientIdentifierTypeByName(OPENMRS_ID_NAME);

    if (patientIdentifierType == null) {
      throw new APIException("Could not find identifier type " + OPENMRS_ID_NAME);
    }
    for (Location location : locations) {
      try {
        Context.getUserContext().setLocation(location);
        for (int i = 0; i < nbPatientsPerLocation; i++) {

          Patient patient = createDemoPatient(ps, patientIdentifierType, location);
          log.info("created demo patient: {} {} {} age: {}",
              new Object[]{patient.getPatientIdentifier(), patient.getGivenName(), patient.getFamilyName(),
                  patient.getAge()});
          patientIds.add(patient.getId());
        }
      } catch (Exception e) {
        log.error("Cant create patient for {}", location.getName(), e);
      }
    }


    return patientIds;
  }
	
	private Patient createDemoPatient(PatientService ps, PatientIdentifierType patientIdentifierType, Location location) {
		Patient patient = new Patient();
		
		DemoPersonGenerator.populatePerson(patient);
		
		PatientIdentifier patientIdentifier = new PatientIdentifier();
		patientIdentifier.setIdentifier(iss.generateIdentifier(patientIdentifierType, "DemoData"));
		patientIdentifier.setIdentifierType(patientIdentifierType);
		patientIdentifier.setDateCreated(new Date());
		patientIdentifier.setLocation(location);
		patient.addIdentifier(patientIdentifier);
		
		patient = ps.savePatient(patient);
		return patient;
	}
}
