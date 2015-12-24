package com.wxxr.nirvana.ui;

import com.wxxr.nirvana.workbench.IWebResource;
import com.wxxr.nirvana.workbench.impl.UIComponent;

public class ResourceUIComponent extends UIComponent {
	private IWebResource[] resources;

	public ResourceUIComponent(IWebResource[] resources) {
		super();
		this.resources = resources;
	}

	public IWebResource[] getResorces() {
		return resources;
	}
}
