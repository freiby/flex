package com.wxxr.nirvana.platform.impl;

import org.xml.sax.Attributes;

import com.wxxr.nirvana.platform.IConfigurationElement;

public abstract class BaseElementHandler implements IConfigureElementHandler {

	private int childCount = 0;
	/**
	 * Constructor for BaseElementHandler.
	 */
	public BaseElementHandler() {
		super();
	}

	/**
	 * @see com.bmo.dol.frontend.configuration.xmlparser.IConfigureElementHandler#setAttributes(Attributes)
	 */
	public void setAttributes(Attributes attrs) {
		getXMLConfigureObject().setAttrs(attrs);
	}

	/**
	 * @see com.bmo.dol.frontend.configuration.xmlparser.IConfigureElementHandler#setContents(String)
	 */
	public void setContents(String context) {
		getXMLConfigureObject().setValue(context);
	}

	/**
	 * @see com.bmo.dol.frontend.configuration.xmlparser.IConfigureElementHandler#setContents(String, ConfigureObject)
	 */
	public void addChild(IConfigurationElement conf) {
		getXMLConfigureObject().addChild(conf);
		childCount++;
		if(conf instanceof XMLConfigurationElement)
			((XMLConfigurationElement)conf).setSeqNumber(childCount);
	}

	/**
	 * @see com.bmo.dol.frontend.configuration.xmlparser.IConfigureElementHandler#setElementName(String)
	 */
	public void setElementName(String name) {
		getXMLConfigureObject().setName(name);
	}

	/**
	 * @see com.bmo.dol.frontend.configuration.xmlparser.IConfigureElementHandler#getConfigure()
	 */
	public IConfigurationElement getConfigure() {
		return getXMLConfigureObject();
	}

	protected abstract XMLConfigurationElement getXMLConfigureObject();
	/**
	 * @see com.bmo.dol.frontend.appfw.configuration.xmlparser.IConfigureElementHandler#getElementName()
	 */
	public String getElementName() {
		return getXMLConfigureObject().getName();
	}

}
