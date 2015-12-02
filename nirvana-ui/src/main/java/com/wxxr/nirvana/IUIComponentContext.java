package com.wxxr.nirvana;

import java.util.List;
import java.util.Map;

import com.wxxr.nirvana.context.IRequestContext;
import com.wxxr.nirvana.workbench.IContributionItem;
import com.wxxr.nirvana.workbench.impl.UIComponent;


public interface IUIComponentContext {
	
	public final static String DESKTOP = "desktop";
	public final static String PAGELAYOUT = "pagelayout";
	public final static String THEME = "theme";
	public final static String VIEW = "view";
	public final static String PAGE = "page";
	
	String getUIId();

	IContributionItem getUiContribute();
	
	IUIComponentContext getChildUIContext(String componentName);
	
	IUIComponentContext getParentUIContext();
	
	UIComponent getCurrentComponent(Map<String,Object> parameters);
	
	List<IUIComponentContext> getChildrenContext();
	
	void init(IUIComponentContext context,IRequestContext rcontext);
	
	IRequestContext getRequestContext();
}
