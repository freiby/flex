package com.wxxr.nirvana.ui;

import com.wxxr.nirvana.ISessionWorkbench;
import com.wxxr.nirvana.platform.IPlatform;
import com.wxxr.nirvana.theme.ITheme;
import com.wxxr.nirvana.theme.IThemeManager;
import com.wxxr.nirvana.workbench.IPageLayoutManager;
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

	public void setCurrentPage(IWorkbenchPage currentPage) {
		this.currentPage = currentPage;
	}

	public IProduct getCurrentProduct() {
		return currentProduct;
	}

	public void setCurrentProduct(IProduct currentProduct) {
		this.currentProduct = currentProduct;
	}

	public IPermissionsManager getPermissionsManager() {
		return workbench.getPermissionsManager();
	}

	public ISecurityManager getSecurityManager() {
		return workbench.getSecurityManager();
	}

	public IPlatform getUIPlatform() {
		return workbench.getUIPlatform();
	}

	public IViewManager getViewManager() {
		return workbench.getViewManager();
	}

	public ITheme getCurrentTheme() {
		return workbench.getCurrentTheme();
	}

	public IThemeManager getThemeManager() {

		return workbench.getThemeManager();
	}

	public IWorkbenchPageManager getWorkbenchPageManager() {

		return workbench.getWorkbenchPageManager();
	}

	public IProductManager getProductManager() {

		return workbench.getProductManager();
	}

	public IWebResourceManager getWebResourceManager() {

		return workbench.getWebResourceManager();
	}

	public void destroy() {
	}

	public IWorkbenchPage getCurrentPage() {
		return currentPage;
	}

	public IPageLayoutManager getPageLayoutManager() {
		return workbench.getPageLayoutManager();
	}
}
