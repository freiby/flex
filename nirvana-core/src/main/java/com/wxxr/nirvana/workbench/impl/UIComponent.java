package com.wxxr.nirvana.workbench.impl;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.nirvana.platform.IConfigurationElement;
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
	
	protected IConfigurationElement elem;
	
	private List<UIComponent> children = new ArrayList<UIComponent>();
	
	public void init(IConfigurationElement config){
		this.elem = config;
		id = config.getAttribute(ATT_ID);
		name = config.getAttribute(ATT_NAME);
		toolTipText = config.getAttribute(ATT_TOOLTIP);
//		ref = config.getAttribute(ATT_REF);
	}
	
	public String getContributorId() {
		return (elem != null) ? elem.getNamespaceIdentifier() : null;
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
	
	public void addChild(UIComponent ui){
		if(!children.contains(ui)){
			children.add(ui);
		}
	}
	
	public void removeChild(UIComponent ui){
		if(children.contains(ui)){
			children.remove(ui);
		}
	}
}
