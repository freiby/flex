package com.wxxr.nirvana.platform.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.platform.IContributor;
import com.wxxr.nirvana.platform.IExtension;
import com.wxxr.nirvana.platform.IExtensionDelta;
import com.wxxr.nirvana.platform.IExtensionPoint;
import com.wxxr.nirvana.platform.IExtensionRegistry;
import com.wxxr.nirvana.platform.IPlatform;
import com.wxxr.nirvana.platform.IRegistryChangeEvent;
import com.wxxr.nirvana.platform.IRegistryChangeListener;
import com.wxxr.nirvana.platform.MessageResourceBundle;
import com.wxxr.nirvana.util.ArrayHelper;

public class BaseExtensionRegistry implements IExtensionRegistry {
	private IPlatform context;
	private Map<String, IExtension> extensions = new ConcurrentHashMap<String, IExtension>();
	private Map<String, IExtensionPoint> extensionpoints = new ConcurrentHashMap<String, IExtensionPoint>();
	private Map<String, LinkedList<IExtension>> unresolvedExtensions = new ConcurrentHashMap<String, LinkedList<IExtension>>();
	private Map<IRegistryChangeListener, Listener> listeners = new ConcurrentHashMap<IRegistryChangeListener, Listener>();

	private static class Listener {
		private String namespace;
		private IRegistryChangeListener l;

		public Listener(String ns, IRegistryChangeListener l) {
			this.namespace = ns;
			this.l = l;
		}

		public boolean isListenerApplied(IRegistryChangeEvent event) {
			if (namespace == null) {
				return true;
			}
			IExtensionDelta[] deltas = event.getExtensionDeltas(namespace);
			if ((deltas != null) && (deltas.length > 0)) {
				return true;
			} else {
				return false;
			}
		}
	}

	// private void fireEvent()

	public BaseExtensionRegistry(IPlatform context) {
		if (context == null) {
			throw new IllegalArgumentException();
		}
		this.context = context;
	}

	public boolean addContribution(Object source, IContributor contributor,
			String name, MessageResourceBundle translationBundle)
			throws IllegalArgumentException {
		Object obj = context.getObjectFactory().createExtensionObject(this,
				source);
		List<IExtensionDelta> deltas = null;
		boolean result = false;
		if (obj != null) {
			if (obj instanceof IExtensionPoint) {
				String pid = ((IExtensionPoint) obj).getUniqueIdentifier();
				extensionpoints.put(pid, (IExtensionPoint) obj);
				deltas = new LinkedList<IExtensionDelta>();
				deltas.add(new ExtensionDelta((IExtensionPoint) obj,
						IExtensionDelta.ADDED));
				resovleUnresolvedExtensions((IExtensionPoint) obj, deltas);
				result = true;
			} else if (obj instanceof IExtension) {
				String pid = ((IExtension) obj)
						.getExtensionPointUniqueIdentifier();
				String extid = ((IExtension) obj).getUniqueIdentifier();
				IExtensionPoint p = getExtensionPoint(pid);
				if (p != null) {
					context.getObjectFactory().addExtensionTo(p,
							(IExtension) obj);
					extensions.put(extid, (IExtension) obj);
					deltas = new LinkedList<IExtensionDelta>();
					deltas.add(new ExtensionDelta((IExtension) obj,
							IExtensionDelta.ADDED));
					result = true;
				} else {
					LinkedList<IExtension> unresolved = unresolvedExtensions
							.get(pid);
					if (unresolved == null) {
						unresolved = new LinkedList<IExtension>();
						unresolvedExtensions.put(pid, unresolved);
					}
					unresolved.add((IExtension) obj);
				}
			}
			if ((deltas != null) && (!deltas.isEmpty())) {
				doFireRegistryEvent(new RegistryChangeEvent(deltas));
			}
		}
		return result;
	}

