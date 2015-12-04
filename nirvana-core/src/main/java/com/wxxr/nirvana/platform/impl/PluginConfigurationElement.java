/*
 * @(#)PluginConfigurationElement.java	 2007-10-24
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.platform.impl;

import java.net.URL;

import com.wxxr.nirvana.platform.IContributor;
import com.wxxr.nirvana.platform.PlatformLocator;
import com.wxxr.nirvana.platform.PluginVersionIdentifier;

/**
 * @author fudapeng
 *
 */
public class PluginConfigurationElement extends XMLConfigurationElement {
  
  private PluginVersionIdentifier version;
  private boolean deployed;  //it is not in use
  private URL pluginxmlURL;
  
  public String getNamespaceIndentifier(){
    return getAttribute(CoreConstants.PLUGIN_ID);
  }

  /* (non-Javadoc)
   * @see com.wxxr.web.platform.core.impl.XMLConfigurationElement#getContributor()
   */
  @Override
  public IContributor getContributor() {
    return PlatformLocator.getPlatform().getContributor(getNamespaceIndentifier());
  }

  public PluginVersionIdentifier getPluginVersion(){
    if(version == null){
      version = new PluginVersionIdentifier(getAttribute(Plugin.PLUGIN_VERSION));
    }
    return version;
  }

  /**
   * @return the deployed
   */
  public boolean isDeployed() {
    return deployed;
  }

  /**
   * @param deployed the deployed to set
   */
  public void setDeployed(boolean deployed) {
    this.deployed = deployed;
  }

  /**
   * @return the pluginxmlURL
   */
  public URL getPluginxmlURL() {
    return pluginxmlURL;
  }

  /**
   * @param pluginxmlURL the pluginxmlURL to set
   */
  public void setPluginxmlURL(URL pluginxmlURL) {
    this.pluginxmlURL = pluginxmlURL;
  }
}
