/*
 * @(#)PluginClassLoader.java	 2007-10-26
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
import java.util.Iterator;
import java.util.LinkedList;

import com.wxxr.nirvana.platform.CoreException;
import com.wxxr.nirvana.platform.IRuntimeLibrary;

/**
 * @author neillin
 *
 */
public class PluginClassLoader extends ClassLoader {

	private IRuntimeLibrary library;
	private String[] exports;
//	private UnifiedLoaderRepository3 repository;
//	private LinkedList<RepositoryClassLoader> loaders = new LinkedList<RepositoryClassLoader>();
	private ClassLoader delegate;
	/**
	 * @param urls
	 */
	public PluginClassLoader(IRuntimeLibrary library,ClassLoader parent) {
		super(parent);
		this.library = library;
//		this.repository = repository;
		URL[] jars = library.getJarFiles();
		if((jars == null)||(jars.length == 0)){
			throw new IllegalArgumentException("Invalid library !!!");
		}
		for (int i = 0; i < jars.length; i++) {
			URL url = jars[i];
			if(url != null){
				try {
//					UnifiedClassLoader3 loader = new UnifiedClassLoader3(url,url,parent,repository);
//					repository.registerClassLoader(loader);
//					loaders.add(loader);
				} catch (Exception e) {
					throw new CoreException("Failed to created plugin class loader for plugin :"+library.getPluginId());
				}
			}
		}
//		delegate = loaders.iterator().next();
	}
	/* (non-Javadoc)
	 * @see java.lang.ClassLoader#findClass(java.lang.String)
	 */
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {

		return delegate.loadClass(name);
	}
	/* (non-Javadoc)
	 * @see java.lang.ClassLoader#findResource(java.lang.String)
	 */
	@Override
	protected URL findResource(String name) {
		return delegate.getResource(name);
	}
	/* (non-Javadoc)
	 * @see java.lang.ClassLoader#findResources(java.lang.String)
	 */
	@Override
	protected Enumeration<URL> findResources(String name) throws IOException {
		return delegate.getResources(name);
	}

	public void destroy(){
//		for (Iterator iterator = loaders.iterator(); iterator.hasNext();) {
//			RepositoryClassLoader loader = (RepositoryClassLoader) iterator.next();
//			if(loader != null){
//				loader.unregister();
//			}		
//		}
	}

}
