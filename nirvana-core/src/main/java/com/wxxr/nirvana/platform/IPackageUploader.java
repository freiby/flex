/*
 * @(#)IPackageUploader.java	 2007-10-26
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
public interface IPackageUploader {
	String getUploaderURL();

	String[] getAllUploadedPackages();

	URL getPackageURL(String packageName);

	// add by wangping begin
	public String getRemoteRoot();

	public void setRemoteRoot(String remoteRoot);

	public String getUserName();

	public void setUserName(String userName);

	public void setPwd(String pwd);

	public String getPwd();
	// add by wangping end
}
