/*
 * @(#)IServiceRegistryEventListener.java	 2007-11-5
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.platform;

/**
 * @author neillin
 *
 */
public interface IServiceRegistryEventListener {
	void serviceRegsitered(Class api, Object service);
	void serviceUnregsitered(Class api, Object service);
}
