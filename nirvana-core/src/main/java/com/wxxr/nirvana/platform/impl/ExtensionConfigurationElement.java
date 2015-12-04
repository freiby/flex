/*
 * @(#)ExtensionConfigurationElement.java	 2007-10-24
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.platform.impl;

/**
 * @author fudapeng
 *
 */
public class ExtensionConfigurationElement extends XMLConfigurationElement {
	/**
	 * return the Identifier of extension point which this extension extends
	 * added at 2007-12-20
	 * @return
	 */
	public String getExtensionPointId(){
		return getAttribute(CoreConstants.EXTENSION_TARGET);
	}
	
	/**
	 * return the id of extension  which this extension declare
	 * added at 2007-12-20
	 * @return
	 */
	public String getExtensionId(){
		return getAttribute(CoreConstants.EXTENSION_ID);
	}
}
