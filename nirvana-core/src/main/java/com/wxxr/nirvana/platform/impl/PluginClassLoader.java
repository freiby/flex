/*
 * @(#)PluginClassLoader.java	 2007-10-26
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.platform.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;

import com.wxxr.nirvana.platform.IRuntimeLibrary;

/**
 * @author fudapeng
 *
 */
public class PluginClassLoader extends ClassLoader {
	private IRuntimeLibrary library;
	private URLClassLoader delegate;
	private ClassLoader parent;
	private boolean init;

	/**
	 * @param urls
	 */
	public PluginClassLoader(IRuntimeLibrary library, ClassLoader parent) {
		super(parent);
		this.library = library;
		this.parent = parent;
	}

	private void init(IRuntimeLibrary library, ClassLoader parent) {
		URL[] jars = null;
		try {
			jars = getLibraries(library.getJarDir());
		} catch (MalformedURLException e) {
			throw new RuntimeException("Invalid library !!!");
		}
		if ((jars == null) || (jars.length == 0)) {
			init = true;
			return;
		}
		delegate = new URLClassLoader(jars, parent);
		init = true;
	}

	/**
	 * 查看安装的目录lib下所有的jar包都加载
	 * 
	 * @return
	 * @throws MalformedURLException
	 */
	private URL[] getLibraries(File jarDir) throws MalformedURLException {
		URL[] libs = null;
		File libDir = jarDir;
		if (libDir.exists() && libDir.isDirectory()) {
			File[] jars = libDir.listFiles(new FileFilter() {
				public boolean accept(File pathname) {
					return pathname.getName().endsWith("jar");
				}
			});
			if (jars != null && jars.length > 0) {
				libs = new URL[jars.length];
				int i = 0;
				for (File fitem : jars) {
					libs[i] = fitem.toURI().toURL();
					i++;
				}
			}
			return libs;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.ClassLoader#findClass(java.lang.String)
	 */
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		if (!init) {
			init(library, parent);
		}
		return delegate == null ? parent.loadClass(name) : delegate
				.loadClass(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.ClassLoader#findResource(java.lang.String)
	 */
	@Override
	protected URL findResource(String name) {
		if (!init) {
			init(library, parent);
		}
		return delegate == null ? parent.getResource(name) : delegate
				.getResource(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.ClassLoader#findResources(java.lang.String)
	 */
	@Override
	protected Enumeration<URL> findResources(String name) throws IOException {
		if (!init) {
			init(library, parent);
		}
		return delegate == null ? parent.getResources(name) : delegate
				.getResources(name);// delegate.getResources(name);
	}

	public void destroy() {
		delegate = null;
	}

}
