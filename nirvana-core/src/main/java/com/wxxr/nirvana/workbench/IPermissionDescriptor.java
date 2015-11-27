/*
 * @(#)IPermissionDescriptor.java	 2007-11-3
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.workbench;

import com.wxxr.nirvana.platform.IConfigurationElement;

/**
 * @author neillin
 *
 */
public interface IPermissionDescriptor extends IContributionItem {
	String[] getRwRoles();
	String[] getRRoles();
	String[] getXRoles();
	String getTargetId();
    void init(IPermissionsManager manager, IConfigurationElement config);   
    void destroy();
}
