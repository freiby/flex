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
import com.wxxr.nirvana.platform.impl.PluginConfigurationElement;
import com.wxxr.nirvana.platform.impl.XMLConfigurationElement;
import com.wxxr.nirvana.workbench.IContributionItem;

/**
 * @author fudapeng
 *
 */
public class BaseContributionItem implements IContributionItem {

	protected IConfigurationElement elem;
	private String contributeId;
	private String contributeVersion;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wxxr.web.ui.IContributionItem#getConfigurationElement()
	 */
	public IConfigurationElement getConfigurationElement() {
		return elem;
	}

	protected void setConfigurationElement(IConfigurationElement config) {
		this.elem = config;
		PluginConfigurationElement element = getPluginConfigurationElement();
		if (element != null) {
			this.contributeId = element.getNamespaceIndentifier();
			this.contributeVersion = element.getPluginVersion().toString();
		}
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

	protected PluginConfigurationElement getPluginConfigurationElement() {
		XMLConfigurationElement elem = (XMLConfigurationElement) this.elem;
		for (; elem != null; elem = elem.getParent()) {
			if (elem instanceof PluginConfigurationElement) {
				return (PluginConfigurationElement) elem;
			}
		}
		return null;
	}

	public String getContributorVersion() {
		return contributeVersion;
	}

}
