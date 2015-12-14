package com.wxxr.nirvana.workbench.impl;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.wxxr.nirvana.workbench.IAdaptableProvider;
import com.wxxr.nirvana.workbench.IAdapterFactory;
import com.wxxr.nirvana.workbench.IAdapterManager;

/**
 * This class is a default implementation of <code>IAdapterManager</code>.
 * It provides fast lookup of property values with the following semantics:
 * <ul>
 * <li> At most one adapter will be invoked per property lookup
 * <li> If multiple installed adapters provide the same property, only 
 *		the first found in the search order is said to <i>define</i> the property 
 *		as it is the only adapter which will be invoked..
 * <li> The search order from a class with the definition<br>
 *			<code>class X extends Y implements A, B</code><br>
 *		is as follows:
 * 		<il>
 *			<li>the target's class: X
 *			<li>X's superclasses in order to <code>Object</code>
 *			<li>a depth-first traversal of the target class's interaces in the order 
 *				returned by <code>getInterfaces</code> (in the example, A and 
 *				its superinterfaces then B and its superinterfaces)
 *		</il>
 * </ul>
 *
 * @see IAdapter
 * @see IAdapterManager
 */
public class AdapterManager implements IAdapterManager {

	/** Table of factories, keyed by <code>Class</code>. */
	protected Hashtable<Class<?>, Vector<IAdapterFactory>> factories;

	/** Cache of adapter search paths; <code>null</code> if none. */
	protected Hashtable<Class<?>,Hashtable<Class<?>,IAdapterFactory>> lookup;
	/** Constructs a new extender manager.
	 */
	public AdapterManager() {
		factories = new Hashtable<Class<?>, Vector<IAdapterFactory>>(5);
		lookup = null;
	}
	/**
	 * Given a list of types, add all of the property entries for the installed
	 * extenders into the lookupTable.  Each entry will be keyed by the property
	 * identifier (supplied in IExtender.getPropertyList) and the extender
	 * supplying that property.
	 */
	private void addFactoriesFor(Class<?>[] types, Hashtable<Class<?>,IAdapterFactory> lookupTable) {
		for (int j=0 ; j < types.length ; j++) {
			Class<?> clazz = types[j];
			Vector<IAdapterFactory> factoryList = factories.get(clazz);
			if (factoryList == null)
				continue;
			for (Enumeration<IAdapterFactory> list = factoryList.elements(); list.hasMoreElements();) {
				IAdapterFactory factory = (IAdapterFactory) list.nextElement();
				Class<?>[] adapters = factory.getAdapterList();
				for (int i = 0; i < adapters.length; i++) {
					Class<?> adapter = adapters[i];
					if (lookupTable.get(adapter) == null)
						lookupTable.put(adapter, factory);
				}
			}
		}
	}
	/**
	 * Returns the class search order starting with <code>extensibleClass</code>.
	 * The search order is defined in this class' comment.
	 */
	private Class<?>[] computeClassOrder(Class<?> extensibleClass) {
		Vector<Class<?>> result = new Vector<Class<?>>(4);
		Class<?> clazz = extensibleClass;
		while (clazz != null) {
			result.addElement(clazz);
			clazz = clazz.getSuperclass();
		}
		return result.isEmpty() ? new Class[0] : result.toArray(new Class[result.size()]);
	}
	/**
	 * Returns the interface search order for the class hierarchy described
	 * by <code>classList</code>.
	 * The search order is defined in this class' comment.
	 */
	private Class<?>[] computeInterfaceOrder(Class<?>[] classList) {
		Vector<Class<?>> result = new Vector<Class<?>>(4);
		Hashtable<Class<?>,Class<?>> seen = new Hashtable<Class<?>, Class<?>>(4);
		for (int i=0 ; i < classList.length ; i++) {
			Class<?>[] interfaces = classList[i].getInterfaces();
			internalComputeInterfaceOrder(interfaces, result, seen);
		}
		return result.isEmpty() ? new Class[0] : result.toArray(new Class[result.size()]);
	}
	/**
	 * Flushes the cache of extender search paths.  This is generally required
	 * whenever an extender is added or removed.  
	 * <p>
	 * It is likely easier to just toss the whole cache rather than trying to be
	 * smart and remove only those entries affected.
	 * </p>
	 */
	public void flushLookup() {
		lookup = null;
	}
	/*
	 * @see IAdapterManager#getAdapter
	 */
	public <T>  T getAdapter(Class<T> target,Object object) {
		Class<?> srcClass = object instanceof Class ? (Class<?>)object : (object instanceof IAdaptableProvider ? ((IAdaptableProvider)object).getAdaptableClass() : object.getClass());
		IAdapterFactory factory = getFactory(srcClass, target);
		T result = null;
		if (factory != null)
			result = factory.getAdaptor(target ,object);
		if (result == null && target.isInstance(object))
			return target.cast(object);
		return result;
	}
	/**
	 * Gets the extender installed for objects of class <code>extensibleClass</code>
	 * which defines the property identified by <code>key</code>.  If no such
	 * extender exists, returns null.
	 */
	private IAdapterFactory getFactory(Class<?> extensibleClass, Class<?> adapter) {
		Hashtable<Class<?>,IAdapterFactory> table;
		// check the cache first.
		if (lookup != null) {
			table = lookup.get(extensibleClass);
			if (table != null)
				return (IAdapterFactory) table.get(adapter);
		}
		// Its not in the cache so we have to build the extender table for this class.
		// The table is keyed by property identifier.  The 
		// value is the <b>sole<b> extender which defines that property.  Note that if
		// if multiple extenders technically define the same property, only the first found
		// in the search order is considered.
		table = new Hashtable<Class<?>,IAdapterFactory>(4);
		// get the list of all superclasses and add the extenders installed for each 
		// of those classes to the table.  
		Class<?>[] classList = computeClassOrder(extensibleClass);
		addFactoriesFor(classList, table);
		// get the ordered set of all interfaces for the extensible class and its 
		// superclasses and add the extenders installed for each 
		// of those interfaces to the table.  
		classList = computeInterfaceOrder(classList);
		addFactoriesFor(classList, table);
		//cache the table and do the lookup again.
		if (lookup == null)
			lookup = new Hashtable<Class<?>, Hashtable<Class<?>,IAdapterFactory>>(5);
		lookup.put(extensibleClass, table);
		return (IAdapterFactory) table.get(adapter);
	}
	
