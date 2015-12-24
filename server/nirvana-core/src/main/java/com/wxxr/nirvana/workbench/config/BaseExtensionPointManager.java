/*
 * @(#)BaseExtensionPointManager.java	 2007-11-2
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.workbench.config;

import com.wxxr.nirvana.platform.IExtension;
import com.wxxr.nirvana.platform.IExtensionDelta;
import com.wxxr.nirvana.platform.IExtensionPoint;
import com.wxxr.nirvana.platform.IPlatform;
import com.wxxr.nirvana.platform.IPluginDescriptor;
import com.wxxr.nirvana.platform.IRegistryChangeEvent;
import com.wxxr.nirvana.platform.IRegistryChangeListener;
import com.wxxr.nirvana.platform.PlatformLocator;

/**
 * @author fudapeng
 *
 */
public abstract class BaseExtensionPointManager implements
		IRegistryChangeListener {

	protected String namespace;
	protected String extensionPointId;

	public BaseExtensionPointManager(String namespace, String extensionPointId) {
		if ((namespace == null) || (extensionPointId == null)) {
			throw new IllegalArgumentException();
		}
		this.namespace = namespace;
		this.extensionPointId = extensionPointId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wxxr.web.platform.core.IRegistryChangeListener#registryChanged(com
	 * .wxxr.web.platform.core.IRegistryChangeEvent)
	 */
	public void registryChanged(IRegistryChangeEvent event) {
		if (event == null) {
			return;
		}
		IExtensionDelta[] deltas = event.getExtensionDeltas(namespace,
				extensionPointId);
		IExtension ext = null;
		for (int i = 0; i < deltas.length; i++) {
			IExtensionDelta delta = deltas[i];
			if (delta != null) {
				switch (delta.getKind()) {
				case IExtensionDelta.ADDED:
					ext = delta.getExtension();
					processExtensionAdded(ext);
					break;
				case IExtensionDelta.REMOVED:
					ext = delta.getExtension();
					processExtensionRemoved(ext);
				}
			}
		}
	}

	public void start() {
		IPlatform platform = getUIPlatform();
		IExtensionPoint point = platform.getExtensionRegistry()
				.getExtensionPoint(namespace, extensionPointId);
		IExtension[] extensions = point.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			IExtension ext = extensions[i];
			if (ext != null) {
				processExtensionAdded(ext);
			}
		}
		getUIPlatform().getExtensionRegistry().addRegistryChangeListener(this,
				namespace);
	}

	public void stop() {
		getUIPlatform().getExtensionRegistry().removeRegistryChangeListener(
				this);
	}

	protected IPlatform getUIPlatform() {
		return PlatformLocator.getPlatform();
	}

	protected abstract void processExtensionRemoved(IExtension ext);

	protected abstract void processExtensionAdded(IExtension ext);

	protected Object createPluginObject(String clazz, IPluginDescriptor plugin)
			throws Exception {
		return PlatformLocator.getPlatform().createPluginObject(plugin, clazz,
				null);
	}

}
