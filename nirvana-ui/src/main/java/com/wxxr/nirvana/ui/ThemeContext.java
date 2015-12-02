package com.wxxr.nirvana.ui;

import java.util.Map;

import com.wxxr.nirvana.IUIComponentContext;
import com.wxxr.nirvana.theme.IDesktop;
import com.wxxr.nirvana.theme.IPageLayout;
import com.wxxr.nirvana.workbench.IContributionItem;
import com.wxxr.nirvana.workbench.impl.ThemeImpl;
import com.wxxr.nirvana.workbench.impl.UIComponent;

public class ThemeContext extends UIComponentContext {
	
	private ThemeImpl theme;
	public ThemeContext(IContributionItem uiContribute) {
		super(uiContribute);
		theme = (ThemeImpl) uiContribute;
	}

	
	public IUIComponentContext getChildUIContext(String componentName) {
		IUIComponentContext result = super.getChildUIContext(componentName);
		if(result != null){
			return result;
		}
		if(componentName.equals(IUIComponentContext.DESKTOP)){
			IDesktop destop = theme.getDesktop();
			result = new DesktopContext(destop);
			result.init(this,null);
			addChildContext(IUIComponentContext.DESKTOP, result);
		}else if(componentName.equals(IUIComponentContext.PAGELAYOUT)){
			IPageLayout pagelayout = theme.getPageLayout();
			result = new PageLayoutContext(pagelayout);
			result.init(this,null);
			addChildContext(IUIComponentContext.PAGELAYOUT,result);
		}
		return result;
	}
	
	public UIComponent getCurrentComponent(Map<String,Object> parameters) {
		return null;
	}

}
