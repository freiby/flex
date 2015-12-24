package com.wxxr.nirvana.workbench;

import com.wxxr.nirvana.platform.IPlatform;
import com.wxxr.nirvana.theme.IThemeManager;

public interface IServiceManager {
	IPermissionsManager getPermissionsManager();

	ISecurityManager getSecurityManager();

	IPlatform getUIPlatform();

	IThemeManager getThemeManager();
	
	IProductManager getProductManager();

	IWebResourceManager getWebResourceManager();

	IPageLayoutManager getPageLayoutManager();
	
	IUIRenderManager getUIRenderManager();
}
	
