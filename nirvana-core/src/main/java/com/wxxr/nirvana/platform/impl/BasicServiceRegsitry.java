/*
 * @(#)BasicServiceRegsitry.java	 2007-11-5
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.platform.impl;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.wxxr.nirvana.platform.IServiceRegistry;
import com.wxxr.nirvana.platform.IServiceRegistryEventListener;

/**
 * @author fudapeng
 *
 */
public class BasicServiceRegsitry implements IServiceRegistry {
	protected LinkedList<IServiceRegistryEventListener> listeners = new LinkedList<IServiceRegistryEventListener>();
	protected Map<Class, Object> registry = new ConcurrentHashMap<Class, Object>();

	/* (non-Javadoc)
	 * @see com.wxxr.web.platform.core.IServiceRegistry#addEventListener(com.wxxr.web.platform.core.IServiceRegistryEventListener)
	 */
	public void addEventListener(IServiceRegistryEventListener l) {
		if(l == null){
			throw new IllegalArgumentException();
		}
		synchronized(listeners){
			if(!listeners.contains(l)){
				listeners.add(l);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.web.platform.core.IServiceRegistry#getService(java.lang.Class)
	 */
	public Object getService(Class key) {
		return registry.get(key);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.web.platform.core.IServiceRegistry#hasService(java.lang.Class)
	 */
	public boolean hasService(Class key) {
		return registry.containsKey(key);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.web.platform.core.IServiceRegistry#registerService(java.lang.Class, java.lang.Object)
	 */
	public void registerService(Class api, Object service) {
		if((api == null)||(service == null)){
			throw new IllegalArgumentException();
		}
		registry.put(api,service);
		fireRegisteredEvent(api,service);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.web.platform.core.IServiceRegistry#removeEventListener(com.wxxr.web.platform.core.IServiceRegistryEventListener)
	 */
	public boolean removeEventListener(IServiceRegistryEventListener l) {
		return listeners.remove(l);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.web.platform.core.IServiceRegistry#unregisterService(java.lang.Class, java.lang.Object)
	 */
	public boolean unregisterService(Class api, Object service) {
		Object obj = registry.get(api);
		if(obj == service){
			registry.remove(api);
			fireUnregisteredEvent(api,service);
			return true;
		}
		return false;
	}
	
	protected void fireRegisteredEvent(Class api, Object service){
		if(listeners.isEmpty()){
			return;
		}
		IServiceRegistryEventListener[] all = listeners.toArray(new IServiceRegistryEventListener[listeners.size()]);
		for (int i = 0; i < all.length; i++) {
			IServiceRegistryEventListener l = all[i];
			if(l != null){
				l.serviceRegsitered(api, service);
			}
		}
	}

	protected void fireUnregisteredEvent(Class api, Object service){
		if(listeners.isEmpty()){
			return;
		}
		IServiceRegistryEventListener[] all = listeners.toArray(new IServiceRegistryEventListener[listeners.size()]);
		for (int i = 0; i < all.length; i++) {
			IServiceRegistryEventListener l = all[i];
			if(l != null){
				l.serviceUnregsitered(api, service);
			}
		}
	}

}
