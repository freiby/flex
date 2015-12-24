/*
 * @(#)CoreException.java	 2007-10-15
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.platform;

/**
 * @author fudapeng
 *
 */
public class CoreException extends RuntimeException {

	/**
	 * 
	 */
	public CoreException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public CoreException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public CoreException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public CoreException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
