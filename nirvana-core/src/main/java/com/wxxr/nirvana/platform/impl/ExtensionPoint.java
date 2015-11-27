package com.wxxr.nirvana.platform.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.platform.IContributor;
import com.wxxr.nirvana.platform.IExtension;
import com.wxxr.nirvana.platform.IExtensionPoint;
import com.wxxr.nirvana.platform.InvalidRegistryObjectException;


public class ExtensionPoint implements IExtensionPoint {
  
  private static final IExtension[] EMPTY_EXTENSIONS = new IExtension[0];

	private Map<String,IExtension> extensions = new ConcurrentHashMap<String, IExtension>();
	private ExtensionPointConfigurationElement config;
	
	/**
   * 
   */
  public ExtensionPoint(ExtensionPointConfigurationElement config) {
    super();
    if(config == null){
      throw new IllegalArgumentException();
    }
    this.config = config;
  }

  public IConfigurationElement[] getConfigurationElements()
			throws InvalidRegistryObjectException {
		return config.getChildren();
	}

	public IContributor getContributor() throws InvalidRegistryObjectException {
		// TODO Auto-generated method stub
		return null;
	}

	public IExtension getExtension(String extensionId)
			throws InvalidRegistryObjectException {
		return extensions.get(extensionId);
	}

	public IExtension[] getExtensions() throws InvalidRegistryObjectException {
		if (extensions.isEmpty())
			return EMPTY_EXTENSIONS;
		return extensions.values().toArray(new IExtension[extensions.size()]);
	}

	public String getLabel() throws InvalidRegistryObjectException {
		return config.getAttribute(CoreConstants.LABEL_NAME);
	}

	public String getNamespaceIdentifier()
			throws InvalidRegistryObjectException {
		return config.getNamespaceIdentifier();
	}

	public String getSchemaReference() throws InvalidRegistryObjectException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getSimpleIdentifier() throws InvalidRegistryObjectException {
		return config.getAttribute(CoreConstants.EXTENSION_POINT_ID);
	}

	public String getUniqueIdentifier() throws InvalidRegistryObjectException {
		return getNamespaceIdentifier()+"."+getSimpleIdentifier();
	}

	public boolean isValid() {
		return true;
	}
	public void addExtensions(IExtension ext) 
			throws InvalidRegistryObjectException {
		if(ext == null){
		  throw new IllegalArgumentException();
		}
		extensions.put(ext.getUniqueIdentifier(), ext);
	}
	
	public boolean removeExtension(IExtension ext) {
    if(ext == null){
      throw new IllegalArgumentException();
    }
    return (removeExtension(ext.getUniqueIdentifier()) != null);	  
	}
	
	public IExtension removeExtension(String extensionId) {
		return extensions.remove(extensionId);    
	}
  
	public IConfigurationElement getConfiguration() {
		return config;
	}
}
