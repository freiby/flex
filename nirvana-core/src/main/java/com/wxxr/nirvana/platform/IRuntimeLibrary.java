/*
 * @(#)IRuntimeLibrary.java	 2007-10-25
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.platform;

import java.net.URL;

/**
 * @author neillin
 *
 */
public interface IRuntimeLibrary {
  String getPluginId();
  URL[] getJarFiles();
  String getExports();
}
