package com.wxxr.nirvana.ui;

import com.wxxr.nirvana.ISessionWorkbench;
import com.wxxr.nirvana.platform.IPlatform;
import com.wxxr.nirvana.theme.ITheme;
import com.wxxr.nirvana.theme.IThemeManager;
import com.wxxr.nirvana.workbench.IPermissionsManager;
import com.wxxr.nirvana.workbench.IProduct;
import com.wxxr.nirvana.workbench.IProductManager;
import com.wxxr.nirvana.workbench.ISecurityManager;
import com.wxxr.nirvana.workbench.IViewManager;
import com.wxxr.nirvana.workbench.IWebResourceManager;
import com.wxxr.nirvana.workbench.IWorkbench;
import com.wxxr.nirvana.workbench.IWorkbenchPage;
import com.wxxr.nirvana.workbench.IWorkbenchPageManager;

public class WorkbenchProxy implements ISessionWorkbench {
	
	private IWorkbench workbench;
	
	private IProduct currentProduct;
	
	private IWorkbenchPage currentPage;
	
	
	public WorkbenchProxy(IWorkbench workbench) {
		this.workbench = workbench;
	}

	public IProduct getCurrentProduct(){
		return currentProduct;
	}
	
	public void setCurrentProduct(IProduct currentProduct) {
		this.currentProduct = currentProduct;
	}

	public IPermissionsManager getPermissionsManager() {
		return null;
	}
	public ISecurityManager getSecurityManager() {
		return null;
	}
	public IPlatform getUIPlatform() {
		return null;
	}
	public IViewManager getViewManager() {
		return null;
	}
	public ITheme getCurrentTheme() {
		
		return null;
	}
	public IThemeManager getThemeManager() {
		
		return null;
	}
	public IWorkbenchPageManager getWorkbenchPageManager() {
		
		return null;
	}
	public IProductManager getProductManager() {
		
		return null;
	}
	public IWebResourceManager getWebResourceManager() {
		
		return null;
	}
	public void destroy() {
	}

	public IWorkbenchPage getCurrentPage() {
		return currentPage;
	}
}
