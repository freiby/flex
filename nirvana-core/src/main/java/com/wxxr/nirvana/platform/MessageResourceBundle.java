/*
 * @(#)MessageResourceBundle.java	 2007-10-26
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.platform;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author neillin
 *
 */
public interface MessageResourceBundle {
  ResourceBundle getResourceBundle();
  ResourceBundle getResourceBundle(Locale locale);
  String getLocalizedMessage(String key);
  String getLocalizedMessage(String key,Locale locale);
}
