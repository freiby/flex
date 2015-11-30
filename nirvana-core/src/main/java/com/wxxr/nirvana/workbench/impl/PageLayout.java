package com.wxxr.nirvana.workbench.impl;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.theme.IPageLayout;

public class PageLayout extends UIComponent implements IPageLayout {

	private String uri;
	public String getURI() {
		return uri;
	}
	
	@Override
	public void init(IConfigurationElement config) {
		super.init(config);
		uri = config.getAttribute("uri");
	}

}
