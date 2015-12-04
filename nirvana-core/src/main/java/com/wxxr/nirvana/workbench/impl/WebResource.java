/*
 * @(#)WebResource.java	 2007-11-2
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.workbench.impl;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.workbench.IWebResource;
import com.wxxr.nirvana.workbench.config.BaseContributionItem;

/**
 * @author fudapeng
 *
 */
public class WebResource extends BaseContributionItem implements IWebResource {
	
	public static final String ATT_SOURCE_DIR = "uri";
	private static final String ATT_SOURCE_TYPE = "type";
	
	

	public WebResource(IConfigurationElement elem) {
		if(elem == null){
			throw new IllegalArgumentException();
		}
		setConfigurationElement(elem);
	}


	/* (non-Javadoc)
	 * @see com.wxxr.web.ui.resources.IWebResource#getType()
	 */
	public String getType() {
		return elem.getAttribute(ATT_SOURCE_TYPE);
	}

	public void destroy() {
	}
	

	public String getUri() {
		return elem.getAttribute(ATT_SOURCE_DIR);
	}

}
