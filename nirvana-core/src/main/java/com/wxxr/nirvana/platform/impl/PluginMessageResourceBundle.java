


/*
 * @(#)PluginMessageResourceBundle.java	 2007-10-26
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.platform.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wxxr.nirvana.platform.MessageResourceBundle;
/**
 * @author neillin
 *
 */
public class PluginMessageResourceBundle implements
    MessageResourceBundle {

 private final static Log log = LogFactory.getLog(PluginMessageResourceBundle.class);
  private String pluginInstallURL;
  private String propertiesPath = "messages/";
  private String propertiesName = "message";
  
  private ClassLoader bundleClassLoader;
  /**
	 * 
	 * @param installURL	is the path that  webplugin.xml in it .
	 */
  public PluginMessageResourceBundle(String installURL){
    if(StringUtils.isBlank(installURL)){
      throw new IllegalArgumentException();
    }
    this.pluginInstallURL = installURL;
    if(pluginInstallURL.startsWith("file:")){
    	pluginInstallURL = pluginInstallURL.substring(5);
    }
  }
  /* (non-Javadoc)
   * @see com.wxxr.web.platform.core.MessageResourceBundle#getLocalizedMessage(java.lang.String)
   */
  public String getLocalizedMessage(String key) {
	  ResourceBundle bundle = null;
	  if((bundle = getResourceBundle()) != null){
		  try {
			String returnS = bundle.getString(key);
			  return new String(returnS.getBytes("ISO-8859-1"),"utf8").intern();
		} catch (UnsupportedEncodingException e) {
			if(log.isInfoEnabled()){
				log.info("Failed to convert message from ISO-8859-1 to UTF8",e);
			}
		} catch(MissingResourceException e){
			if(log.isInfoEnabled()){
				log.info("Cannot find message with key :" + key);
			}
		}
	  }
	  return key;
  }

  /* (non-Javadoc)
   * @see com.wxxr.web.platform.core.MessageResourceBundle#getLocalizedMessage(java.lang.String, java.util.Locale)
   */
  public String getLocalizedMessage(String key, Locale locale) {
	  if(getResourceBundle() != null){
		  //   code change   ISO-8859-1 is java load properties default codestyle  utf8 is the propertiesfile's codestyle  generally the style is  os default codestyle ( the program run on)  
		  String returnS = getResourceBundle(locale) .getString(key);
		  try {
			returnS = new String(returnS.getBytes("ISO-8859-1"),"utf8");
		} catch (UnsupportedEncodingException e) {
			log.error(e);
		}
	    return  (returnS != null) ? returnS : key;		  
	  }else{
		  return key;
	  }
	
  }

  /* (non-Javadoc)
   * @see com.wxxr.web.platform.core.MessageResourceBundle#getResourceBundle()
   */
  public ResourceBundle getResourceBundle() {
	  
	  return getBundle(Locale.getDefault());
  }

  /* (non-Javadoc)
   * @see com.wxxr.web.platform.core.MessageResourceBundle#getResourceBundle(java.util.Locale)
   */
  public ResourceBundle getResourceBundle(Locale locale) {
	  
	 return getBundle(locale);
	  
  }
  private ResourceBundle getBundle(Locale locale){
		  PropertyResourceBundle propertyResourceBundle = null;
		  ResourceBundle resourceBundle = null;
		try {
			if(bundleClassLoader == null){
				URL url = new File(pluginInstallURL,propertiesPath).toURI().toURL();
				URL[] urls = new URL[]{url};				
				bundleClassLoader = new URLClassLoader(urls);
			}
			resourceBundle = PropertyResourceBundle.getBundle(propertiesName, locale, bundleClassLoader);
			
		} catch (Exception e) {
			log.info("Failed to load message bundle :" + e.getLocalizedMessage());
			if(log.isDebugEnabled()){
				log.debug("load file error"  ,e);
			}
		}
		return resourceBundle;
	  
  }



}
