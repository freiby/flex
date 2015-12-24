package com.wxxr.nirvana.workbench.impl;

import com.wxxr.nirvana.platform.IPlatform;
import com.wxxr.nirvana.platform.PlatformLocator;
import com.wxxr.nirvana.theme.IThemeManager;
import com.wxxr.nirvana.workbench.ICreateRenderContext;
import com.wxxr.nirvana.workbench.IPageLayoutManager;
import com.wxxr.nirvana.workbench.IPermissionsManager;
import com.wxxr.nirvana.workbench.IProductManager;
import com.wxxr.nirvana.workbench.IRender;
import com.wxxr.nirvana.workbench.ISecurityManager;
import com.wxxr.nirvana.workbench.IServiceManager;
import com.wxxr.nirvana.workbench.IUIRenderManager;
import com.wxxr.nirvana.workbench.IWebResourceManager;

public class ServiceManager implements IServiceManager {
	private IThemeManager themeManager;

	private ISecurityManager securityManager;

	private IPermissionsManager permissionsManager;

	private IProductManager productManager;

	private IWebResourceManager webresourceManager;

	private IPageLayoutManager pageLayoutManager;

	private IUIRenderManager renderManager;
	
	public IWebResourceManager getWebResourceManager() {
		if (webresourceManager == null) {
			webresourceManager = new WebResourceManager();
			webresourceManager.start();
		}
		return webresourceManager;
	}

	public IProductManager getProductManager() {
		if (productManager == null) {
			productManager = new ProductManager();
			productManager.start();
		}
		return productManager;
	}

	public IThemeManager getThemeManager() {
		if (themeManager == null) {
			themeManager = new ThemeManager(getCreateContext());
			themeManager.start();
		}
		return themeManager;
	}

	public IPermissionsManager getPermissionsManager() {
		if (permissionsManager == null) {
			permissionsManager = new PermissionsManager();
			permissionsManager.start();
		}
		return permissionsManager;
	}

	public ISecurityManager getSecurityManager() {
		return securityManager;
	}
	
	public IPageLayoutManager getPageLayoutManager() {
		if (pageLayoutManager == null) {
			pageLayoutManager = new PageLayoutManager(getCreateContext());
			pageLayoutManager.start();
		}
		return pageLayoutManager;
	}

	public IUIRenderManager getUIRenderManager() {
		if (renderManager == null) {
			renderManager = new UIRenderManager();
			renderManager.start();
		}
		return renderManager;
	}
	
	
	private ICreateRenderContext getCreateContext() {
		ICreateRenderContext context = new ICreateRenderContext() {
			public IRender createRender(String id) {
				return getUIRenderManager().find(id);
			}
		};
		return context;
	}

	public IPlatform getUIPlatform() {
		return PlatformLocator.getPlatform();
	}

}
