package com.wxxr.nirvana.workbench.impl;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.platform.impl.PluginConfigurationElement;
import com.wxxr.nirvana.platform.impl.XMLConfigurationElement;
import com.wxxr.nirvana.workbench.IRender;
import com.wxxr.nirvana.workbench.IUIContributionItem;

public class UIComponent implements IUIContributionItem {

	public static final String ATT_ID = "id";
	public static final String ATT_TOOLTIP = "toolTip";
	public static final String ATT_NAME = "name";
	public static final String ATT_REF = "ref";

	protected String description;
	protected String id;
	protected String name;
	protected String toolTipText;
	private IRender render;

	protected IConfigurationElement elem;

	private List<UIComponent> children = new ArrayList<UIComponent>();

	// 这个是组件所在插件的版本
	private String contributeVersion;
	// 这个是组件所在插件id
	private String contributeId;

	public void init(IConfigurationElement config, IRender render) {
		this.elem = config;
		id = config.getAttribute(ATT_ID);
		name = config.getAttribute(ATT_NAME);
		toolTipText = config.getAttribute(ATT_TOOLTIP);
		PluginConfigurationElement element = getPluginConfigurationElement();
		if (element != null) {
			this.contributeId = element.getNamespaceIndentifier();
			this.contributeVersion = element.getPluginVersion().toString();
		}
		this.render = render;
	}

	// 这个是组件所在插件id
	public String getContributorId() {
		return contributeId;
	}

	public String getContributorVersion() {
		return contributeVersion;
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

	public IConfigurationElement getConfigurationElement() {
		return elem;
	}

	public String getExtensionId() {
		return null;
	}

	public String getId() {
		return id;
	}

	public String getToolTipText() {
		return toolTipText;
	}

	public String getSubcontextURI() {
		return null;
	}

	public String getUniqueIndentifier() {
		return getContributorId() + "." + id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void addChild(UIComponent ui) {
		if (!children.contains(ui)) {
			children.add(ui);
		}
	}

	public void removeChild(UIComponent ui) {
		if (children.contains(ui)) {
			children.remove(ui);
		}
	}

	public IRender getRender(){
		return render;
	}
}
