/*
 * @(#)BaseContributionItem.java	 2007-11-12
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.workbench.config;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.workbench.IContributionItem;

/**
 * @author neillin
 *
 */
public class BaseContributionItem implements IContributionItem {

	protected IConfigurationElement elem;

	/* (non-Javadoc)
	 * @see com.wxxr.web.ui.IContributionItem#getConfigurationElement()
	 */
	public IConfigurationElement getConfigurationElement() {
		return elem;
	}
	
	
	protected void setConfigurationElement(IConfigurationElement config){
		this.elem = config;
	}


	/**
	 * @return the contributorId
	 */
	public String getContributorId() {
		return (elem != null) ? elem.getNamespaceIdentifier() : null;
	}


	public String getExtensionId() {
		return (elem != null) ? elem.getDeclaringExtensionId() : null;
	}

}
