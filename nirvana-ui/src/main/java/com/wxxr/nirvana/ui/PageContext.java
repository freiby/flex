package com.wxxr.nirvana.ui;

import java.util.Map;

import com.wxxr.nirvana.ContainerAccess;
import com.wxxr.nirvana.IPageContext;
import com.wxxr.nirvana.ISessionWorkbench;
import com.wxxr.nirvana.IUIComponentContext;
import com.wxxr.nirvana.theme.IPageLayout;
import com.wxxr.nirvana.workbench.IContributionItem;
import com.wxxr.nirvana.workbench.impl.Product.PageRef;
import com.wxxr.nirvana.workbench.impl.UIComponent;
import com.wxxr.nirvana.workbench.impl.WorkbenchPage;
import com.wxxr.nirvana.workbench.impl.WorkbenchPage.ViewRef;

public class PageContext extends UIComponentContext implements IPageContext {

	private ThemeContext themeContext;
	private PageRef[] pagerefs;
	private ISessionWorkbench workbench;

	public PageContext(IContributionItem uiContribute, PageRef[] pagerefs) {
		super(uiContribute);
		this.themeContext = (ThemeContext) themeContext;
		this.pagerefs = pagerefs;
		workbench = ContainerAccess.getSessionWorkbench();

	}

	@Override
	protected IUIComponentContext createUIContext(String componentName) {
		IUIComponentContext result = null;
		WorkbenchPage current = (WorkbenchPage) workbench.getCurrentPage();
		if (componentName.equals(IUIComponentContext.VIEW)) {
			ViewRef[] viewrefs = current.getAllViewRefs();
			result = new ViewContext(null, viewrefs);
			result.init(this);
			addChildContext(IUIComponentContext.VIEW, result);
		}
		return result;
	}

	public UIComponent getCurrentComponent(Map<String, Object> parameters) {
		return (WorkbenchPage) workbench.getCurrentPage();
	}

	public IPageLayout getPageLayout() {
		WorkbenchPage page = (WorkbenchPage) workbench.getCurrentPage();
		String pageId = page.getUniqueIndentifier();
		if (pagerefs != null) {
			for (PageRef item : pagerefs) {
				if (item.id.equalsIgnoreCase(pageId)) {
					String layoutId = item.layout;
					IPageLayout pagelayout = workbench.getPageLayoutManager()
							.getPageLayout(layoutId);
					return pagelayout;
				}
			}
		}
		return null;
	}

}
