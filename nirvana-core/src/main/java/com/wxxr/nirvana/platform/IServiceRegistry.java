/*
 * @(#)IServiceRegistry.java	 2007-11-5
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.platform;

/**
 * @author fudapeng
 *
 */
public interface IServiceRegistry {

	void registerService(Class api, Object service);
	boolean unregisterService(Class api, Object service);
	Object getService(final Class key);
	boolean hasService(final Class key);
	void addEventListener(IServiceRegistryEventListener l);
	boolean removeEventListener(IServiceRegistryEventListener l);
}
