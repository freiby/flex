package com.wxxr.nirvana.platform;

public interface IPluginInitializer {
	void init(IPluginDescriptor plugin, IPlatform platform)
			throws CoreException;

	void destroy(IPluginDescriptor plugin);
}
