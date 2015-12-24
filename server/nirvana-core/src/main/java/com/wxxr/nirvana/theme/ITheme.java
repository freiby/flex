/*
 * @(#)Theme.java	 2007-9-27
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.theme;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.workbench.IContributionItem;
import com.wxxr.nirvana.workbench.impl.ResourceRef;

/**
 * @author fudapeng
 *
 */
public interface ITheme extends IContributionItem {

	String getId();

	String getDescription();

	/**
	 * this method should be called after a new instance was created.
	 * 
	 * added at 2007-11-5
	 * 
	 * @param manager
	 * @param config
	 *            represents the default layout data
	 */
	void init(IThemeManager manager, IConfigurationElement config);

	void destroy();

	IThemeManager getThemeManager();

	IDesktop getDesktop();

	ResourceRef[] getResourceRefs();

	boolean isDefault();
}
