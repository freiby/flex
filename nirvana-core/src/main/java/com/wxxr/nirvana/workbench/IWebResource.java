/*
 * @(#)IWebResource.java	 2007-11-2
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.workbench;


/**
 * @author fudapeng
 *
 */
public interface IWebResource extends IContributionItem {
	String getType();
	String getUri();
	void destroy();
}
