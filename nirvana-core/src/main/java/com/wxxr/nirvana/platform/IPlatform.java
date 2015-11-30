/*
 * @(#)ICoreContext.java	 2007-10-23
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.platform;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author neillin
 *
 */
public interface IPlatform {
  IExtensionObjectFactory getObjectFactory();
  INameStrategy getNameStrategy();
  IContributor getContributor(String id);
  IExtensionRegistry getExtensionRegistry();
  IServiceRegistry getServiceRegsitry();
  /**
   * Returns the plug-in descriptor with the given plug-in identifier
   * in this platform, or <code>null</code> if there is no such
   * plug-in.  If there are multiple versions of the identified plug-in,
   * one will be non-deterministically chosen and returned.  
   *
   * @param pluginId the unique identifier of the plug-in 
   *    (e.g. <code>"com.example.acme"</code>).
   * @return the plug-in descriptor, or <code>null</code>
   */
  public IPluginDescriptor getPluginDescriptor(String pluginId);


  /**
   * Returns all plug-in descriptors known to this platform.
   * Returns an empty array if there are no installed plug-ins.
   *
   * @return the plug-in descriptors known to this platform
   */
  public IPluginDescriptor[] getPluginDescriptors();
  
  /**
   * deploy plugin from specific location, where a jar file or a folder contains all related resource resides
   * 
   * added at 2007-10-25
   * @param packageURL
   * @throws CoreException
   */
  public void deployPlugin(URL packageURL) throws CoreException;
  
  
  /**
   * deploy plugin with xmlString
   * 
   * added at 2009-3-19
   * @param pluginXmlString
   * @throws CoreException
   */
//  public void deployPluginByXml(String pluginXmlString) throws CoreException;
  
  /**
   * undeploy all version of plugin specified by pluginId if version is not specified.
   * or undeploy specific version of plugin if version is presented.
   * added at 2007-10-25
   * @param pluginId
   * @param version
   * @throws CoreException
   */
  public void undeployPlugin(String pluginId, String version) throws CoreException;
  
  /**
   * return the latest version number of plugin which deployed in this platform
   * added at 2007-10-25
   * @param pluginId
   * @return
   */
  public String getLatestDeployedPluginVersion(String pluginId);
  
  /**
   * check if specific plugin is deployed if version is not presented
   * check if specific version of plugin is deployed if version is presented
   * added at 2007-10-25
   * @param pluginId
   * @param version
   * @return
   */
  public boolean isPluginDeployed(String pluginId, String version);
  
  public boolean isPluginActivated(String pluginId, String version);
    
  public ClassLoader getPlatformClassLoader();
  
  /**
   * activate specific version of deployed plugin, or activate latest version of deployed plugin
   * if version is not presented.
   * added at 2007-10-27
   * @param pluginId
   * @param version
   * @throws CoreException
   */
  public void activatePlugin(String pluginId, String version) throws CoreException;
  
  /**
   * deactivate currently activated plugin
   * added at 2007-10-27
   * @param pluginId
   * @param version
   * @throws CoreException
   */
  public void deActivatePlugin(String pluginId) throws CoreException;
  
  public String getPackageUploadURL();
  
  public String getPluginRootDir();
  
  public Object createPluginObject(IPluginDescriptor plugin, String className, IConfigurationElement parameters) throws Exception;
  
  public Object createPluginObject(String pluginId, String className, IConfigurationElement parameters) throws Exception;
  
  public Class<?> loadPluginClass(String pluginId, String className) throws ClassNotFoundException;
  
  public void registerWebClassLoader(URLClassLoader classLoader);
  
  public void unregisterWebClassLoader(URLClassLoader classLoader);
  
  public String getWebPluginXmlContent( String pluginId, String version );
  public String listActiveVersionOfPlugin(String pluginId);
  public String[] listAllPluginNames() ;
  public String[] listAllVersionsOfPlugin(String pluginId);
  public void activatePlugins()  throws Exception;
  
  public void start() throws Exception;

}
