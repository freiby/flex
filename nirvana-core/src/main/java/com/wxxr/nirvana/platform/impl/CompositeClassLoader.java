/*
 * @(#)CompositeClassLoader.java	 2007-11-14
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.platform.impl;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * @author fudapeng
 *
 */
public class CompositeClassLoader extends ClassLoader {

	private ClassLoader webClassLoder;

	/**
	 * @param deferTo
	 */
	public CompositeClassLoader(java.lang.ClassLoader parent) {
		super(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.ClassLoader#findClass(java.lang.String)
	 */
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		if (webClassLoder != null) {
			return webClassLoder.loadClass(name);
		} else {
			throw new ClassNotFoundException(name);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.ClassLoader#findResource(java.lang.String)
	 */
	@Override
	protected URL findResource(String name) {
		if (webClassLoder != null) {
			return webClassLoder.getResource(name);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.ClassLoader#findResources(java.lang.String)
	 */
	@Override
	protected Enumeration<URL> findResources(String name) throws IOException {
		if (webClassLoder != null) {
			return webClassLoder.getResources(name);
		}
		return null;
	}

	/**
	 * @return the webClassLoder
	 */
	public ClassLoader getWebClassLoder() {
		return webClassLoder;
	}

	/**
	 * @param webClassLoder
	 *            the webClassLoder to set
	 */
	public void setWebClassLoder(ClassLoader webClassLoder) {
		this.webClassLoder = webClassLoder;
	}

}
