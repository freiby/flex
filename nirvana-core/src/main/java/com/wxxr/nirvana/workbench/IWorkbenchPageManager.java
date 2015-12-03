/*
 * @(#)LayoutManager.java	 2007-9-27
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.workbench;


/**
 * @author neillin
 *
 */
public interface IWorkbenchPageManager {
	
  /**
   * return layout initialized with default layout data
   * 
   * @param layoutId
   * @return
   */
  IWorkbenchPage getWorkbenchPage(String pageId);

  IWorkbenchPage[] getAllPages();
  String[] getAllPageIds(); 
//  String getCurrentWorkbenchPageId();
  void destroy();
  void start();

}
