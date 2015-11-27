/*
 * @(#)PluginDescriptor.java	 2007-10-24
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.platform.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wxxr.nirvana.platform.CoreException;
import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.platform.IExtension;
import com.wxxr.nirvana.platform.IExtensionPoint;
import com.wxxr.nirvana.platform.IExtensionRegistry;
import com.wxxr.nirvana.platform.IPlatform;
import com.wxxr.nirvana.platform.IPluginDescriptor;
import com.wxxr.nirvana.platform.IPluginInitializer;
import com.wxxr.nirvana.platform.IRuntimeLibrary;
import com.wxxr.nirvana.platform.MessageResourceBundle;
import com.wxxr.nirvana.platform.PluginVersionIdentifier;

/**
 * @author neillin
 *
 */
public class Plugin implements IPluginDescriptor, IPluginInitializer {

  public static final String PLUGIN_DEPENDENCIES = "requires";
  public static final String PLUGIN_PROVIDER_NAME = "provider-name";
  public static final String PLUGIN_VERSION = "version";
  public static final String PLUGIN_NAME = "name";
  public static final String PLUGIN_INITIALIZER_CLASS = "class";
  public static final String PLUGIN_RUNTIME="runtime";
  public static final String PLUGIN_RUNTIME_LIBRARY="library";
  public static final String PLUGIN_RUNTIME_LIBRARY_NAME="name";
  public static final String PLUGIN_RUNTIME_LIBRARY_EXPORTS="exports";
  
  private final static String[] EMPTY_STRING_ARRAY = new String[0];
  private final static Log log = LogFactory.getLog(Plugin.class);
  
  private PluginConfigurationElement configure;
  private IExtensionRegistry registry;
  private String installURL;
  private MessageResourceBundle messageResourceBundle;
  private PluginVersionIdentifier versionIdentifier;
  private IRuntimeLibrary runtimeLibrary;
  private ClassLoader pluginClassLoader;
  private String[] dependencies;
  private boolean initialized;
  private boolean localPlugin;
  private IPluginInitializer pluginInitializer;
  
  public Plugin(PluginConfigurationElement configure,IExtensionRegistry registry){
    if((configure == null)||(registry == null)){
      throw new IllegalArgumentException();
    }
    this.configure = configure;
    this.registry = registry;
  }
  /* (non-Javadoc)
   * @see com.wxxr.web.platform.core.IPluginDescriptor#getDependencies()
   */
  public String[] getDependencies() {
    if(dependencies == null){
	    IConfigurationElement runtime = configure.getChild(PLUGIN_DEPENDENCIES);
	    if(runtime == null){
	    	dependencies = EMPTY_STRING_ARRAY;
	    }else{
	    	String s = runtime.getValue();//configure.getAttribute(PLUGIN_DEPENDENCIES);
	    	if(StringUtils.isNotBlank(s)){
	    		dependencies = s.trim().split(",");
	    	}else{
	    		dependencies = EMPTY_STRING_ARRAY;
	    	}
	    	
	    }
    }
    String[] temp = new String[dependencies.length];
    for (int i=0;i<dependencies.length;i++) {
		temp[i] = dependencies[i].trim();
	}
    dependencies = temp;
    return dependencies;
  }

  /* (non-Javadoc)
   * @see com.wxxr.web.platform.core.IPluginDescriptor#getExtension(java.lang.String)
   */
  public IExtension getExtension(String extensionName) {
    return registry.getExtension(configure.getNamespaceIndentifier()+"."+extensionName);
  }

  /* (non-Javadoc)
   * @see com.wxxr.web.platform.core.IPluginDescriptor#getExtensionPoint(java.lang.String)
   */
  public IExtensionPoint getExtensionPoint(String extensionPointId) {
    return registry.getExtensionPoint(configure.getNamespaceIndentifier(),extensionPointId);
  }

  /* (non-Javadoc)
   * @see com.wxxr.web.platform.core.IPluginDescriptor#getExtensionPoints()
   */
  public IExtensionPoint[] getExtensionPoints() {
    return registry.getExtensionPoints(configure.getNamespaceIndentifier());
  }

