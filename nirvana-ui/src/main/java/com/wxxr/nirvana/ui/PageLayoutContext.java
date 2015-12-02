package com.wxxr.nirvana.ui;

import java.util.Map;

import com.wxxr.nirvana.IUIComponentContext;
import com.wxxr.nirvana.workbench.IContributionItem;
import com.wxxr.nirvana.workbench.impl.PageLayout;
import com.wxxr.nirvana.workbench.impl.UIComponent;

public class PageLayoutContext extends UIComponentContext {

	private PageLayout pageLayout;
	public PageLayoutContext(IContributionItem uiContribute) {
		super(uiContribute);
		pageLayout = (PageLayout) uiContribute;
	}

	public IUIComponentContext createChildUIContext(String componentName) {
		return null;
	}

	public UIComponent getCurrentComponent(Map<String,Object> parameters) {
		return null;
	}

}
