/*
 * @(#)ThemeManager.java	 2007-9-27
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.theme;

import com.wxxr.nirvana.platform.CoreException;
import com.wxxr.nirvana.workbench.IWorkbenchManager;

/**
 * @author neillin
 *
 */
public interface IThemeManager {
  ITheme getTheme(String themeId);
  void addTheme(ITheme theme);
  ITheme removeTheme(String themeId);
  ITheme[] getAllThemes();
  String[] getAllThemeIds(); 
  ITheme getDefaultTheme();
  void init(IWorkbenchManager owner) throws CoreException;
  void destroy();
}
