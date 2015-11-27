package com.wxxr.nirvana.platform.impl;
	/**
 * wangping 2007-11-23
 */
public interface IPackageUploaderClient {
	public boolean upload(String urlPath,String urlFileName,String uid,String pwd,String localPath,String localFileName);

}
