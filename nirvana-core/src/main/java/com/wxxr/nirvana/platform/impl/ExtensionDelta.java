/*
 * @(#)ExtensionDelta.java	 2007-10-29
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.platform.impl;

import com.wxxr.nirvana.platform.IExtension;
import com.wxxr.nirvana.platform.IExtensionDelta;
import com.wxxr.nirvana.platform.IExtensionPoint;

/**
 * @author fudapeng
 *
 */
public class ExtensionDelta implements IExtensionDelta {
	private final Object extObject;
	private final int kind;

	public ExtensionDelta(IExtension ext, int kind) {
		if ((ext == null)
				|| ((kind != IExtensionDelta.ADDED) && (kind != IExtensionDelta.REMOVED))) {
			throw new IllegalArgumentException();
		}
		this.extObject = ext;
		this.kind = kind;
	}

	public ExtensionDelta(IExtensionPoint extp, int kind) {
		if ((extp == null)
				|| ((kind != IExtensionDelta.ADDED) && (kind != IExtensionDelta.REMOVED))) {
			throw new IllegalArgumentException();
		}
		this.extObject = extp;
		this.kind = kind;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wxxr.web.platform.core.IExtensionDelta#getExtension()
	 */
	public IExtension getExtension() {
		return (extObject instanceof IExtension) ? (IExtension) extObject
				: null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wxxr.web.platform.core.IExtensionDelta#getExtensionPoint()
	 */
	public IExtensionPoint getExtensionPoint() {
		return (extObject instanceof IExtensionPoint) ? (IExtensionPoint) extObject
				: null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wxxr.web.platform.core.IExtensionDelta#getKind()
	 */
	public int getKind() {
		return kind;
	}

}
