// Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
// SPDX-FileCopyrightText: 2025 ICRC
//
// SPDX-License-Identifier: BSD-3-Clause

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
