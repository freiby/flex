/*
 * @(#)PlatformLocator.java	 2007-10-24
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.platform;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wxxr.nirvana.platform.impl.Platform;

/**
 * @author fudapeng
 *
 */
public class PlatformLocator {
	private static IPlatform platform;

	private static Log log = LogFactory.getLog(PlatformLocator.class);

	private PlatformLocator() {
	}

	/**
	 * @return the platform
	 */
	public static IPlatform getPlatform() {
		if (platform == null) {
			try {
				platform = new Platform();
			} catch (Exception e) {
				log.error("create platform error ", e);
			}
		}
		return platform;
	}

}
