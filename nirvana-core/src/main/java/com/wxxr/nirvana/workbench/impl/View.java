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
	private ResourceRef[] resourcesRefs;
	
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
		resourcesRefs = new ResourceRef[rConfigs.length];
		for (int j = 0; j < rConfigs.length; j++) {
			ResourceRef rr = new ResourceRef();
			IConfigurationElement rConfig = rConfigs[j];
			String rid = rConfig.getAttribute(UIComponent.ATT_REF);
			String init = rConfig.getAttribute("init");
			rr.init = init;
			rr.id = rid;
			resourcesRefs[j] = rr;
			resourcesIds[j] = rid;
		}
	}
	
	public class ResourceRef{
		public String id;
		public String init;
	}
	
	public void destroy() {
	}

	public String getViewURI() {
		return viewURI;
	}

	public ResourceRef[] getResourcesRef() {
		return resourcesRefs;
	}

	public String get(String attri) {
		IConfigurationElement elem = getConfigurationElement();
		if(elem != null){
			return elem.getAttribute(attri);
		}
		return null;
	}

}
