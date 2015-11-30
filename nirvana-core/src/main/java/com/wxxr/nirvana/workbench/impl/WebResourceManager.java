/*
 * @(#)WebResourceManager.java	 2007-11-1
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.workbench.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.platform.IExtension;
import com.wxxr.nirvana.workbench.IWebResource;
import com.wxxr.nirvana.workbench.UIConstants;
import com.wxxr.nirvana.workbench.config.BaseExtensionPointManager;

/**
 * @author neillin
 *
 */
public class WebResourceManager extends BaseExtensionPointManager  {

	private static final String RESOURCE_ELEMENT_NAME="resource";
	private static final Log log = LogFactory.getLog(WebResourceManager.class);
	private Timer timer;
	private List<IWebResource> list = null;
	public WebResourceManager() {
		super(UIConstants.UI_NAMESPACE,UIConstants.EXTENSION_POINT_RESOURCES);
	}

	
	protected void processExtensionAdded(IExtension ext){
		list = new ArrayList<IWebResource>();
		IConfigurationElement[] configs = ext.getConfigurationElements();
		for (int i = 0; i < configs.length; i++) {
			IConfigurationElement elem = configs[i];
			if((elem != null)&&RESOURCE_ELEMENT_NAME.equalsIgnoreCase(elem.getName())){
				WebResource res = new WebResource(elem);
				list.add(res);
			}
		}
	}

	
	public void destroy(){
		super.stop();
	}


	@Override
	protected void processExtensionRemoved(IExtension ext) {
		
	}

}
