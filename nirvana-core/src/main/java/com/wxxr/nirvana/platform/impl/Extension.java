package com.wxxr.nirvana.platform.impl;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.platform.IContributor;
import com.wxxr.nirvana.platform.IExtension;
import com.wxxr.nirvana.platform.IExtensionPoint;
import com.wxxr.nirvana.platform.InvalidRegistryObjectException;


public class Extension implements IExtension {
	
	private ExtensionConfigurationElement configuration;
	
	public Extension(ExtensionConfigurationElement elem){
		this.configuration = elem;
	}

	public IConfigurationElement[] getConfigurationElements()
			throws InvalidRegistryObjectException {
		return configuration.getChildren();
	}

	public IContributor getContributor() throws InvalidRegistryObjectException {
		return configuration.getContributor();
	}

	public String getExtensionPointUniqueIdentifier()
			throws InvalidRegistryObjectException {
		return configuration.getAttribute(CoreConstants.EXTENSION_TARGET);
	}

	public String getLabel() throws InvalidRegistryObjectException {
		return configuration.getAttribute(CoreConstants.LABEL_NAME);
	}

	public String getNamespaceIdentifier()
			throws InvalidRegistryObjectException {
		return configuration.getNamespaceIdentifier();
	}

	public String getSimpleIdentifier() throws InvalidRegistryObjectException {
		return configuration.getAttribute(CoreConstants.EXTENSION_ID);
	}

	public String getUniqueIdentifier() throws InvalidRegistryObjectException {
		return getNamespaceIdentifier()+"."+getSimpleIdentifier();
	}

	public boolean isValid() {
		return true;
	}

	/**
	 * @return the configuration
	 */
	public IConfigurationElement getConfiguration() {
		return configuration;
	}

	/*
 * (non-javadoc)
 */
private IExtensionPoint iExtensionPoint;
 
/**
 * Getter of the property <tt>iExtensionPoint</tt>
 *
 * @return Returns the iExtensionPoint.
 * 
 */
public IExtensionPoint getIExtensionPoint()
{
	return iExtensionPoint;
}

/**
 * Setter of the property <tt>iExtensionPoint</tt>
 *
 * @param iExtensionPoint The iExtensionPoint to set.
 *
 */
public void setIExtensionPoint(IExtensionPoint iExtensionPoint ){
	this.iExtensionPoint = iExtensionPoint;
}

}
