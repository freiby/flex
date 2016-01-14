/*
 * @(#)Platform.java	 2007-10-24
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.platform.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wxxr.nirvana.platform.CoreException;
import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.platform.IContributor;
import com.wxxr.nirvana.platform.IExtension;
import com.wxxr.nirvana.platform.IExtensionObjectFactory;
import com.wxxr.nirvana.platform.IExtensionPoint;
import com.wxxr.nirvana.platform.IExtensionRegistry;
import com.wxxr.nirvana.platform.INameStrategy;
import com.wxxr.nirvana.platform.IPlatform;
import com.wxxr.nirvana.platform.IPlatformListener;
import com.wxxr.nirvana.platform.IPluginDescriptor;
import com.wxxr.nirvana.platform.IRuntimeLibrary;
import com.wxxr.nirvana.platform.IServiceRegistry;
import com.wxxr.nirvana.platform.PlatformEvent;
import com.wxxr.nirvana.platform.PluginVersionIdentifier;
import com.wxxr.nirvana.util.JarUtils;
import com.wxxr.nirvana.util.StringPropertyReplacer;

/**
 * @author fudapeng
 *
 */
public class Platform implements IPlatform {
	private static final Log log = LogFactory.getLog(Platform.class);
	private static final String DEFAULT_PLUGIN_ROOT = "${data.dir}/webplugins";
	private static final IPluginDescriptor[] NO_PLUGINS = new IPluginDescriptor[0];
	private List<IPlatformListener> listeners = new ArrayList<IPlatformListener>();
	private char[] c = { 'c' };

	private static class PluginVersion {
		String pluginId;
		String version;
		Set<String> depend;
	}

