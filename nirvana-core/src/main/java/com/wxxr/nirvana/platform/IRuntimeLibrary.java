/*
 * @(#)IRuntimeLibrary.java	 2007-10-25
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.platform;

import java.io.File;

/**
 * @author fudapeng
 *
 */
public interface IRuntimeLibrary {
	String getPluginId();

	// URL[] getJarFiles();
	File getJarDir();

	String getExports();
}