  /* (non-Javadoc)
   * @see com.wxxr.web.platform.core.IPluginDescriptor#getExtensions()
   */
  public IExtension[] getExtensions() {
    return registry.getExtensions(configure.getNamespaceIndentifier());
  }

  /* (non-Javadoc)
   * @see com.wxxr.web.platform.core.IPluginDescriptor#getInstallURL()
   */
  public String getInstallURL() {
    return installURL;
  }

  /* (non-Javadoc)
   * @see com.wxxr.web.platform.core.IPluginDescriptor#getLabel()
   */
  public String getLabel() {
    return configure.getAttribute(CoreConstants.LABEL_NAME);
  }

  /* (non-Javadoc)
   * @see com.wxxr.web.platform.core.IPluginDescriptor#getProviderName()
   */
  public String getProviderName() {
    return configure.getAttribute(PLUGIN_PROVIDER_NAME);
  }

  /* (non-Javadoc)
   * @see com.wxxr.web.platform.core.IPluginDescriptor#getResourceString(java.lang.String)
   */
  public String getResourceString(String value) {
    String msg = null;
    if(messageResourceBundle != null){
      msg = messageResourceBundle.getLocalizedMessage(value);
    }
    if(msg == null){
      msg = value;
    }
    return msg;
  }

  /* (non-Javadoc)
   * @see com.wxxr.web.platform.core.IPluginDescriptor#getResourceString(java.lang.String, com.wxxr.common.util.MessageResourceBundle)
   */
  public String getResourceString(String value,
      MessageResourceBundle resourceBundle) {
    String msg = null;
    if(resourceBundle != null){
      msg = resourceBundle.getLocalizedMessage(value);
    }
    if(msg == null){
      msg = getResourceString(value);
    }
    return msg;
  }

  /* (non-Javadoc)
   * @see com.wxxr.web.platform.core.IPluginDescriptor#getUniqueIdentifier()
   */
  public String getUniqueIdentifier() {
    return configure.getNamespaceIndentifier();
  }

  /* (non-Javadoc)
   * @see com.wxxr.web.platform.core.IPluginDescriptor#getVersionIdentifier()
   */
  public PluginVersionIdentifier getVersionIdentifier() {
    if(versionIdentifier == null){
      versionIdentifier = new PluginVersionIdentifier(configure.getAttribute(PLUGIN_VERSION));
    }
    return versionIdentifier;
  }

  /**
   * @param installURL the installURL to set
   */
  public void setInstallURL(String installURL) {
    this.installURL = installURL;
  }
  /**
   * @return the messageResourceBundle
   */
  public MessageResourceBundle getMessageResourceBundle() {
    return messageResourceBundle;
  }
  /**
   * @param messageResourceBundle the messageResourceBundle to set
   */
  public void setMessageResourceBundle(MessageResourceBundle messageResourceBundle) {
    this.messageResourceBundle = messageResourceBundle;
  }

