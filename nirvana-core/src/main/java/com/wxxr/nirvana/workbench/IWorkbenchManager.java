package com.wxxr.nirvana.workbench;

import com.wxxr.nirvana.platform.IPlatform;
import com.wxxr.nirvana.theme.IThemeManager;

public interface IWorkbenchManager {
	IThemeManager getThemeManager();

	IWorkbenchPageManager getPageManager();

	IPermissionsManager getPermissionsManager();

	ISecurityManager getSecurityManager();

	IWorkbench getCurrentWorkbench();

	IPlatform getUIPlatform();

	String getDefaultPageId();

	String getDefaultThemeId();
}
