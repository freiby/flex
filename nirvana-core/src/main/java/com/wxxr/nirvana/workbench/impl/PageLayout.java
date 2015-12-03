package com.wxxr.nirvana.workbench.impl;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.theme.IPageLayout;
import com.wxxr.nirvana.workbench.IDispatchUI;

public class PageLayout extends UIComponent implements IPageLayout,IDispatchUI {

	private String uri;
	public String getURI() {
		return uri;
	}
	
	@Override
	public void init(IConfigurationElement config) {
		super.init(config);
		uri = config.getAttribute("uri");
	}

	public void destroy() {
		
	}

}