package com.wxxr.nirvana.platform;

/**
 * This interface describes a registry contributor - an entity that supplies information
 * to the extension registry. 
 * <p>
 * Registry contributor objects can be obtained by calling {@link IExtensionPoint#getContributor()}, 
 * {@link IExtension#getContributor()}, and {@link IConfigurationElement#getContributor()}.
 * Alternatively, a contributor factory appropriate for the registry in use can be called to directly
 * obtain an IContributor object.
 * </p><p>
 * This interface can be used without OSGi running.
 * </p><p>
 * This interface is not intended to be implemented or extended by clients.
 * </p>
 * @see org.eclipse.core.runtime.ContributorFactoryOSGi
 * @see org.eclipse.core.runtime.ContributorFactorySimple
 * 
 * @since org.eclipse.equinox.registry 3.2
 */
public interface IContributor {

	/**
	 * Provides name of the contributor (e.g., "org.eclipse.core.runtime").
	 * 
	 * @return name of the registry contributor 
	 */
	public String getName();
}
