package com.wxxr.nirvana.workbench.impl;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.workbench.IView;
import com.wxxr.nirvana.workbench.IViewManager;

public class View extends UIComponent implements IView{

	private static final String ATT_VIEW_URI="uri";
	
	private static final String RESOURCE_ELEMENT = "resource";
	
	protected IViewManager manager;
	protected String viewURI;
	
	private String[] resourcesIds;
	
	public boolean getAllowMultiple() {
		return false;
	}

	public void init(IViewManager manager, IConfigurationElement config) {
		super.init(config);
		this.manager = manager;
		viewURI = config.getAttribute(ATT_VIEW_URI);
		initResources(config);
	}
	
	
	
	protected void initResources(IConfigurationElement config) {
		IConfigurationElement[] rConfigs = config.getChildren(RESOURCE_ELEMENT);
		resourcesIds = new String[rConfigs.length];
		for (int j = 0; j < rConfigs.length; j++) {
			IConfigurationElement rConfig = rConfigs[j];
			String rid = rConfig.getAttribute(UIComponent.ATT_REF);
			resourcesIds[j] = rid;
		}
	}
	

	public void destroy() {
	}

	public String getViewURI() {
		return viewURI;
	}

}
