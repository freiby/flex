/*
 * @(#)ThemeManager.java	 2007-9-27
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.theme;


public interface IThemeManager {
  ITheme getTheme(String themeId);
  ITheme removeTheme(String themeId);
  ITheme[] getAllThemes();
  String[] getAllThemeIds(); 
  ITheme getDefaultTheme();
  void destroy();
  void start();
}
