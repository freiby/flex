package com.wxxr.nirvana.workbench.impl;

import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.platform.impl.PluginConfigurationElement;
import com.wxxr.nirvana.platform.impl.XMLConfigurationElement;
import com.wxxr.nirvana.workbench.IRender;

public abstract class CommonRender implements IRender {

	private String id;
	private String uri;
	private String contributeId;
	private String contributeVersion;

	public void render(UIComponent context) throws NirvanaException {
	}

	public String getUri() {
		return uri;
	}
	// 这个是组件所在插件id
	public String getContributorId() {
		return contributeId;
	}

	public String getContributorVersion() {
		return contributeVersion;
	}

	public final void init(IConfigurationElement config) {
		id = config.getAttribute("id");
		PluginConfigurationElement element = getPluginConfigurationElement(config);
		contributeId = element == null ? "" : element.getNamespaceIndentifier();
		contributeVersion = element == null ? "" : element.getPluginVersion()
				.toString();
		uri = config.getAttribute("uri");
	}

	protected PluginConfigurationElement getPluginConfigurationElement(
			IConfigurationElement config) {
		XMLConfigurationElement elem = (XMLConfigurationElement) config;
		for (; elem != null; elem = elem.getParent()) {
			if (elem instanceof PluginConfigurationElement) {
				return (PluginConfigurationElement) elem;
			}
		}
		return null;
	}
}
