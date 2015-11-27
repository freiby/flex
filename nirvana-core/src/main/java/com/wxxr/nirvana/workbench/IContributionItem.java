/*
 * @(#)ContributionItem.java	 2007-9-30
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.workbench;

import com.wxxr.nirvana.platform.IConfigurationElement;

/**
 * @author neillin
 *
 */
public interface IContributionItem {
  String getContributorId();
  IConfigurationElement getConfigurationElement();
  String getExtensionId();
}
