/*
 * @(#)WebResource.java	 2007-11-2
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.workbench.impl;

import java.io.File;
import java.net.URL;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.platform.IPluginDescriptor;
import com.wxxr.nirvana.workbench.IWebResource;
import com.wxxr.nirvana.workbench.IWorkbenchManager;
import com.wxxr.nirvana.workbench.config.BaseContributionItem;

/**
 * @author neillin
 *
 */
public class WebResource extends BaseContributionItem implements IWebResource {
	
	public static final String ATT_SOURCE_DIR = "src";
	public static final String ATT_SOURCE_TYPE = "type";
	public static final String ATT_MAP_TO = "mapTo";
	

	private String sourceDirectory;
	private String mapToPath;
	
	public WebResource(IConfigurationElement elem) {
		if(elem == null){
			throw new IllegalArgumentException();
		}
		setConfigurationElement(elem);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.web.ui.resources.IWebResource#getMapToURI()
	 */
	public String getMapToURI() {
		return elem.getAttribute(ATT_MAP_TO);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.web.ui.resources.IWebResource#getSourceDirectory()
	 */
	public synchronized String getSourceDirectory() {
		if(sourceDirectory == null){
			IPluginDescriptor plugin = getWorkbenchManager().getUIPlatform().getPluginDescriptor(elem.getNamespaceIdentifier());
			try {
				sourceDirectory = new File(new URL(plugin.getInstallURL()).getPath(),elem.getAttribute(ATT_SOURCE_DIR)).getAbsolutePath();
			} catch (Exception e) {
			}
		}
		return sourceDirectory;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.web.ui.resources.IWebResource#getType()
	 */
	public String getType() {
		return elem.getAttribute(ATT_SOURCE_TYPE);
	}

	
	protected IWorkbenchManager getWorkbenchManager(){
//		return WorkbenchManagerLocator.getWorkbenchManager();
		return null;
	}

	public String getRealPathOfMapTo() {
//		if(mapToPath == null){
//			mapToPath = new File(getWorkbenchManager().getWorkbenchDocumentRoot(),getMapToURI()).getAbsolutePath();
//		}
//		return mapToPath;
		return null;
	}

}
