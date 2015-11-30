/*
 * @(#)WebWorkbench.java	 2007-9-27
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.workbench;

import com.wxxr.nirvana.platform.IPlatform;
import com.wxxr.nirvana.theme.ITheme;

/**
 * @author neillin
 *
 */
public interface IWorkbench {

	IPermissionsManager getPermissionsManager();

	ISecurityManager getSecurityManager();

	IWorkbench getCurrentWorkbench();

	IPlatform getUIPlatform();

	String getDefaultPageId();

	String getDefaultThemeId();

	IViewManager getViewManager();

	String getServerContextName();

	ITheme getCurrentTheme();

	IWorkbenchPageManager getWorkbenchPageManager();

	void destroy();
}
