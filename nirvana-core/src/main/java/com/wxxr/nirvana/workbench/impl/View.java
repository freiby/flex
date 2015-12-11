package com.wxxr.nirvana.workbench.impl;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.workbench.IRender;
import com.wxxr.nirvana.workbench.IView;
import com.wxxr.nirvana.workbench.IViewManager;

public class View extends UIComponent implements IView {

	private static final String ATT_VIEW_URI = "uri";

	private static final String RESOURCE_ELEMENT = "resource";

	protected IViewManager manager;
	protected String viewURI;

	private ResourceRef[] resourcesRefs;

	public boolean getAllowMultiple() {
		return false;
	}

	public void init(IViewManager manager, IConfigurationElement config,
			IRender render) {
		super.init(config, render);
		this.manager = manager;
		viewURI = config.getAttribute(ATT_VIEW_URI);
		initResources(config);
	}

	protected void initResources(IConfigurationElement config) {
		IConfigurationElement[] rConfigs = config.getChildren(RESOURCE_ELEMENT);
		if (rConfigs == null)
			return;
		resourcesRefs = new ResourceRef[rConfigs.length];
		for (int j = 0; j < rConfigs.length; j++) {
			ResourceRef rr = new ResourceRef(rConfigs[j]);
			IConfigurationElement rConfig = rConfigs[j];
			resourcesRefs[j] = rr;
		}
	}

	public ResourceRef[] getResourcesRef() {
		return resourcesRefs;
	}

	public String get(String attri) {
		IConfigurationElement elem = getConfigurationElement();
		if (elem != null) {
			return elem.getAttribute(attri);
		}
		return null;
	}

	public void destroy() {
	}

	public String getURI() {
		return viewURI;
	}

}
