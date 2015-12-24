package com.wxxr.nirvana.workbench;

public interface IPermissionsManager {
	IPermissionDescriptor getPermission(String namespace, String targetId);

	void addPermission(IPermissionDescriptor permission);

	IPermissionDescriptor removePermission(String namespace, String targetId);

	IPermissionDescriptor[] getAllPermissions();

	String[] getAllNamespaces();

	String[] getAllPermissionIds(String namesapce);

	IPermissionDescriptor getDefaultPermission();

	void destroy();

	void start();
}
