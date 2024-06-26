/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.createusers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.liquibase.ChangeSetExecutorCallback;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.util.DatabaseUpdater;

/**
 * This class contains the logic that is run every time this module is either started or shutdown
 */
public class CreateUsersActivator extends BaseModuleActivator {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * @see #started()
	 */
	@Override
	public void started() {
		log.warn("Started Create.users");
		log.warn("Normally oauth2 module should manage datafilter inputs as well: To be improved.");
		try {
			DatabaseUpdater.executeChangelog("liquibase-add-default-users.xml", (ChangeSetExecutorCallback) null);
		}
		catch (Exception e) {
			log.error(e);
		}
		new ReferenceDemoDataCreator().create();
	}
	
	/**
	 * @see #shutdown()
	 */
	public void shutdown() {
		log.warn("Shutdown Create.users");
	}
	
}
