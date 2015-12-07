package com.wxxr.nirvana.ui;

import java.util.Map;

import com.wxxr.nirvana.ContainerAccess;
import com.wxxr.nirvana.IUIComponentContext;
import com.wxxr.nirvana.workbench.IWorkbenchPage;
import com.wxxr.nirvana.workbench.impl.Product.PageRef;
import com.wxxr.nirvana.workbench.impl.UIComponent;
import com.wxxr.nirvana.workbench.impl.WorkbenchPage;

public class PageNavigationContext extends UIComponentContext{
	
	private IWorkbenchPage[] pages;
	private PageRef[] pagerefs;
	
	private PageNavigation pageNavigation;
	
	public class PageNavigation extends UIComponent{
		public IWorkbenchPage[] getAllPages() {
			return pages;
		}
		@Override
		public String getName() {
			return "PageNavigation";
		}
	}
	public PageNavigationContext(PageRef[] pagerefs) {
		super(null);
		this.pagerefs = pagerefs;
	}
	@Override
	public void init(IUIComponentContext parent) {
		super.init(parent);
		pages = new WorkbenchPage[pagerefs.length];
		for(int i=0; i<pagerefs.length; i++){
			IWorkbenchPage page = ContainerAccess.getSessionWorkbench().getWorkbenchPageManager().getWorkbenchPage(pagerefs[i].id);
			pages[i] = page;
		}
		pageNavigation = new PageNavigation();
	}

	public UIComponent getCurrentComponent(Map<String, Object> parameters) {
		return pageNavigation;
	}

	public IWorkbenchPage[] getAllPages() {
		return pages;
	}

}
