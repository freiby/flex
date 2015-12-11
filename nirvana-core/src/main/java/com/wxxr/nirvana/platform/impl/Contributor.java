/*
 * @(#)Contributor.java	 2007-10-23
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.platform.impl;

import com.wxxr.nirvana.platform.IContributor;

/**
 * @author fudapeng
 *
 */
public class Contributor implements IContributor {

	private String name;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wxxr.web.platform.core.IContributor#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
