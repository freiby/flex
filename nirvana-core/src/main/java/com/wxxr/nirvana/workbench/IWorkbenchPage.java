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
import com.wxxr.nirvana.workbench.impl.WorkbenchPage.ViewRef;

/**
 * @author neillin
 *
 */
public interface IWorkbenchPage extends IUIContributionItem {
  void init(IWorkbenchPageManager manager, IConfigurationElement config);
  IView getViewsById(String id);
  String[] getAllViewIds();
  ViewRef[] getAllViewRefs();
  ViewRef getViewRefById(String id);
  boolean hasView(String vid);
  
  void destroy();
  IWorkbenchPageManager getWorkbenchPageManager();
}
