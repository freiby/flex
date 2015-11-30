package com.wxxr.nirvana.workbench.impl;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.theme.IDesktop;

public class Desktop extends UIComponent implements IDesktop {
	private String uri;
	
	@Override
	public void init(IConfigurationElement config) {
		super.init(config);
		uri = config.getAttribute("uri");
	}
	public String getURI() {
		return uri;
	}

}
