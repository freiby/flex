package com.wxxr.nirvana.ui;

import java.util.Map;

import com.wxxr.nirvana.ContainerAccess;
import com.wxxr.nirvana.IUIComponentContext;
import com.wxxr.nirvana.workbench.IRender;
import com.wxxr.nirvana.workbench.IWorkbenchPage;
import com.wxxr.nirvana.workbench.impl.Product.PageRef;
import com.wxxr.nirvana.workbench.impl.UIComponent;
import com.wxxr.nirvana.workbench.impl.WorkbenchPage;

public class PageNavigationContext extends UIComponentContext {

	private IWorkbenchPage[] pages;
	private PageRef[] pagerefs;

	private PageNavigation pageNavigation;
	
	private String navigationRef;

	public class PageNavigation extends UIComponent {
		public IWorkbenchPage[] getAllPages() {
			return pages;
		}

		@Override
		public String getName() {
			return "PageNavigation";
		}

//		@Override
//		public String getContributorVersion() {
//			String pageContribute = (pages != null && pages.length > 0) ? ((WorkbenchPage) pages[0])
//					.getContributorVersion() : null;
//					getRender().get
//			return 
//		}
//
//		@Override
//		public String getContributorId() {
//			return (pages != null && pages.length > 0) ? ((WorkbenchPage) pages[0])
//					.getContributorId() : null;
//		}
		
		@Override
		public IRender getRender() {
			if(navigationRef != null){
				return  ContainerAccess.getServiceManager().getUIRenderManager().find(navigationRef);
			}
			return null;
		}
	}

	public PageNavigationContext(PageRef[] pagerefs, String navigationRef) {
		super(null);
		this.pagerefs = pagerefs;
		this.navigationRef = navigationRef;
	}

	@Override
	public void init(IUIComponentContext parent) {
		super.init(parent);
		pages = new WorkbenchPage[pagerefs.length];
		for (int i = 0; i < pagerefs.length; i++) {
			IWorkbenchPage page = ContainerAccess.getSessionWorkbench()
					.getWorkbenchPageManager().getWorkbenchPage(pagerefs[i].id);
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
