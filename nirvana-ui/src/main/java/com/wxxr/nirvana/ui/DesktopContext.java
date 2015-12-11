package com.wxxr.nirvana.ui;

import java.util.Map;

import com.wxxr.nirvana.workbench.IContributionItem;
import com.wxxr.nirvana.workbench.impl.Desktop;
import com.wxxr.nirvana.workbench.impl.UIComponent;

public class DesktopContext extends UIComponentContext {

	private Desktop desktop = null;

	public DesktopContext(IContributionItem uiContribute) {
		super(uiContribute);
		desktop = (Desktop) uiContribute;
	}

	public UIComponent getCurrentComponent(Map<String, Object> parameters) {
		return desktop;
	}

}
