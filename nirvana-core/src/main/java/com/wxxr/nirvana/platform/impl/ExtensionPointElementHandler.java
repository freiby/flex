/*
 * @(#)ExtensionPointElementHandler.java	 2007-10-24
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.platform.impl;


/**
 * @author neillin
 *
 */
public class ExtensionPointElementHandler extends BaseElementHandler {

  private ExtensionPointConfigurationElement element = new ExtensionPointConfigurationElement();

  /* (non-Javadoc)
   * @see com.wxxr.web.platform.core.impl.BaseElementHandler#getXMLConfigureObject()
   */
  @Override
  protected XMLConfigurationElement getXMLConfigureObject() {
    return element;
  }

}