	protected void doFireRegistryEvent(IRegistryChangeEvent event) {
		if (event == null) {
			throw new IllegalArgumentException();
		}
		Listener[] pls = null;
		synchronized (listeners) {
			if (listeners.isEmpty()) {
				return;
			}
			pls = listeners.values().toArray(new Listener[listeners.size()]);
		}
		for (int i = 0; (pls != null) && (i < pls.length); i++) {
			Listener l = pls[i];
			if ((l != null) && l.isListenerApplied(event)) {
				l.l.registryChanged(event);
			}
		}
	}

	protected void resovleUnresolvedExtensions(IExtensionPoint point,
			List<IExtensionDelta> deltas) {
		LinkedList<IExtension> unresolveds = unresolvedExtensions.remove(point
				.getUniqueIdentifier());
		if ((unresolveds == null) || unresolveds.isEmpty()) {
			return;
		}
		for (Iterator<IExtension> iterator = unresolveds.iterator(); iterator
				.hasNext();) {
			IExtension extension = iterator.next();
			context.getObjectFactory().addExtensionTo(point, extension);
			extensions.put(extension.getUniqueIdentifier(), extension);
			deltas.add(new ExtensionDelta(extension, IExtensionDelta.ADDED));
		}
	}

	public void addRegistryChangeListener(IRegistryChangeListener listener,
			String namespace) {
		if (listeners == null) {
			listeners = new HashMap();
		} else {
			if (listeners.containsKey(listener)) {
				return;
			}
		}
		listeners.put(listener, new Listener(namespace, listener));
	}

	public void addRegistryChangeListener(IRegistryChangeListener listener) {
		addRegistryChangeListener(listener, null);

	}

	public IConfigurationElement[] getConfigurationElementsFor(
			String extensionPointId) {
		IExtensionPoint p = (IExtensionPoint) extensionpoints
				.get(extensionPointId);
		return (p != null) ? p.getConfigurationElements()
				: new IConfigurationElement[0];
	}

	public IConfigurationElement[] getConfigurationElementsFor(
			String namespace, String extensionPointName) {
		String pid = context.getNameStrategy().getUniqueIdentifier(namespace,
				extensionPointName);
		return getConfigurationElementsFor(pid);
	}

	public IConfigurationElement[] getConfigurationElementsFor(
			String namespace, String extensionPointName, String extensionId) {
		String pid = context.getNameStrategy().getUniqueIdentifier(namespace,
				extensionPointName);
		IExtensionPoint p = (IExtensionPoint) extensionpoints.get(pid);
		IExtension ext = (p != null) ? p.getExtension(extensionId) : null;
		return (ext != null) ? ext.getConfigurationElements()
				: new IConfigurationElement[0];
	}

	public IExtension getExtension(String extensionId) {
		IExtension ext = (IExtension) extensions.get(extensionId);
		return ext;
	}

	public IExtension getExtension(String extensionPointId, String extensionId) {
		IExtensionPoint p = (IExtensionPoint) extensionpoints
				.get(extensionPointId);
		IExtension ext = (p != null) ? p.getExtension(extensionId) : null;
		return ext;
	}

	public IExtension getExtension(String namespace, String extensionPointName,
			String extensionId) {
		String pid = context.getNameStrategy().getUniqueIdentifier(namespace,
				extensionPointName);
		return getExtension(pid, extensionId);
	}

	public IExtensionPoint getExtensionPoint(String extensionPointId) {
		return (IExtensionPoint) extensionpoints.get(extensionPointId);
	}

	public IExtensionPoint getExtensionPoint(String namespace,
			String extensionPointName) {
		String pid = context.getNameStrategy().getUniqueIdentifier(namespace,
				extensionPointName);
		return getExtensionPoint(pid);
	}

	public IExtensionPoint[] getExtensionPoints() {
		Collection vals = extensionpoints.values();
		return ArrayHelper.toExtensionPoints(vals);
	}

	public IExtensionPoint[] getExtensionPoints(String namespace) {
		ArrayList list = new ArrayList();
		for (Iterator itr = extensionpoints.entrySet().iterator(); itr
				.hasNext();) {
			Entry entry = (Entry) itr.next();
			String pid = (String) entry.getKey();
			IExtensionPoint p = (IExtensionPoint) entry.getValue();
			if (context.getNameStrategy().getNamespaceFromIdentifier(pid)
					.equals(namespace)) {
				list.add(p);
			}
		}
		return ArrayHelper.toExtensionPoints(list);
	}

