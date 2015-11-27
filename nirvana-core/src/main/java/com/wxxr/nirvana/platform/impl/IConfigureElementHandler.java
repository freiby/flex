package com.wxxr.nirvana.platform.impl;

import java.io.Serializable;

import org.xml.sax.Attributes;

import com.wxxr.nirvana.platform.IConfigurationElement;


/**
 * @author nelin
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public interface IConfigureElementHandler extends Serializable {
	
	void setAttributes(Attributes attrs);
	void setContents(String context);
	void addChild(IConfigurationElement conf);
	void setElementName(String name);
	IConfigurationElement getConfigure();
	String getElementName();
}
