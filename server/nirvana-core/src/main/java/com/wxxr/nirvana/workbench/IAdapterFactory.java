package com.wxxr.nirvana.workbench;
/**
 * An adapter factory is used to create adaptor for specific interface class
 * one or more classes that implements the <code>IAdaptable</code>
 * interface. Adapter factories are registered with an
 * adapter manager.
 *
 * @see IAdapterManager
 * @see IAdaptable
 */
public interface IAdapterFactory {
	/**
	 * Returns an object which is an instance of the given class
	 * associated with the given object. Returns <code>null</code> if
	 * no such object can be found.
	 *
	 * @param adaptableObject the adaptable object being queried
	 *   (usually an instance of <code>IAdaptable</code>)
	 * @param adapterType the type of adapter to look up
	 * @return a object castable to the given adapter type, 
	 *    or <code>null</code> if this adapter factory 
	 *    does not have an adapter of the given type for the
	 *    given object
	 */
	<T> T getAdaptor( Class<T> adapterType, Object adaptableObject);
	/**
	 * Returns the collection of adapter types handled by this
	 * factory.
	 * <p>
	 * This method is generally used by an adapter manager
	 * to discover which adapter types are supported, in advance
	 * of dispatching any actual <code>getAdapter</code> requests.
	 * </p>
	 *
	 * @return the collection of adapter types
	 */
	public Class<?>[] getAdapterList();
}