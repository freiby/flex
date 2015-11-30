package com.wxxr.nirvana.workbench.impl;

import com.wxxr.nirvana.context.NirvanaContext;
import com.wxxr.nirvana.platform.IPlatform;
import com.wxxr.nirvana.theme.ITheme;
import com.wxxr.nirvana.theme.IThemeManager;
import com.wxxr.nirvana.workbench.IPermissionsManager;
import com.wxxr.nirvana.workbench.ISecurityManager;
import com.wxxr.nirvana.workbench.IViewManager;
import com.wxxr.nirvana.workbench.IWorkbench;
import com.wxxr.nirvana.workbench.IWorkbenchPageManager;

public class Workbench implements IWorkbench {
	
	private IThemeManager themeManager;

	private ISecurityManager securityManager;
	
	private IPermissionsManager permissionsManager;

	private NirvanaContext nirvanaContext;
	
	private IWorkbenchPageManager workbenchPage;


	public IWorkbenchPageManager getWorkbenchPageManager() {
		return workbenchPage;
	}
	
	public IThemeManager getThemeManager() {
		return themeManager;
	}

	public IPermissionsManager getPermissionsManager() {
		return permissionsManager;
	}

	public ISecurityManager getSecurityManager() {
		return securityManager;
	}

	public IWorkbench getCurrentWorkbench() {
		return null;
	}

	public IPlatform getUIPlatform() {
		return null;
	}

	public String getDefaultPageId() {
		return null;
	}

	public String getDefaultThemeId() {
		return null;
	}

	public IViewManager getViewManager() {
		return null;
	}

	public String getServerContextName() {
		return null;
	}

	public ITheme getCurrentTheme() {
		
		return null;
	}

	public void destroy() {
	}

}
