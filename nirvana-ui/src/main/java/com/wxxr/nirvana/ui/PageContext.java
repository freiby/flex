package com.wxxr.nirvana.ui;

import java.util.Map;

import com.wxxr.nirvana.ISessionWorkbench;
import com.wxxr.nirvana.IUIComponentContext;
import com.wxxr.nirvana.IWorkbenchContainer;
import com.wxxr.nirvana.workbench.IContributionItem;
import com.wxxr.nirvana.workbench.impl.Product.PageRef;
import com.wxxr.nirvana.workbench.impl.UIComponent;
import com.wxxr.nirvana.workbench.impl.WorkbenchPage;
import com.wxxr.nirvana.workbench.impl.WorkbenchPage.ViewRef;

public class PageContext extends UIComponentContext {

	private ThemeContext themeContext;
	private PageRef[] pagerefs;
	private ISessionWorkbench workbench;
	
	public PageContext(IContributionItem uiContribute, PageRef[] pagerefs) {
		super(uiContribute);
		this.themeContext = (ThemeContext) themeContext;
		this.pagerefs = pagerefs;
		workbench = (ISessionWorkbench) getRequestContext().getSessionScope().get(IWorkbenchContainer.WORKBENCH_PROXY);
		
	}
	
	@Override
	public IUIComponentContext getChildUIContext(String componentName) {
		IUIComponentContext result = super.getChildUIContext(componentName);
		if(result != null){
			return result;
		}
		WorkbenchPage current = (WorkbenchPage) workbench.getCurrentPage();
		if(componentName.equals(IUIComponentContext.VIEW)){
			ViewRef[] viewrefs = current.getAllViewRefs();
			result = new ViewContext(null,viewrefs);
			result.init(this,null);
			addChildContext(IUIComponentContext.VIEW, result);
		}
		return result;
	}

	public UIComponent getCurrentComponent(Map<String,Object> parameters) {
		return (WorkbenchPage) workbench.getCurrentPage();
	}

}
