package org.openmrs.module.createusers;

import org.openmrs.module.referencedemodata.DemoDataConceptCache;
import org.openmrs.module.referencedemodata.condition.DemoConditionGenerator;
import org.openmrs.module.referencedemodata.diagnosis.DemoDiagnosisGenerator;
import org.openmrs.module.referencedemodata.obs.DemoObsGenerator;
import org.openmrs.module.referencedemodata.orders.DemoOrderGenerator;
import org.openmrs.module.referencedemodata.program.DemoProgramGenerator;
import org.openmrs.module.referencedemodata.providers.DemoProviderGenerator;

public class Generators {
	
	protected final DemoProviderGenerator providerGenerator;
	
	protected final DemoDataConceptCache conceptCache = new DemoDataConceptCache();
	
	protected final DemoObsGenerator obsGenerator = new DemoObsGenerator(conceptCache);
	
	protected final DemoOrderGenerator orderGenerator = new DemoOrderGenerator();
	
	protected final DemoConditionGenerator conditionGenerator = new DemoConditionGenerator();
	
	protected final DemoDiagnosisGenerator diagnosisGenerator = new DemoDiagnosisGenerator(conceptCache, conditionGenerator);
	
	protected final DemoVisitGeneratorLocation visitGenerator;
	
	protected final DemoProgramGenerator programGenerator = new DemoProgramGenerator();
	
	public Generators(DemoProviderGenerator providerGenerator) {
		this.providerGenerator = providerGenerator;
		visitGenerator = new DemoVisitGeneratorLocation(providerGenerator, obsGenerator, orderGenerator, diagnosisGenerator);
	}
}