	private static Platform currentInstance;
	private BaseExtensionRegistry registry = new BaseExtensionRegistry(this);
	// private WebClassLoaderAdaptor webClAdaptor;
	private String pluginRootDir;
	private PluginXMLParser parser = new PluginXMLParser();
	// private UnifiedLoaderRepository3 loaderRepository = new
	// UnifiedLoaderRepository3();
	private IServiceRegistry serviceRegistry = new BasicServiceRegsitry();
	private ClassLoader platformClassLoader, parentClassLoader;
	private Map<String, PluginClassLoader> pluginClassLoaders = new ConcurrentHashMap<String, PluginClassLoader>();
	// deployedPluginVersions<pluginID,map<PluginVersionIdentifier,element> >
	private Map<String, Map<PluginVersionIdentifier, PluginConfigurationElement>> deployedPluginVersions = new ConcurrentHashMap<String, Map<PluginVersionIdentifier, PluginConfigurationElement>>();
	// map<pluginId,pluginversion>
	private Map<String, String> currentActivedPluginVersion = new ConcurrentHashMap<String, String>();
	private INameStrategy nameStrategy = new INameStrategy() {

		public String getUniqueIdentifier(String namespace, String name) {
			if (StringUtils.isBlank(namespace) || StringUtils.isBlank(name)) {
				throw new IllegalArgumentException();
			}
			return new StringBuffer(namespace).append('.').append(name)
					.toString();
		}

		public String getNamespaceFromIdentifier(String identifier) {
			if (StringUtils.isBlank(identifier)) {
				throw new IllegalArgumentException();
			}
			int idx = identifier.lastIndexOf('.');
			if (idx == -1) {
				throw new IllegalArgumentException("Invalid identifier :"
						+ identifier);
			}
			return identifier.substring(0, idx);
		}

		public String getNameFromIdentifier(String identifier) {
			if (StringUtils.isBlank(identifier)) {
				throw new IllegalArgumentException();
			}
			int idx = identifier.lastIndexOf('.');
			if (idx == -1) {
				throw new IllegalArgumentException("Invalid identifier :"
						+ identifier);
			}
			return identifier.substring(idx + 1);
		}

	};
	private IExtensionObjectFactory objectFactory = new IExtensionObjectFactory() {

		// IExtensionObjectFactory implements
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.wxxr.web.platform.core.IExtensionObjectFactory#addExtensionTo
		 * (com.wxxr.web.platform.core.IExtensionPoint,
		 * com.wxxr.web.platform.core.IExtension)
		 */
		public void addExtensionTo(IExtensionPoint p, IExtension ext) {
			if ((p instanceof ExtensionPoint) && (ext instanceof Extension)) {
				ExtensionPoint point = (ExtensionPoint) p;
				Extension extension = (Extension) ext;
				point.addExtensions(ext);
			} else {
				throw new IllegalArgumentException();
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.wxxr.web.platform.core.IExtensionObjectFactory#
		 * createExecutableExtension(com.wxxr.web.platform.core.IContributor,
		 * java.lang.String, java.lang.String)
		 */
		public Object createExecutableExtension(IContributor contribute,
				String className, String contributorName) {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.wxxr.web.platform.core.IExtensionObjectFactory#createExtension
		 * (com.wxxr.web.platform.core.IExtensionRegistry, java.lang.Object)
		 */
		public IExtension createExtension(IExtensionRegistry registry,
				Object source) {
			if (source instanceof ExtensionConfigurationElement) {
				Extension ext = new Extension(
						(ExtensionConfigurationElement) source);
				return ext;
			} else {
				throw new IllegalArgumentException(
						"source is not instanceof ExtensionConfigurationElement");
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.wxxr.web.platform.core.IExtensionObjectFactory#createExtensionObject
		 * (com.wxxr.web.platform.core.IExtensionRegistry, java.lang.Object)
		 */
		public Object createExtensionObject(IExtensionRegistry registry,
				Object source) {
			if (source instanceof PluginConfigurationElement) {
				return createPlugin(registry, source);
			}
			if (source instanceof ExtensionPointConfigurationElement) {
				return createExtensionPoint(registry, source);
			}
			if (source instanceof ExtensionConfigurationElement) {
				return createExtension(registry, source);
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.wxxr.web.platform.core.IExtensionObjectFactory#createExtensionPoint
		 * (com.wxxr.web.platform.core.IExtensionRegistry, java.lang.Object)
		 */
		public IExtensionPoint createExtensionPoint(
				IExtensionRegistry registry, Object source) {
			if (source instanceof ExtensionPointConfigurationElement) {
				ExtensionPoint ext = new ExtensionPoint(
						(ExtensionPointConfigurationElement) source);
				return ext;
			} else {
				throw new IllegalArgumentException(
						"source is not instanceof ExtensionPointConfigurationElement");
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.wxxr.web.platform.core.IExtensionObjectFactory#createPlugin(com
		 * .wxxr.web.platform.core.IExtensionRegistry, java.lang.Object)
		 */
		public IPluginDescriptor createPlugin(IExtensionRegistry registry,
				Object source) {
			if (source instanceof PluginConfigurationElement) {
				Plugin plugin = new Plugin((PluginConfigurationElement) source,
						registry);
				return plugin;
			} else {
				throw new IllegalArgumentException(
						"Source is not instanceof PluginConfigurationElement!");
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.wxxr.web.platform.core.IExtensionObjectFactory#getContributor
		 * (java.lang.String)
		 */
		public IContributor getContributor(String contributorId) {
			if (StringUtils.isBlank(contributorId)) {
				throw new IllegalArgumentException();
			}
			IContributor contrib = contributors.get(contributorId);
			if (contrib == null) {
				contrib = new Contributor();
				((Contributor) contrib).setName(contributorId);
				contributors.put(contributorId, contrib);
			}
			return contrib;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.wxxr.web.platform.core.IExtensionObjectFactory#removeExtensionFrom
		 * (com.wxxr.web.platform.core.IExtensionPoint,
		 * com.wxxr.web.platform.core.IExtension)
		 */
		public boolean removeExtensionFrom(IExtensionPoint p, IExtension ext) {
			if ((p instanceof ExtensionPoint) && (ext instanceof Extension)) {
				ExtensionPoint point = (ExtensionPoint) p;
				return point.removeExtension(ext);
			} else {
				throw new IllegalArgumentException();
			}
		}
	};

	private Map<String, Plugin> plugins = new ConcurrentHashMap<String, Plugin>();// actived
																					// plugins
	private Map<String, IContributor> contributors = new ConcurrentHashMap<String, IContributor>();
	private Timer timer;
	private TimerTask scanTask = new TimerTask() {

		@Override
		public void run() {
			try {
				scanPluginRoot();
			} catch (Exception e) {
				log.warn("Caught exception when scan plugin root :"
						+ pluginRootDir, e);
			}
		}

	};

	/**
	 * @throws Exception
	 * 
	 */
	public Platform() throws Exception {
		platformClassLoader = Thread.currentThread().getContextClassLoader();
		parentClassLoader = platformClassLoader;
		// loaderRepository.registerClassLoader((UnifiedClassLoader3)platformClassLoader);
		if (pluginRootDir == null) {
			pluginRootDir = StringPropertyReplacer
					.replaceProperties(DEFAULT_PLUGIN_ROOT);
		}
		currentInstance = this;
	}

	public static Platform getCurrentInstance() {
		return currentInstance;
	}

	/**
	 * @return the nameStrategy
	 */
	public INameStrategy getNameStrategy() {
		return nameStrategy;
	}

	/**
	 * @param nameStrategy
	 *            the nameStrategy to set
	 */
	public void setNameStrategy(INameStrategy nameStrategy) {
		this.nameStrategy = nameStrategy;
	}

	/**
	 * @return the objectFactory
	 */
	public IExtensionObjectFactory getObjectFactory() {
		return objectFactory;
	}

	/**
	 * @param objectFactory
	 *            the objectFactory to set
	 */
	public void setObjectFactory(IExtensionObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wxxr.web.platform.core.ICoreContext#getContributor(java.lang.String)
	 */
	public IContributor getContributor(String id) {
		return objectFactory.getContributor(id);
	}

	public void addContributor(IContributor contributor) {
		if (contributor == null) {
			throw new IllegalArgumentException();
		}
		contributors.put(contributor.getName(), contributor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wxxr.web.platform.core.IPlatform#getExtensionRegistry()
	 */
	public IExtensionRegistry getExtensionRegistry() {
		return registry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wxxr.web.platform.core.IPlatform#getPluginDescriptor(java.lang.String
	 * )
	 */
	public IPluginDescriptor getPluginDescriptor(String pluginId) {
		return plugins.get(pluginId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wxxr.web.platform.core.IPlatform#getPluginDescriptors()
	 */
	public IPluginDescriptor[] getPluginDescriptors() {
		if (plugins.isEmpty()) {
			return NO_PLUGINS;
		}
		return plugins.values().toArray(new IPluginDescriptor[plugins.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wxxr.web.platform.core.IPlatform#deployPlugin(java.lang.String)
	 */
	public void deployPlugin(String uploadedJarFileName) throws CoreException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wxxr.web.platform.core.IPlatform#deployPluginByXml(java.lang.String)
	 */
	// public void deployPluginByXml(String pluginXmlString) throws
	// CoreException{
	// try {
	// Document document = DocumentHelper.parseText(pluginXmlString);
	//
	// OutputFormat format = OutputFormat.createPrettyPrint();
	// format.setEncoding("GB2312");
	// List list = document.selectNodes("//plugin");
	// Iterator iter=list.iterator();
	// String version = null;
	// String pluginId = null;
	// while(iter.hasNext()){
	// Element element =(Element)iter.next();
	// version =element.attribute(Plugin.PLUGIN_VERSION).getValue();
	// pluginId =element.attribute(CoreConstants.PLUGIN_ID).getValue();
	// }
	// if(StringUtils.isBlank(pluginId) || StringUtils.isBlank(version)){
	// throw new
	// Exception("pluginXmlString is not contain plugin id  or version info.");
	// }
	// File dir = new File(pluginRootDir + "\\" + pluginId + "\\" +version);
	// dir.mkdirs();
	// File f = new File(pluginRootDir + "\\" + pluginId + "\\" +version +
	// "\\webplugin.xml");
	// if(f.createNewFile()){
	// XMLWriter writer = new XMLWriter(new FileWriter(f),format);//ƴ��
	// writer.write(document);
	// writer.close();
	// }else{
	// throw new Exception(pluginId + "/" +version + " is exists .");
	// }
	//
	// } catch (Exception e) {
	// log.error("Caught Exception when deploy plugin: " + pluginXmlString,e);
	// throw new CoreException("Failed to deploy plugin : " +
	// pluginXmlString,e);
	// }
	//
	// }

	/**
	 * @return the pluginRootDir
	 */
	public String getPluginRootDir() {
		return pluginRootDir;
	}

	/**
	 * @param pluginRootDir
	 *            the pluginRootDir to set
	 */
	public void setPluginRootDir(String pluginRootDir) {
		this.pluginRootDir = pluginRootDir;
	}

	/*
	 * (non-Javadoc) deployPlugin step: 1 get the plugin's
	 * PulginConfigureElement 2 mkdir "pluginRootDir/pulginId/pulginVersion" 3
	 * unjar to the new dir 4 pluginConfig.setPluginxmlURL(unjar file dir) 5
	 * regist to Platform.versions 6 set the plugin's PulginConfigureElement is
	 * deployed
	 * 
	 * @see com.wxxr.web.platform.core.IPlatform#deployPlugin(java.net.URL)
	 */
	public void deployPlugin(URL packageURL) throws CoreException {
		if (packageURL == null) {
			throw new IllegalArgumentException();
		}
		try {
			URLConnection conn = packageURL.openConnection();
			// if(conn instanceof JarURLConnection){
			PluginConfigurationElement pluginConfig = null;
			JarInputStream jin = new JarInputStream(conn.getInputStream());
			try {
				for (JarEntry entry = jin.getNextJarEntry(); (pluginConfig == null)
						&& (entry != null); entry = jin.getNextJarEntry()) {
					String name = entry.getName();
					if (log.isDebugEnabled()) {
						log.debug("Looking at entry: '" + name + "'");
					}
					if (name.equalsIgnoreCase(CoreConstants.PLUGIN_XML)) {
						if (log.isInfoEnabled()) {
							log.info("Found plugin xml!");
						}
						pluginConfig = parser.parse(jin);
						break;
					}
					jin.closeEntry();
				}
			} finally {
				try {
					jin.close();
				} catch (Exception e) {
				}
			}
			if (pluginConfig != null) {
				File deployDir = new File(pluginRootDir + "/"
						+ pluginConfig.getNamespaceIdentifier(),
						pluginConfig.getAttribute(Plugin.PLUGIN_VERSION));
				if (log.isInfoEnabled()) {
					log.info("Unzip package :" + packageURL
							+ " to deploy directory "
							+ deployDir.getAbsolutePath());
				}
				deployDir.mkdirs();
				InputStream ins = packageURL.openStream();
				try {
					JarUtils.unjar(ins, deployDir);
				} finally {
					try {
						ins.close();
					} catch (Exception e) {
					}
				}
				if (log.isInfoEnabled()) {
					log.info("Package :" + packageURL
							+ " was unzipped to directory "
							+ deployDir.getAbsolutePath());
				}
				pluginConfig.setPluginxmlURL(new File(deployDir,
						CoreConstants.PLUGIN_XML).toURL());
				Map<PluginVersionIdentifier, PluginConfigurationElement> versions = deployedPluginVersions
						.get(pluginConfig.getNamespaceIdentifier());
				if (versions == null) {
					versions = new HashMap<PluginVersionIdentifier, PluginConfigurationElement>();
					deployedPluginVersions.put(
							pluginConfig.getNamespaceIdentifier(), versions);
				}
				versions.put(pluginConfig.getPluginVersion(), pluginConfig);
			}
			// }
		} catch (Exception e) {
			log.error("Caught Exception when deploy plugin:" + packageURL, e);
			throw new CoreException("Failed to deploy plugin :" + packageURL, e);
		}
	}

	protected Plugin activate(PluginConfigurationElement pluginConfig)
			throws Exception {
		if (pluginConfig == null) {
			throw new IllegalArgumentException();
		}
		String deployURL = new File(pluginConfig.getPluginxmlURL().getFile())
				.getParentFile().toURL().toExternalForm();
		if (log.isInfoEnabled()) {
			log.info("Deploying plugin "
					+ pluginConfig.getNamespaceIdentifier());
		}
		Plugin plugin = (Plugin) getObjectFactory().createPlugin(registry,
				pluginConfig);
		plugin.setInstallURL(deployURL);
		plugin.setMessageResourceBundle(new PluginMessageResourceBundle(
				deployURL));
		plugin.setPluginClassLoader(createPluginClassLoader(plugin
				.getRuntimeLibrary()));
		plugin.setVersionTimeStamp(Long.toString(System.currentTimeMillis()));
		plugins.put(plugin.getUniqueIdentifier(), plugin);
		IConfigurationElement[] elems = pluginConfig.getChildren();
		resolvePluginElemsOrder(elems);
		for (int i = 0; (elems != null) && (i < elems.length); i++) {
			if (log.isDebugEnabled()) {
				log.debug("Deploy element:" + elems[i].getName());
				log.debug("Deploy element id:"
						+ elems[i].getAttribute(CoreConstants.EXTENSION_ID));
			}
			registry.addContribution(elems[i], pluginConfig.getContributor(),
					elems[i].getName(), plugin.getMessageResourceBundle());
		}

		if (log.isInfoEnabled()) {
			log.info("Plugin: " + pluginConfig.getNamespaceIdentifier()
					+ " was deployed !");
		}
		return plugin;
	}

	private void resolvePluginElemsOrder(IConfigurationElement[] elems) {
		return;
		// if(elems.length>0){
		// if(elems[0].getName().equalsIgnoreCase(CoreConstants.EXTENSION_POINT)){
		// return;
		// }
		// }
		// Map<String, IConfigurationElement> map = new HashMap<String,
		// IConfigurationElement>();
		// for (int i = 0; (elems != null) && (i < elems.length); i++) {
		// String extentionPointString =
		// elems[i].getAttribute(CoreConstants.EXTENSION_TARGET);
		// if(StringUtils.isBlank(extentionPointString)){
		// String extensionId =
		// elems[i].getAttribute(CoreConstants.EXTENSION_ID);
		// if(StringUtils.isBlank(extensionId)){
		// map.put(elems[i].getName(), elems[i]);
		// }else{
		// map.put(extensionId, elems[i]);
		// }
		// }else{
		// if (extentionPointString.equalsIgnoreCase("com.wxxr.web.ui.actions"))
		// {
		// map.put("1", elems[i]);
		// } else if
		// (extentionPointString.equalsIgnoreCase("com.wxxr.web.ui.menus")) {
		// map.put("2", elems[i]);
		// } else if
		// (extentionPointString.equalsIgnoreCase("com.wxxr.web.ui.views")) {
		// map.put("3", elems[i]);
		// } else if
		// (extentionPointString.equalsIgnoreCase("com.wxxr.web.ui.pages")) {
		// map.put("4", elems[i]);
		// } else {
		// String extensionId =
		// elems[i].getAttribute(CoreConstants.EXTENSION_ID);
		// if(StringUtils.isBlank(extensionId)){
		// map.put(elems[i].getName(), elems[i]);
		// }else{
		// map.put(extensionId, elems[i]);
		// }
		// }
		// }
		// }
		// String[] ss = map.keySet().toArray(new String[0]);
		// Arrays.sort(ss);
		// for (int i = 0; i < ss.length; i++) {
		// String string = ss[i];
		// elems[i] = map.get(string);
		// }
	}

	protected void deactivate(Plugin plugin) {
		if (plugin == null) {
			throw new IllegalArgumentException();
		}
		if (plugin.isPluginInitialized()
				&& (plugin.getPluginInitializer() != null)) {
			try {
				plugin.getPluginInitializer().destroy(plugin);
			} catch (Exception e) {
				log.warn(
						"Failed to deactivate plugin :"
								+ plugin.getUniqueIdentifier(), e);
			}
		}
		IExtension[] extensions = plugin.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			registry.removeExtension(extensions[i]);
		}
		IExtensionPoint[] points = plugin.getExtensionPoints();
		for (int i = 0; i < points.length; i++) {
			registry.removeExtensionPoint(points[i]);
		}
		plugins.remove(plugin.getUniqueIdentifier());
		PluginClassLoader cl = pluginClassLoaders.remove(plugin
				.getUniqueIdentifier());
		if (cl != null) {
			cl.destroy();
		}

		// dispatch event
		// IEvent event = new
		// PluginRegistryChangeEvent(plugin,PluginRegistryChangeEvent.REMOVED);
		// EventManager.getInstance().notifyEvent(event);

		PlatformEvent event = new PlatformEvent(plugin.getUniqueIdentifier(),
				plugin.getVersionIdentifier().toString());
		fireDeactivePluginEvent(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wxxr.web.platform.core.IPlatform#getLatestDeployedPluginVersion(java
	 * .lang.String)
	 */
	public String getLatestDeployedPluginVersion(String pluginId) {
		PluginVersionIdentifier v = getLatestDeployedVersion(pluginId);
		return (v != null) ? v.toString() : null;
	}

	protected PluginVersionIdentifier getLatestDeployedVersion(String pluginId) {
		if (pluginId == null) {
			throw new IllegalArgumentException();
		}
		Map<PluginVersionIdentifier, PluginConfigurationElement> versions = deployedPluginVersions
				.get(pluginId);
		if ((versions == null) || versions.isEmpty()) {
			return null;
		}
		if (versions.size() == 1) {
			return versions.keySet().iterator().next();
		}
		TreeSet<PluginVersionIdentifier> set = new TreeSet<PluginVersionIdentifier>(
				versions.keySet());
		return set.last();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wxxr.web.platform.core.IPlatform#isPluginDeployed(java.lang.String,
	 * java.lang.String)
	 */
	public boolean isPluginDeployed(String pluginId, String version) {
		if (pluginId == null) {
			throw new IllegalArgumentException();
		}
		Map<PluginVersionIdentifier, PluginConfigurationElement> versions = deployedPluginVersions
				.get(pluginId);
		if ((versions == null) || versions.isEmpty()) {
			return false;
		}
		return versions.containsKey(new PluginVersionIdentifier(version));
	}

	protected PluginConfigurationElement getPluginVersion(String pluginId,
			String version) {
		return getPluginVersion(pluginId, new PluginVersionIdentifier(version));
	}

	protected PluginConfigurationElement getPluginVersion(String pluginId,
			PluginVersionIdentifier version) {
		if ((pluginId == null) || (version == null)) {
			throw new IllegalArgumentException();
		}
		Map<PluginVersionIdentifier, PluginConfigurationElement> versions = deployedPluginVersions
				.get(pluginId);
		if ((versions == null) || versions.isEmpty()) {
			return null;
		}
		return versions.get(version);
	}

	/*
	 * (non-Javadoc) deployPlugin step: 1 get the plugin's
	 * PulginConfigureElement 2 mkdir "pluginRootDir/pulginId/pulginVersion" 3
	 * unjar to the new dir 4 pluginConfig.setPluginxmlURL(unjar file dir) 5
	 * regist to Platform.versions 6 set the plugin's PulginConfigureElement is
	 * deployed
	 * 
	 * uddeployplugin step: 1 remove pulginid folder from plugRootDir 2 remove
	 * pulgin from deployedPluginVersions ,and change it to deActive state at
	 * the same time
	 * 
	 * @see
	 * com.wxxr.web.platform.core.IPlatform#undeployPlugin(java.lang.String,
	 * java.lang.String)
	 */
	public void undeployPlugin(String pluginId, String version)
			throws CoreException {
		synchronized (c) {
			if (!isPluginDeployed(pluginId, version)) {
				throw new CoreException("Plugin :" + pluginId + " of version :"
						+ version + " is not deployed !");
			}
			if (version == null) {
				try {
					// 1 remove pulginid folder from plugRootDir
					File deployDir = new File(pluginRootDir + "/" + pluginId);
					if (deployDir.exists()) {
						FileUtils.deleteDirectory(deployDir);
					}
				} catch (IOException ee) {
					log.warn(" can't delete pluginRootDir" + "/" + pluginId
							+ "/" + version);
					log.warn("Failed to deactivate plugin :" + pluginId
							+ " of version :" + version, ee);
//					throw new CoreException("Failed to deactivate plugin :"
//							+ pluginId + " of version :" + version
//							+ " can't delete pluginRootDir" + "/" + pluginId
//							+ "/" + version, ee);
				} catch (Exception e) {
					log.error("Failed to deactivate plugin :" + pluginId
							+ " of version :" + version, e);
					throw new CoreException("Failed to deactivate plugin :"
							+ pluginId + " of version :" + version, e);
				}
				
				
				// 2 remove pulgin from deployedPluginVersions
				deployedPluginVersions.remove(pluginId);
				// 3 change it to deActive state at the same time
				deactivate((Plugin) getPluginDescriptor(pluginId));
				log.info(" plugin :" + pluginId + "was undeploy !");
				
			} else {
				try {
					Plugin p = (Plugin) getPluginDescriptor(pluginId);
					p.equals(p);
					// 1 remove pulginid folder from plugRootDir
					File deployDir = new File(pluginRootDir + "/" + pluginId,
							version);
					if (deployDir.exists()) {
						FileUtils.deleteDirectory(deployDir);

						File deployRoot = new File(pluginRootDir + "/"
								+ pluginId);
						if (deployRoot.exists()) {// del the version when after
													// del this version the
													// plugin have none version
													// in deployDir
							String[] sf = deployRoot.list();
							if (sf.length < 1) {
								FileUtils.deleteDirectory(deployRoot);
							}
						}
					}
					// 2 remove pulgin from deployedPluginVersions
					Map<PluginVersionIdentifier, PluginConfigurationElement> configs = deployedPluginVersions
							.get(pluginId);
					configs.remove(new PluginVersionIdentifier(version));
					if (configs.size() < 1) {// del the version when after del
												// this version the plugin have
												// none version in deployDir
						deployedPluginVersions.remove(pluginId);
					}
					// 3 change it to deActive state at the same time
					deactivate((Plugin) getPluginDescriptor(pluginId));
					log.info(" plugin :" + pluginId + " of version :" + version
							+ "was undeploy !");
				} catch (IOException ee) {
					log.warn(" can't delete pluginRootDir" + "/" + pluginId
							+ "/" + version);
					log.warn("Failed to undeploy plugin :" + pluginId
							+ " of version :" + version, ee);
//					throw new CoreException("Failed to undeploy plugin :"
//							+ pluginId + " of version :" + version
//							+ " can't delete pluginRootDir" + "/" + pluginId
//							+ "/" + version, ee);
				} catch (Exception e) {
					log.warn("Failed to undeploy plugin :" + pluginId
							+ " of version :" + version, e);
					throw new CoreException("Failed to undeploy plugin :"
							+ pluginId + " of version :" + version, e);
				}
			}
			// undeploy children plugin which depending the undeploy plugin
			String[] deployedPluginIds = deployedPluginVersions.keySet()
					.toArray(new String[0]);
			if ((deployedPluginIds != null) && (deployedPluginIds.length > 0)) {
				for (int i = 0; i < deployedPluginIds.length; i++) {
					String pluginId2 = deployedPluginIds[i];
					String version2 = currentActivedPluginVersion
							.get(pluginId2);// the active version
					if (version2 == null) {
						log.error(pluginId2
								+ "is include of deployedPluginVersions but not in currentActivedPluginVersion !! ");
						continue;
					}
					PluginConfigurationElement pluginConfigurationElement = (PluginConfigurationElement) deployedPluginVersions
							.get(pluginId2).get(
									new PluginVersionIdentifier(version2));
					// maybe throw exception because version is null
					IConfigurationElement[] dependElem = pluginConfigurationElement
							.getChildren(CoreConstants.PLUGIN_DEPEND);
					if (dependElem != null && dependElem.length > 0) {
						int index = dependElem[0].getValue().indexOf(pluginId);
						if (index != -1) {
							if (log.isDebugEnabled()) {
								log.debug("when undeploy "
										+ pluginId
										+ " "
										+ version
										+ " find "
										+ pluginId2
										+ " "
										+ version2
										+ "  depend it  so undeploy them at the same time .");
							}
							undeployPlugin(pluginId2, version2);
						}
					}
				}
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wxxr.web.platform.core.IPlatform#createPluginClassLoader(com.wxxr
	 * .web.platform.core.IRuntimeLibrary)
	 */
	protected ClassLoader createPluginClassLoader(IRuntimeLibrary runtimeLibray) {
		if ((runtimeLibray == null) || (runtimeLibray.getJarDir() == null)) {
			return platformClassLoader;
		}
		PluginClassLoader loader = new PluginClassLoader(runtimeLibray,
				parentClassLoader);
		pluginClassLoaders.put(runtimeLibray.getPluginId(), loader);
		return loader;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wxxr.web.platform.core.IPlatform#getPlatformClassLoader()
	 */
	public ClassLoader getPlatformClassLoader() {
		return platformClassLoader;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wxxr.web.platform.core.IPlatform#activatePlugin(java.lang.String)
	 */
	public void activatePlugin(String pluginId, String version)
			throws CoreException {
		if (!isPluginActivated(pluginId, version)) {
			if (!isPluginDeployed(pluginId, version)) {
				throw new CoreException("Plugin :" + pluginId + " of version :"
						+ version + " is not deployed !");
			}
			if (isPluginActivated(pluginId, null)) {
				deactivate((Plugin) getPluginDescriptor(pluginId));
			}
			try {
				Plugin p = activate(getPluginVersion(pluginId, version));
				currentActivedPluginVersion.put(pluginId, version);

				// dispatch event
				// IEvent event = new
				// PluginRegistryChangeEvent(p,PluginRegistryChangeEvent.ADDED);
				// EventManager.getInstance().notifyEvent(event);
				PlatformEvent event = new PlatformEvent(
						p.getUniqueIdentifier(), version);
				fireActivePluginEvent(event);

			} catch (Exception e) {
				log.warn("Failed to activate plugin :" + pluginId
						+ " of version :" + version, e);
				throw new CoreException("Failed to activate plugin :"
						+ pluginId + " of version :" + version, e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wxxr.web.platform.core.IPlatform#deActivatePlugin(java.lang.String,
	 * java.lang.String)
	 */
	public void deActivatePlugin(String pluginId) throws CoreException {
		if (isPluginActivated(pluginId, null)) {
			deactivate(plugins.get(pluginId));
			currentActivedPluginVersion.remove(pluginId);
		}
	}

	private boolean initialization = false;

	public synchronized void start() throws Exception {
		if (initialization == true) {
			return;
		}
		PluginConfigurationElement pe = initPluginRoot();

		scanPluginRoot();
		timer = new Timer();
		timer.scheduleAtFixedRate(scanTask, 5000L, 5000L);
		initialization = true;
	}

	public void stop() {
		if (timer != null) {
			scanTask.cancel();
			timer.cancel();
			timer = null;
			scanTask = null;
		}
	}

	protected void scanPluginRoot() throws Exception {
		synchronized (c) {
			File pluginRoot = new File(pluginRootDir);
			File[] plugins = pluginRoot.listFiles(new FileFilter() {

				public boolean accept(File pathname) {
					if (pathname.isDirectory()) {
						return true;
					}
					return false;
				}
			});
			for (int i = 0; i < plugins.length; i++) {
				File dir = plugins[i];
				if (dir != null) {
					if (log.isDebugEnabled()) {
						log.debug("Scan plugin versions under folder :"
								+ dir.getAbsolutePath());
					}
					scanPluginVersions(dir);
				}
			}
			activatePlugins();
		}
	}

	private PluginConfigurationElement initPluginRoot() throws Exception {
		File pluginRoot = new File(pluginRootDir);
		if (!pluginRoot.exists()) {
			if (!pluginRoot.mkdirs()) {
				throw new CoreException(
						"Failed to create plugin root directory :"
								+ pluginRootDir);
			}
		}
		if (!pluginRoot.isDirectory()) {
			throw new CoreException("Invalid plugin root directory :"
					+ pluginRootDir);
		}
		URL corePluginXML = Platform.class
				.getResource(CoreConstants.PLUGIN_XML);
		PluginConfigurationElement pluginConfig = null;
		;
		if (corePluginXML != null) {
			if (log.isInfoEnabled()) {
				log.info("Found platform core plugin xml :"
						+ corePluginXML.toExternalForm());
			}
			pluginConfig = parser.parse(corePluginXML.openStream());
			File corePluginDir = new File(new File(pluginRoot,
					pluginConfig.getNamespaceIndentifier()), pluginConfig
					.getPluginVersion().toString());

			pluginConfig.setPluginxmlURL(new File(corePluginDir,
					CoreConstants.PLUGIN_XML).toURL());

			Map<PluginVersionIdentifier, PluginConfigurationElement> versions = deployedPluginVersions
					.get(pluginConfig.getNamespaceIdentifier());
			if (versions == null) {
				versions = new HashMap<PluginVersionIdentifier, PluginConfigurationElement>();
				deployedPluginVersions.put(
						pluginConfig.getNamespaceIdentifier(), versions);
			}
			versions.put(pluginConfig.getPluginVersion(), pluginConfig);

			if (!corePluginDir.exists()) {
				if (!corePluginDir.mkdirs()) {
					throw new CoreException(
							"Failed to create platform core plugin directory :"
									+ corePluginDir);
				}
			}
			File xmlFile = new File(corePluginDir, CoreConstants.PLUGIN_XML);
			if (!xmlFile.exists()) {
				if (log.isInfoEnabled()) {
					log.info("Copy platform core plugin xml to plugin directory:"
							+ corePluginDir.getAbsolutePath());
				}
				FileUtils.copyURLToFile(corePluginXML, xmlFile);
			} else {
				if (corePluginXML.openConnection().getLastModified() > xmlFile
						.lastModified()) {
					FileUtils.copyURLToFile(corePluginXML, xmlFile);
				}
			}
			return pluginConfig;
		} else {
			if (log.isInfoEnabled()) {
				log.info("Cannot find platform core plugin xml");
			}

		}

		return pluginConfig;
	}

	protected void scanPluginVersions(File pluginDir) {
		String pluginName = pluginDir.getName();
		Map<PluginVersionIdentifier, PluginConfigurationElement> configs = deployedPluginVersions
				.get(pluginName);
		if (configs == null) {
			configs = new HashMap<PluginVersionIdentifier, PluginConfigurationElement>();
			deployedPluginVersions.put(pluginName, configs);
		}
		File[] versions = pluginDir.listFiles();
		for (int i = 0; i < versions.length; i++) {
			File pathname = versions[i];
			if (configs.containsKey(new PluginVersionIdentifier(pathname
					.getName()))) {
				continue; // this version plugin already deployed
			}
			try {
				File pluginxml = new File(pathname, CoreConstants.PLUGIN_XML);
				if (pluginxml.exists()) {
					if (log.isInfoEnabled()) {
						log.info("Found plugin xml under folder :"
								+ pathname.getAbsolutePath());
					}
					URL xmlURL = pluginxml.toURL();
					InputStream ins = xmlURL.openStream();
					try {
						PluginConfigurationElement pluginConfig = parser
								.parse(ins);
						if (log.isInfoEnabled()) {
							log.info("Plugin xml was parsed successfully!"
									+ pluginxml.getAbsolutePath());
						}
						pluginConfig.setPluginxmlURL(xmlURL);
						configs.put(pluginConfig.getPluginVersion(),
								pluginConfig);
					} finally {
						try {
							ins.close();
						} catch (Exception e) {
						}
					}
				}else{
					if(deployedPluginVersions.containsKey(pluginName)){
						deployedPluginVersions.remove(pluginName);
					}
				}
			} catch (Exception e) {
				log.warn("Failed to scan plugin version under folder :"
						+ pathname.getAbsolutePath(), e);
			}
		}
	}

	public void activatePlugins() throws Exception {
		LinkedList<PluginVersion> startingPlugins = new LinkedList<PluginVersion>();
		String[] deployedPluginIds = deployedPluginVersions.keySet().toArray(
				new String[0]);
		if ((deployedPluginIds != null) && (deployedPluginIds.length > 0)) {
			for (int i = 0; i < deployedPluginIds.length; i++) {
				String pluginId = deployedPluginIds[i];
				String version = getLatestDeployedPluginVersion(pluginId);
				Set depend = new HashSet();
				PluginConfigurationElement pluginConfigurationElement = (PluginConfigurationElement) deployedPluginVersions
						.get(pluginId)
						.get(new PluginVersionIdentifier(version));
				// maybe throw exception because version is null
				IConfigurationElement[] dependElem = pluginConfigurationElement
						.getChildren(CoreConstants.PLUGIN_DEPEND);
				if (dependElem != null && dependElem.length > 0) {
					if (dependElem[0] != null
							&& dependElem[0].getValue() != null) {
						String[] depends = dependElem[0].getValue().split(",");
						for (int j = 0; j < depends.length; j++) {
							String dependId = depends[j];
							dependId = dependId.trim();
							depend.add(dependId);
						}
					}
				}
				PluginVersion v = new PluginVersion();
				v.pluginId = pluginId;
				v.version = version;
				v.depend = depend;
				startingPlugins.add(v);
			}
		}
		if (!startingPlugins.isEmpty()) {
			LinkedList<PluginVersion> orderedPlugin = resolveDependencyOrder(startingPlugins);
			for (Iterator<PluginVersion> iterator = orderedPlugin.iterator(); iterator
					.hasNext();) {
				PluginVersion pluginVersion = iterator.next();
				if (log.isDebugEnabled()) {
					log.debug("do activatePlugin :" + pluginVersion.pluginId
							+ "     version: " + pluginVersion.version);
				}
				activatePlugin(pluginVersion.pluginId, pluginVersion.version);
			}
		}
	}

	private LinkedList<PluginVersion> resolveDependencyOrder(
			LinkedList<PluginVersion> startingPlugins) {
		// return startingPlugins;
		LinkedList<PluginVersion> sortedList = new LinkedList<PluginVersion>();
		for (Iterator iterator = startingPlugins.iterator(); iterator.hasNext();) {
			PluginVersion pluginVersion = (PluginVersion) iterator.next();
			int insertIndex = sortedList.size();
			Set<String> dependSet = pluginVersion.depend;
			for (Iterator iterator2 = dependSet.iterator(); iterator2.hasNext();) {
				String dependPlugId = (String) iterator2.next();
				PluginVersion dependPlug = getPluginVersionFromStartingPlugins(
						dependPlugId, startingPlugins);
				if (dependPlug == null) {
					log.warn(pluginVersion.pluginId + " is depend "
							+ dependPlugId + ", the " + dependPlugId
							+ "is not deployed!  so " + pluginVersion.pluginId
							+ " can't be actived!");
					insertIndex = -1;
					break;
				}
				int dependPluginIndex = sortedList.indexOf(dependPlug);
				int pluginIndex = sortedList.indexOf(pluginVersion);
				if (dependPluginIndex == -1) {
					if (pluginIndex != -1) {
						if (pluginIndex == 0) {
							sortedList.addFirst(dependPlug);
						} else {
							sortedList.add(pluginIndex, dependPlug);
						}
						insertIndex = -1;
					} else {
						sortedList.addLast(dependPlug);
						insertIndex = sortedList.size();
					}
				} else {
					insertIndex = dependPluginIndex + 1;
				}
			}
			if (insertIndex != -1) {
				sortedList.add(insertIndex, pluginVersion);
			}
		}
		return sortedList;
	}

	// if one pluginid has two version in LinkedList return anyone
	private PluginVersion getPluginVersionFromStartingPlugins(String pluginId,
			LinkedList<PluginVersion> startingPlugins) {
		PluginVersion returnPluginVersion = null;
		for (Iterator iterator = startingPlugins.iterator(); iterator.hasNext();) {
			PluginVersion pluginVersion = (PluginVersion) iterator.next();
			if (pluginId.equals(pluginVersion.pluginId)) {
				returnPluginVersion = pluginVersion; // if one pluginid has two
														// version in LinkedList
														// return anyone
			}
		}
		return returnPluginVersion;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wxxr.web.platform.core.IPlatform#isPluginActivated(java.lang.String,
	 * java.lang.String)
	 */
	public boolean isPluginActivated(String pluginId, String version) {
		IPluginDescriptor plugin = getPluginDescriptor(pluginId);
		if (version != null) {
			return (plugin != null)
					&& (plugin.getVersionIdentifier()
							.equals(new PluginVersionIdentifier(version)));
		} else {
			return (plugin != null);
		}
	}

	public IServiceRegistry getServiceRegsitry() {
		return serviceRegistry;
	}

	public Object createPluginObject(IPluginDescriptor plugin,
			String className, IConfigurationElement parameters)
			throws Exception {
		return loadClass(plugin, className).newInstance();
	}

	protected void makeSurePluginInitialized(IPluginDescriptor plugin) {
		if ((!plugin.isPluginInitialized())
				&& (plugin.getPluginInitializer() != null)) {
			plugin.getPluginInitializer().init(plugin, this);
		}
	}

	public Object createPluginObject(String pluginId, String className,
			IConfigurationElement parameters) throws Exception {
		return createPluginObject(getPluginDescriptor(pluginId), className,
				parameters);
	}

	public String[] listAllPluginNames() {
		synchronized (deployedPluginVersions) {
			if (deployedPluginVersions.isEmpty()) {
				return null;
			}
			return deployedPluginVersions.keySet().toArray(
					new String[deployedPluginVersions.size()]);
		}
	}

	public String[] listAllVersionsOfPlugin(String pluginId) {
		synchronized (deployedPluginVersions) {
			Map<PluginVersionIdentifier, PluginConfigurationElement> versions = deployedPluginVersions
					.get(pluginId);
			if ((versions == null) || (versions.isEmpty())) {
				return null;
			}
			String[] result = new String[versions.size()];
			int cnt = 0;
			for (Iterator itr = versions.keySet().iterator(); itr.hasNext();) {
				PluginVersionIdentifier v = (PluginVersionIdentifier) itr
						.next();
				if (v != null) {
					result[cnt++] = v.toString();
				}
			}
			return result;
		}
	}

	public String listActiveVersionOfPlugin(String pluginId) {
		synchronized (plugins) {
			Plugin plugin = plugins.get(pluginId);
			if (plugin == null) {
				return null;
			}
			return plugin.getVersionIdentifier().toString();
		}
	}

	public String[] listAllExtensionPointIds() {
		IExtensionPoint[] exts = registry.getExtensionPoints();
		if (exts == null) {
			return null;
		}
		String[] result = new String[exts.length];
		int cnt = 0;
		for (int i = 0; i < exts.length; i++) {
			IExtensionPoint p = exts[i];
			if (p != null) {
				result[cnt++] = p.getUniqueIdentifier();
			}
		}
		return result;
	}

	public String[] listAllNamespaces() {
		return registry.getNamespaces();
	}

	public String[] listAllExtensionIdsOfPoint(String extensionPointId) {
		IExtensionPoint point = registry.getExtensionPoint(extensionPointId);
		if (point == null) {
			return null;
		}
		IExtension[] exts = point.getExtensions();
		if (exts == null) {
			return null;
		}
		String[] result = new String[exts.length];
		int cnt = 0;
		for (int i = 0; i < exts.length; i++) {
			IExtension p = exts[i];
			if (p != null) {
				result[cnt++] = p.getUniqueIdentifier();
			}
		}
		return result;
	}

	public String[] listAllExtensionIdsOfNamespace(String namespace) {
		IExtension[] exts = registry.getExtensions(namespace);
		if (exts == null) {
			return null;
		}
		String[] result = new String[exts.length];
		int cnt = 0;
		for (int i = 0; i < exts.length; i++) {
			IExtension p = exts[i];
			if (p != null) {
				result[cnt++] = p.getUniqueIdentifier();
			}
		}
		return result;
	}

	public String[] listAllExtensionPointIdsOfNamespace(String namespace) {
		IExtensionPoint[] exts = registry.getExtensionPoints(namespace);
		if (exts == null) {
			return null;
		}
		String[] result = new String[exts.length];
		int cnt = 0;
		for (int i = 0; i < exts.length; i++) {
			IExtensionPoint p = exts[i];
			if (p != null) {
				result[cnt++] = p.getUniqueIdentifier();
			}
		}
		return result;
	}

	public String[] listExtensionConfigurations(String extId) {
		IExtension ext = registry.getExtension(extId);
		if (ext == null) {
			return null;
		}
		IConfigurationElement[] elems = ext.getConfigurationElements();
		if (elems == null) {
			return null;
		}
		String[] result = new String[elems.length];
		int cnt = 0;
		for (int i = 0; i < elems.length; i++) {
			IConfigurationElement p = elems[i];
			if (p != null) {
				result[cnt++] = p.toString();
			}
		}
		return result;
	}

	public String[] listExtensionPointConfigurations(String extId) {
		IExtensionPoint ext = registry.getExtensionPoint(extId);
		if (ext == null) {
			return null;
		}
		IConfigurationElement[] elems = ext.getConfigurationElements();
		if (elems == null) {
			return null;
		}
		String[] result = new String[elems.length];
		int cnt = 0;
		for (int i = 0; i < elems.length; i++) {
			IConfigurationElement p = elems[i];
			if (p != null) {
				result[cnt++] = p.toString();
			}
		}
		return result;

	}

	public Class<?> loadPluginClass(String pluginId, String className)
			throws ClassNotFoundException {
		if (StringUtils.isBlank(pluginId)) {
			throw new IllegalArgumentException("Invalid plugin ID :" + pluginId);
		}
		return loadClass(getPluginDescriptor(pluginId), className);
	}

	protected Class<?> loadClass(IPluginDescriptor plugin, String className)
			throws ClassNotFoundException {
		if (plugin == null) {
			throw new IllegalArgumentException("Invalid plugin : NULL");
		}
		makeSurePluginInitialized(plugin);
		ClassLoader cl = plugin.getPluginClassLoader();
		return cl.loadClass(StringUtils.trim(className));
	}

	public void registerWebClassLoader(URLClassLoader classLoader) {
		if (classLoader == null) {
			throw new IllegalArgumentException();
		}
		((CompositeClassLoader) parentClassLoader)
				.setWebClassLoder(classLoader);
	}

	public void unregisterWebClassLoader(URLClassLoader classLoader) {
		if (classLoader == null) {
			throw new IllegalArgumentException();
		}
		((CompositeClassLoader) parentClassLoader).setWebClassLoder(null);
	}

	public String getWebPluginXmlContent(String pluginId, String version) {
		return getPluginVersion(pluginId, version).toXML();
	}

	public String getPackageUploadURL() {
		return null;
	}

	public void addPlatformListener(IPlatformListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	public void removePlatformListener(IPlatformListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}

	private void fireActivePluginEvent(PlatformEvent event) {
		for (IPlatformListener listener : listeners) {
			listener.onActivePlugin(event);
		}
	}

	private void fireDeactivePluginEvent(PlatformEvent event) {
		for (IPlatformListener listener : listeners) {
			listener.onDeactivePlugin(event);
		}
	}

}
