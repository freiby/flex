package com.wxxr.nirvana.platform;

public interface IExtensionObjectFactory {
  IPluginDescriptor createPlugin(IExtensionRegistry registry,Object source);
  IExtension createExtension(IExtensionRegistry registry,Object source);
  IExtensionPoint createExtensionPoint(IExtensionRegistry registry,Object source);
  Object createExtensionObject(IExtensionRegistry registry,Object source);
  boolean removeExtensionFrom(IExtensionPoint p, IExtension ext);
  void addExtensionTo(IExtensionPoint p, IExtension ext);
  IContributor getContributor(String contributorId);
  Object createExecutableExtension(IContributor contribute, String className, String contributorName);
}
