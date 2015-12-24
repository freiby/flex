package com.wxxr.nirvana.workbench;
/**
 * An adapter manager maintains a registry of adapter factories.
 * Clients directly invoke methods on an adapter manager to register
 * and unregister adapters.
 * All adaptable objects (that is, objects that implement the 
 * <code>IAdaptable</code> interface) funnel 
 * <code>IAdaptable.getAdapter</code> invocations to their
 * adapter manager's <code>IAdapterManger.getAdapter</code>
 * method. The adapter manager then forwards this request
 * unmodified to the <code>IAdapterFactory.getAdapter</code> 
 * method on one of the registered adapter factories.
 *
 * @see IAdaptable
 * @see IAdapterFactory
 */
public interface IAdapterManager {
	/**
	 * Returns an object which is an instance of the given class
	 * associated with the given object. Returns <code>null</code> if
	 * no such object can be found.
	 *
	 * @param adaptable the adaptable object being queried
	 *   (usually an instance of <code>IAdaptable</code>)
	 * @param adapterType the type of adapter to look up
	 * @return a object castable to the given adapter type, 
	 *    or <code>null</code> if the given adaptable object does not
	 *    have an adapter of the given type
	 */
	<T> T getAdapter(Class<T> adapterType, Object adaptable);
	/**
	 * Registers the given adapter factory as extending objects of
	 * the given type.
	 * <p>
	 * If the type being extended is a class,
	 * the given factory's adapters are available on instances
	 * of that class and any of its subclasses.  If it is an interface, 
	 * the adapters are available to all classes that directly 
	 * or indirectly implement that interface.
	 * </p>
	 *
	 * @param factory the adapter factory
	 * @param adaptable the type being extended
	 * @see #unregisterAdapters
	 */
	public void registerAdapters(Class<?> adaptable,IAdapterFactory factory);
	/**
	 * Removes the given adapter factory completely from the list of 
	 * registered factories. Equivalent to calling
	 * <code>unregisterAdapters(IAdapterFactory,Class)</code>
	 * on all classes against which it had been explicitly registered.
	 * Does nothing if the given factory is not currently registered.
	 *
	 * @param factory the adapter factory to remove
	 * @see #registerAdapters
	 */
	public void unregisterAdapters(IAdapterFactory factory);
	/**
	 * Removes the given adapter factory from the list of factories
	 * registered as extending the given class.
	 * Does nothing if the given factory and type combination is not
	 * registered.
	 *
	 * @param factory the adapter factory to remove
	 * @param adaptable one of the types against which the given
	 *    factory is registered
	 * @see #registerAdapters
	 */
	public void unregisterAdapters(Class<?> adaptable, IAdapterFactory factory);
}