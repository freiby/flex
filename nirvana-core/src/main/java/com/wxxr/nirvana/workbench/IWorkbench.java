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
import com.wxxr.nirvana.theme.IThemeManager;

/**
 * @author neillin
 *
 */
public interface IWorkbench {

	IPermissionsManager getPermissionsManager();

	ISecurityManager getSecurityManager();

	IPlatform getUIPlatform();

	IViewManager getViewManager();

	ITheme getCurrentTheme();
	
	IThemeManager getThemeManager();

	IWorkbenchPageManager getWorkbenchPageManager();

	IProductManager getProductManager();
	
	IWebResourceManager getWebResourceManager();
	
	IPageLayoutManager getPageLayoutManager();
	
	void destroy();
}
