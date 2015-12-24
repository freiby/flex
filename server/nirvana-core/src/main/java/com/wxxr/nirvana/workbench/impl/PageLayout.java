package com.wxxr.nirvana.workbench.impl;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.theme.IPageLayout;
import com.wxxr.nirvana.workbench.IDispatchUI;
import com.wxxr.nirvana.workbench.IPageLayoutManager;
import com.wxxr.nirvana.workbench.IRender;

public class PageLayout extends UIComponent implements IPageLayout, IDispatchUI {

	private String uri;

	public String getURI() {
		return uri;
	}

	public void destroy() {
	}

	public void init(IPageLayoutManager manager, IConfigurationElement config,
			IRender render) {
		super.init(config, render);
		uri = config.getAttribute("uri");
	}
}