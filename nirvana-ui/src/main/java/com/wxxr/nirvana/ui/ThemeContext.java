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

	@Override
	protected IUIComponentContext createUIContext(String componentName) {
		IUIComponentContext result = null;
		if (componentName.equals(IUIComponentContext.DESKTOP)) {
			IDesktop destop = theme.getDesktop();
			result = new DesktopContext(destop);
			result.init(this);
			addChildContext(IUIComponentContext.DESKTOP, result);
		}
		return result;
	}

	public UIComponent getCurrentComponent(Map<String, Object> parameters) {
		return null;
	}

}