  /**
   * @return the runtimeLibray
   */
  public IRuntimeLibrary getRuntimeLibrary() {
    if(runtimeLibrary == null){
      IConfigurationElement runtime = configure.getChild(PLUGIN_RUNTIME);
      if(runtime != null){
        IConfigurationElement[] libraries = runtime.getChildren(PLUGIN_RUNTIME_LIBRARY);
        if((libraries != null)&&(libraries.length > 0)){
          StringBuffer buf = new StringBuffer();
          LinkedList<URL> urls = new LinkedList<URL>();
          int cnt = 0;
          for (int j = 0; j < libraries.length; j++) {
            IConfigurationElement elem = libraries[j];
            String s = elem.getAttribute(PLUGIN_RUNTIME_LIBRARY_NAME);
            if(StringUtils.isNotBlank(s)){
              String path = getInstallURL();
              if(path.startsWith("file:")){
            	  path = path.substring(5);
              }
              File f = new File(path,s);
              try {
                urls.add(f.toURL());
                if(log.isDebugEnabled()){
                  log.debug("Add runtime library :"+ f.getAbsolutePath());
                }
                s = elem.getAttribute(PLUGIN_RUNTIME_LIBRARY_EXPORTS);
                if(StringUtils.isNotBlank(s)){
                  if(cnt > 0){
                    buf.append(',');
                  }
                  buf.append(StringUtils.trim(s));
                  cnt++;
                  if(log.isDebugEnabled()){
                    log.debug("Add exports pattern :"+ s);
                  }
                }
              } catch (MalformedURLException e) {
                log.error("Invalid library :"+s,e);
                throw new CoreException("Invalid library :"+s,e);
              }
            }
          }
          RuntimeLibrary rLibrary = new RuntimeLibrary();
          rLibrary.setJarFiles(urls.toArray(new URL[urls.size()]));
          rLibrary.setExports(buf.toString());
          rLibrary.setPluginId(getUniqueIdentifier());
          runtimeLibrary = rLibrary;
        }
      }
    }
    return runtimeLibrary;
  }
  /* (non-Javadoc)
   * @see com.wxxr.web.platform.core.IPluginDescriptor#getActivatorClass()
   */
  protected String getInitializerDelegateClass() {
    return configure.getAttribute(PLUGIN_INITIALIZER_CLASS);
  }
  /* (non-Javadoc)
   * @see com.wxxr.web.platform.core.IPluginDescriptor#getName()
   */
  public String getName() {
    return configure.getAttribute(PLUGIN_NAME);
  }
  /* (non-Javadoc)
   * @see com.wxxr.web.platform.core.IPluginDescriptor#isLocalPlugin()
   */
  public boolean isLocalPlugin() {
    return localPlugin;
  }
  /**
   * @param localPlugin the localPlugin to set
   */
  public void setLocalPlugin(boolean localPlugin) {
    this.localPlugin = localPlugin;
  }
  /* (non-Javadoc)
   * @see com.wxxr.web.platform.core.IPluginDescriptor#getPluginClassLoader()
   */
  public ClassLoader getPluginClassLoader() {
    return pluginClassLoader;
  }
  
  public void setPluginClassLoader(ClassLoader pluginClassLoader) {
    this.pluginClassLoader = pluginClassLoader;
  }
  /**
   * @return the pluginActivator
   */
  public IPluginInitializer getPluginInitializer() {   
    return this;
  }
  
  /**
   * @return the pluginActivator
   */
  protected IPluginInitializer getInitializerDelegate() {
    if((pluginInitializer == null)&&StringUtils.isNotBlank(getInitializerDelegateClass())){
      try {
    	  pluginInitializer = (IPluginInitializer)getPluginClassLoader().loadClass(getInitializerDelegateClass()).newInstance();
      } catch (Exception e) {
        throw new CoreException("Failed to create plugin activator for plugin :"+getUniqueIdentifier(),e);
      }
    }
    return pluginInitializer;
  }

  /* (non-Javadoc)
   * @see com.wxxr.web.platform.core.IPluginDescriptor#getConfigurationElement()
   */
  public IConfigurationElement getConfigurationElement() {
    return configure;
  }
  
	public boolean isPluginInitialized() {
		return initialized;
	}
	
	public void destroy(IPluginDescriptor plugin) {
		IPluginInitializer delegate = getInitializerDelegate();
		if(delegate != null){
			delegate.destroy(plugin);
		}
		this.initialized = false;
	}
	public void init(IPluginDescriptor plugin, IPlatform platform)
			throws CoreException {
		IPluginInitializer delegate = getInitializerDelegate();
		if(delegate != null){
			delegate.init(plugin,platform);
		}
		this.initialized = true;		
	}
	
	private String versionTimeStamp = "";
	public void setVersionTimeStamp(String time){
		versionTimeStamp = time;
	}
	public String getVersionTimeStamp(){
		return versionTimeStamp;
	}

}
