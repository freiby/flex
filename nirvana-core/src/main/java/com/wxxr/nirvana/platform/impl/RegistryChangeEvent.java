package com.wxxr.nirvana.platform.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.wxxr.nirvana.platform.IExtension;
import com.wxxr.nirvana.platform.IExtensionDelta;
import com.wxxr.nirvana.platform.IExtensionPoint;
import com.wxxr.nirvana.platform.IPlatform;
import com.wxxr.nirvana.platform.IRegistryChangeEvent;
import com.wxxr.nirvana.platform.PlatformLocator;
import com.wxxr.nirvana.util.ArrayHelper;

public class RegistryChangeEvent implements IRegistryChangeEvent {

	private List<IExtensionDelta> deltas;

	public RegistryChangeEvent(List<IExtensionDelta> changes) {
		if ((changes == null) || changes.isEmpty()) {
			throw new IllegalArgumentException();
		}
		this.deltas = new LinkedList<IExtensionDelta>(changes);
	}

	public IExtensionDelta getExtensionDelta(String namespace,
			String extensionPoint, String extension) {
		IPlatform ctx = PlatformLocator.getPlatform();
		String pid = ctx.getNameStrategy().getUniqueIdentifier(namespace,
				extensionPoint);
		for (Iterator itr = deltas.iterator(); itr.hasNext();) {
			IExtensionDelta delta = (IExtensionDelta) itr.next();
			if (delta != null) {
				IExtension ext = delta.getExtension();
				if ((ext != null)
						&& (ext.getExtensionPointUniqueIdentifier().equals(pid))
						&& (ext.getUniqueIdentifier().equals(extension))) {
					return delta;
				}
			}
		}
		return null;
	}

	public IExtensionDelta[] getExtensionDeltas() {
		return ArrayHelper.toExtensionDeltas(deltas);
	}

	public IExtensionDelta[] getExtensionDeltas(String namespace) {
		ArrayList list = new ArrayList();
		IPlatform ctx = PlatformLocator.getPlatform();
		for (Iterator itr = deltas.iterator(); itr.hasNext();) {
			IExtensionDelta delta = (IExtensionDelta) itr.next();
			if (delta != null) {
				IExtension ext = delta.getExtension();
				String pid = null;
				if (ext != null) {
					pid = ext.getExtensionPointUniqueIdentifier();
				}
				IExtensionPoint p = delta.getExtensionPoint();
				if (p != null) {
					pid = p.getUniqueIdentifier();
				}
				if ((pid != null)
						&& namespace.equals(ctx.getNameStrategy()
								.getNamespaceFromIdentifier(pid))) {
					list.add(delta);
				}
			}
		}
		return ArrayHelper.toExtensionDeltas(list);
	}

	public IExtensionDelta[] getExtensionDeltas(String namespace,
			String extensionPoint) {
		ArrayList list = new ArrayList();
		IPlatform ctx = PlatformLocator.getPlatform();
		String pointid = ctx.getNameStrategy().getUniqueIdentifier(namespace,
				extensionPoint);
		for (Iterator itr = deltas.iterator(); itr.hasNext();) {
			IExtensionDelta delta = (IExtensionDelta) itr.next();
			if (delta != null) {
				IExtension ext = delta.getExtension();
				String pid = null;
				if (ext != null) {
					pid = ext.getExtensionPointUniqueIdentifier();
				}
				IExtensionPoint p = delta.getExtensionPoint();
				if (p != null) {
					pid = p.getUniqueIdentifier();
				}
				if ((pid != null) && pid.equals(pointid)) {
					list.add(delta);
				}
			}

		}
		return ArrayHelper.toExtensionDeltas(list);
	}

}
