/*
 * @(#)LayoutManager.java	 2007-9-27
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.workbench;

import org.apache.tiles.Page;

import com.wxxr.nirvana.platform.CoreException;
import com.wxxr.nirvana.platform.IConfigurationElement;

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
  Page getPage(String pageId);

  /**
   * return a new-created layout initialized with specific configuration, 
   * this layout would not be shared with other page 
   * 
   * @param layoutId
   * @return
   */
  Page getPage(String layoutId, IConfigurationElement elem);
  void addPage(Page page);
  Page removeLayout(String layoutId);
  Page[] getPages();
  String[] getAllPageIds(); 
  String getDefaultPageId();	 
  void init(IWorkbenchManager owner) throws CoreException;
  void destroy();

}
