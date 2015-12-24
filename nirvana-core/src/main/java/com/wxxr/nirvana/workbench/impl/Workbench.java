package com.wxxr.nirvana.workbench.impl;

import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.theme.ITheme;
import com.wxxr.nirvana.workbench.IActionManager;
import com.wxxr.nirvana.workbench.ICreateRenderContext;
import com.wxxr.nirvana.workbench.IProduct;
import com.wxxr.nirvana.workbench.IWorkbench;
import com.wxxr.nirvana.workbench.IWorkbenchPage;
import com.wxxr.nirvana.workbench.IWorkbenchPageManager;

public class Workbench implements IWorkbench {

	private IActionManager actionManager;
	
	private IWorkbenchPageManager workbenchPageManager;
	
	private ICreateRenderContext createRenderContext;
	
	private IProduct currentProduct;
	
	private IWorkbenchPage currentPage;

	public void start() throws NirvanaException {
	}

	public IWorkbenchPageManager getWorkbenchPageManager() {
		if (workbenchPageManager == null) {
			workbenchPageManager = new WorkbenchPageManager(createRenderContext);
			workbenchPageManager.start();
		}
		return workbenchPageManager;
	}
	
	public IActionManager getActionManager() {
		if (actionManager == null) {
			actionManager = new ActionManager();
			actionManager.start();
		}
		return actionManager;
	}
	
	public ITheme getCurrentTheme() {
		return null;
	}

	public void destroy() {
		
	}

	@Override
	public IProduct getCurrentProduct() {
		return currentProduct;
	}

	@Override
	public IWorkbenchPage getCurrentPage() {
		return currentPage;
	}

	public void setCurrentProduct(IProduct currentProduct) {
		this.currentProduct = currentProduct;
	}

	public void setCurrentPage(IWorkbenchPage currentPage) {
		this.currentPage = currentPage;
	}
	
	
	

}
