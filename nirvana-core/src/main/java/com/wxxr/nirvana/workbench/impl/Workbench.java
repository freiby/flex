package com.wxxr.nirvana.workbench.impl;

import com.wxxr.nirvana.platform.IPlatform;
import com.wxxr.nirvana.platform.PlatformLocator;
import com.wxxr.nirvana.theme.ITheme;
import com.wxxr.nirvana.theme.IThemeManager;
import com.wxxr.nirvana.workbench.IPageLayoutManager;
import com.wxxr.nirvana.workbench.IPermissionsManager;
import com.wxxr.nirvana.workbench.IProductManager;
import com.wxxr.nirvana.workbench.IRender;
import com.wxxr.nirvana.workbench.ISecurityManager;
import com.wxxr.nirvana.workbench.IUIRenderManager;
import com.wxxr.nirvana.workbench.IViewManager;
import com.wxxr.nirvana.workbench.IWebResourceManager;
import com.wxxr.nirvana.workbench.IWorkbench;
import com.wxxr.nirvana.workbench.IWorkbenchPageManager;

public class Workbench implements IWorkbench {

	private IThemeManager themeManager;

	private ISecurityManager securityManager;

	private IPermissionsManager permissionsManager;

	private IWorkbenchPageManager workbenchPageManager;

	private IViewManager viewManager;

	private IProductManager productManager;

	private IWebResourceManager webresourceManager;

	private IPageLayoutManager pageLayoutManager;
	
	private IUIRenderManager renderManager;
	
	public interface ICreateRenderContext{
		IRender createRender(String id);
	};

	public IWebResourceManager getWebResourceManager() {
		if (webresourceManager == null) {
			webresourceManager = new WebResourceManager();
			webresourceManager.start();
		}
		return webresourceManager;
	}

	public IWorkbenchPageManager getWorkbenchPageManager() {
		if (workbenchPageManager == null) {
			workbenchPageManager = new WorkbenchPageManager(getCreateContext());
			workbenchPageManager.start();
		}
		return workbenchPageManager;
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

	public IPlatform getUIPlatform() {
		return PlatformLocator.getPlatform();
	}

	public IViewManager getViewManager() {
		if (viewManager == null) {
			viewManager = new ViewManager(getCreateContext());
			viewManager.start();
		}
		return viewManager;
	}
	
	private ICreateRenderContext getCreateContext(){
		ICreateRenderContext context = new ICreateRenderContext(){
			public IRender createRender(String id) {
				return getUIRenderManager().find(id);
			}
		};
		return context;
	}

	public ITheme getCurrentTheme() {
		return themeManager.getDefaultTheme();
	}

	public void destroy() {
	}

	public IPageLayoutManager getPageLayoutManager() {
		if (pageLayoutManager == null) {
			pageLayoutManager = new PageLayoutManager(getCreateContext());
			pageLayoutManager.start();
		}
		return pageLayoutManager;
	}
	
	public IUIRenderManager getUIRenderManager() {
		if(renderManager == null){
			renderManager = new UIRenderManager();
			renderManager.start();
		}
		return renderManager;
	}

}
