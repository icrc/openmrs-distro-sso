// SPDX-FileCopyrightText: 2025 ICRC
//
// SPDX-License-Identifier: BSD-3-Clause

package org.openmrs.module.createusers;

import org.openmrs.Provider;
import org.openmrs.api.context.Context;
import org.openmrs.module.referencedemodata.Randomizer;
import org.openmrs.module.referencedemodata.providers.DemoProviderGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Generate data with other provider to user provider linked to a location.
 */
public class DemoProviderGeneratorWithLocation extends DemoProviderGenerator {
	
	private List<Provider> clinicians = null;
	
	private List<Provider> labTechs = null;
	
	@Override
  public Provider getRandomClinician() {
    if (clinicians == null) {
      clinicians = new ArrayList<>(Context.getProviderService().getAllProviders(false));
    }
    return Randomizer.randomListEntry(clinicians);
  }
	
	@Override
  public Provider getRandomLabTech() {
    if (labTechs == null) {
      labTechs = new ArrayList<>(Context.getProviderService().getAllProviders(false));
    }
    return Randomizer.randomListEntry(labTechs);
  }
}