	private void internalComputeInterfaceOrder(Class<?>[] interfaces, Vector<Class<?>> result, Hashtable<Class<?>,Class<?>> seen) {
		Vector<Class<?>> newInterfaces = new Vector<Class<?>>(seen.size());
		for (int i = 0; i < interfaces.length; i++) {
			Class<?> interfac = interfaces[i];
			if (seen.get(interfac) == null) {
				result.addElement(interfac);
				seen.put(interfac, interfac);
				newInterfaces.addElement(interfac);
			}
		}
		for (Enumeration<Class<?>> newList = newInterfaces.elements(); newList.hasMoreElements();)
			internalComputeInterfaceOrder((newList.nextElement()).getInterfaces(), result, seen);
	}
	/*
	 * @see IAdapterManager#registerAdapters
	 */
	public void registerAdapters(Class<?> extensibleType,IAdapterFactory factory) {
		Vector<IAdapterFactory> list = factories.get(extensibleType);
		if (list == null) {
			list = new Vector<IAdapterFactory>(5);
			factories.put(extensibleType, list);
		}
		list.addElement(factory);
		flushLookup();
	}
	/*
	 * @see IAdapterManager#unregisterAdapters
	 */
	public void unregisterAdapters(IAdapterFactory factory) {
		for (Enumeration<Vector<IAdapterFactory>> enu = factories.elements(); enu.hasMoreElements();) {
			Vector<IAdapterFactory> list =  enu.nextElement();
			list.removeElement(factory);
		}
		flushLookup();
	}
	/*
	 * @see IAdapterManager#unregisterAdapters
	 */
	public void unregisterAdapters(Class<?> extensibleType,IAdapterFactory factory ) {
		Vector<IAdapterFactory> factoryList = factories.get(extensibleType);
		if (factoryList == null)
			return;
		factoryList.removeElement(factory);
		flushLookup();
	}
	/*
	 * @see IAdapterManager#unregisterAllAdapters
	 */
	public void unregisterAllAdapters() {
		factories = new Hashtable<Class<?>, Vector<IAdapterFactory>>(5);
		flushLookup();
	}
}