	public IExtension[] getExtensions(String namespace) {
		ArrayList list = new ArrayList();
		for (Iterator itr = extensions.entrySet().iterator(); itr.hasNext();) {
			Entry entry = (Entry) itr.next();
			String extid = (String) entry.getKey();
			if (context.getNameStrategy().getNamespaceFromIdentifier(extid)
					.equals(namespace)) {
				list.add(entry.getValue());
			}
		}
		return ArrayHelper.toExtensions(list);
	}

	public IExtension[] getExtensions() {
		Collection vals = extensions.values();
		return ArrayHelper.toExtensions(vals);
	}

	public String[] getNamespaces() {
		ArrayList list = new ArrayList();
		for (Iterator itr = extensionpoints.entrySet().iterator(); itr
				.hasNext();) {
			Entry entry = (Entry) itr.next();
			String ns = context.getNameStrategy().getNamespaceFromIdentifier(
					(String) entry.getKey());
			if (!list.contains(ns)) {
				list.add(ns);
			}
		}
		for (Iterator itr = extensions.entrySet().iterator(); itr.hasNext();) {
			Entry entry = (Entry) itr.next();
			String ns = context.getNameStrategy().getNamespaceFromIdentifier(
					(String) entry.getKey());
			if (!list.contains(ns)) {
				list.add(ns);
			}
		}
		return ArrayHelper.toStrings(list);
	}

	public boolean removeExtension(IExtension extension)
			throws IllegalArgumentException {

		String extid = extension.getUniqueIdentifier();
		IExtension ext = (IExtension) extensions.remove(extid);
		if (ext != null) {
			// remove extension from extension point
			String pid = ext.getExtensionPointUniqueIdentifier();
			IExtensionPoint extensionPoint = (IExtensionPoint) extensionpoints
					.get(pid);
			if (extensionPoint != null) {
				context.getObjectFactory().removeExtensionFrom(extensionPoint,
						ext);
			}
			List<IExtensionDelta> deltas = new LinkedList<IExtensionDelta>();
			deltas.add(new ExtensionDelta(ext, IExtensionDelta.REMOVED));
			doFireRegistryEvent(new RegistryChangeEvent(deltas));
			return true;
		}
		return false;
	}

	protected boolean checkRemovePermission(IExtension extension) {
		return true;
	}

	protected boolean checkStopPermission() {
		return true;
	}

	public boolean removeExtensionPoint(IExtensionPoint extensionPoint)
			throws IllegalArgumentException {
		String pid = extensionPoint.getUniqueIdentifier();
		extensionPoint = (IExtensionPoint) extensionpoints.remove(pid);
		if (extensionPoint != null) {
			IExtension[] exts = extensionPoint.getExtensions();
			List<IExtensionDelta> deltas = new LinkedList<IExtensionDelta>();
			deltas.add(new ExtensionDelta(extensionPoint,
					IExtensionDelta.REMOVED));
			LinkedList<IExtension> unresolveds = unresolvedExtensions.get(pid);
			if (unresolveds == null) {
				unresolveds = new LinkedList<IExtension>();
				unresolvedExtensions.put(pid, unresolveds);
			}
			for (int i = 0; (exts != null) && (i < exts.length); i++) {
				IExtension ext = exts[i];
				if (ext != null) {
					extensions.remove(ext.getUniqueIdentifier());
					unresolveds.add(ext);
					deltas.add(new ExtensionDelta(ext, IExtensionDelta.REMOVED));
				}
			}
			doFireRegistryEvent(new RegistryChangeEvent(deltas));
			return true;
		}
		return false;
	}

	public void removeRegistryChangeListener(IRegistryChangeListener listener) {
		if (listeners != null) {
			listeners.remove(listener);
		}
	}

	public void stop() {
	}
}
