package com.wxxr.nirvana.workbench.impl;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.theme.IDesktop;
import com.wxxr.nirvana.workbench.IDispatchUI;
import com.wxxr.nirvana.workbench.IRender;

public class Desktop extends UIComponent implements IDesktop, IDispatchUI {
	private String uri;

	@Override
	public void init(IConfigurationElement config, IRender render) {
		super.init(config, render);
		uri = config.getAttribute("uri");
	}

	public String getURI() {
		return uri;
	}

}
