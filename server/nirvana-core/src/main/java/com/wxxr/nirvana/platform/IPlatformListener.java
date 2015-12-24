package com.wxxr.nirvana.platform;

public interface IPlatformListener {
	void onActivePlugin(PlatformEvent event) throws CoreException;

	void onDeactivePlugin(PlatformEvent event) throws CoreException;;
}
