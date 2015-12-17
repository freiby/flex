package com.wxxr.nirvana.platform;

/**
 * An extension delta represents changes to the extension registry.
 * <p>
 * This interface can be used without OSGi running.
 * </p>
 * <p>
 * This interface is not intended to be implemented by clients.
 * </p>
 * 
 * @since 3.0
 */
public interface IExtensionDelta {
	/**
	 * Delta kind constant indicating that an extension has been added to an
	 * extension point.
	 * 
	 * @see IExtensionDelta#getKind()
	 */
	public int ADDED = 1;
	/**
	 * Delta kind constant indicating that an extension has been removed from an
	 * extension point.
	 * 
	 * @see IExtensionDelta#getKind()
	 */
	public int REMOVED = 2;

	/**
	 * The kind of this extension delta.
	 * 
	 * @return the kind of change this delta represents
	 * @see #ADDED
	 * @see #REMOVED
	 */
	public int getKind();

	/**
	 * Returns the affected extension.
	 * 
	 * @return the affected extension
	 */
	public IExtension getExtension();

	/**
	 * Returns the affected extension point.
	 * 
	 * @return the affected extension point
	 */
	public IExtensionPoint getExtensionPoint();
}
