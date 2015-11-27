/*
 * @(#)DefaultElementHandler.java	 2007-10-24
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
public class DefaultElementHandler extends BaseElementHandler {

  private XMLConfigurationElement element = new XMLConfigurationElement();

  /* (non-Javadoc)
   * @see com.wxxr.web.platform.core.impl.BaseElementHandler#getXMLConfigureObject()
   */
  @Override
  protected XMLConfigurationElement getXMLConfigureObject() {
    return element;
  }

}
