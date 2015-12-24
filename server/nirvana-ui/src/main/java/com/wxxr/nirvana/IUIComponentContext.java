package com.wxxr.nirvana;

import java.util.List;
import java.util.Map;

import com.wxxr.nirvana.workbench.IContributionItem;
import com.wxxr.nirvana.workbench.impl.UIComponent;

/**
 * 渲染每个组件的需要知道组件的上下文，也就是那个product的哪个theme的哪个deskop等等
 * 
 * @author fudapeng
 *
 */
public interface IUIComponentContext {

	public final static String DESKTOP = "desktop";
	public final static String PAGELAYOUT = "pagelayout";
	public final static String THEME = "theme";
	public final static String VIEW = "view";
	public final static String PAGE = "page";
	public final static String PAGENAV = "navigation";

	String getUIId();

	IContributionItem getUiContribute();

	IUIComponentContext getChildUIContext(String componentName);

	IUIComponentContext getParentUIContext();

	UIComponent getCurrentComponent(Map<String, Object> parameters);

	List<IUIComponentContext> getChildrenContext();

	void init(IUIComponentContext context);

}
