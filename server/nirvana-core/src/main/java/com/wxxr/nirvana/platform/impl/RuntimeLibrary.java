/*
 * @(#)RuntimeLibrary.java	 2007-10-25
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.platform.impl;

import java.io.File;

import com.wxxr.nirvana.platform.IRuntimeLibrary;

/**
 * @author fudapeng
 *
 */
public class RuntimeLibrary implements IRuntimeLibrary {
	private String exports;
	private String pluginId;
	private File jarDir;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wxxr.web.platform.core.IRuntimeLibrary#getExports()
	 */
	public String getExports() {
		return exports;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wxxr.web.platform.core.IRuntimeLibrary#getJarFiles()
	 */
	// public URL[] getJarFiles() {
	// return jarFiles;
	// }

	/**
	 * @param exports
	 *            the exports to set
	 */
	public void setExports(String exports) {
		this.exports = exports;
	}

	/**
	 * @return the pluginId
	 */
	public String getPluginId() {
		return pluginId;
	}

	/**
	 * @param pluginId
	 *            the pluginId to set
	 */
	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}

	public File getJarDir() {
		return jarDir;
	}

	public void setJarDir(File jarDir) {
		this.jarDir = jarDir;
	}

}
