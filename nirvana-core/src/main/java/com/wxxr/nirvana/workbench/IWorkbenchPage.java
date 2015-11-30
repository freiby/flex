/*
 * @(#)WorkbenchPage.java	 2007-9-27
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
public interface IWorkbenchPage extends IUIContributionItem {
  void init(IWorkbenchPageManager manager, IConfigurationElement config);
  IView getViewsById(String id);
  String[] getAllViewIds();
  boolean hasView(String vid);
  void destroy();
  IWorkbenchPageManager getWorkbenchPageManager();
}
