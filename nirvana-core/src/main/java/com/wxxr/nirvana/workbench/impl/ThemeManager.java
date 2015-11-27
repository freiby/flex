/*
 * @(#)ThemeManager.java	 2007-10-31
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.workbench.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.management.jmx.Trace;
import com.wxxr.nirvana.platform.CoreException;
import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.platform.IExtension;
import com.wxxr.nirvana.platform.IPluginDescriptor;
import com.wxxr.nirvana.theme.ITheme;
import com.wxxr.nirvana.theme.IThemeManager;
import com.wxxr.nirvana.workbench.IWorkbenchManager;
import com.wxxr.nirvana.workbench.UIConstants;
import com.wxxr.nirvana.workbench.config.BaseExtensionPointManager;

/**
 * @author neillin
 *
 */
public class ThemeManager extends BaseExtensionPointManager implements IThemeManager {
	
	private static final Log log = LogFactory.getLog(ThemeManager.class);
	private static final ITheme[] NO_CHILD = new ITheme[0];
	private static final String THEME_ELEMENT_NAME="theme";
	private static final String ATT_ID="id";
	private static final String ATT_CLASS="class";
	private static final String DEFAULT_THEME_ID = "industrial";
	
	public ThemeManager(){
		super(UIConstants.UI_NAMESPACE,UIConstants.EXTENSION_POINT_THEMES);
	}

	protected Map<String, ITheme> themes = new HashMap<String, ITheme>();
	protected Map<String, List<String>> exts = new ConcurrentHashMap<String, List<String>>();
	protected IWorkbenchManager manager;
	
    public void addTheme(ITheme theme) {
      if(theme == null){
        throw new IllegalArgumentException();
      }
      synchronized(themes){
        themes.put(theme.getId(), theme);
      }
      
    }

    public String[] getAllThemeIds() {
      synchronized(themes){
        if(themes.isEmpty()){
          return UIConstants.EMPTY_STRING_ARRAY;
        }
        return themes.keySet().toArray(new String[themes.size()]);
      }
    }

    public ITheme[] getAllThemes() {
      synchronized(themes){
        if(themes.isEmpty()){
          return NO_CHILD;
        }
        return themes.values().toArray(new ITheme[themes.size()]);
      }
    }

    public ITheme getTheme(String themeId) {
      synchronized(themes){
        return themes.get(themeId);
      }
    }

    public ITheme removeTheme(String themeId) {
      synchronized(themes){
        return themes.remove(themeId);
      }
    }

	public ITheme getDefaultTheme() {
		String themeId = manager.getDefaultThemeId();
		return (themeId != null) ? getTheme(themeId) : getTheme(DEFAULT_THEME_ID);
	}

	public void destroy() {
		super.stop();
		
	}

	public void init(IWorkbenchManager owner) throws CoreException {
		manager = owner;
		super.start();
	}

	protected ITheme createNewTheme(IConfigurationElement elem) throws Exception {
		String id = elem.getAttribute(ATT_ID);
		ITheme theme = null;
		String clazz = elem.getAttribute(ATT_CLASS);
		if(StringUtils.isBlank(clazz)){
			theme = new ThemeImpl();
		}else{
	    	IPluginDescriptor plugin = getUIPlatform().getPluginDescriptor(elem.getNamespaceIdentifier());
	    	theme = (ITheme)createPluginObject(clazz, plugin);				
		}
		theme.init(this, elem);
		addTheme(theme);
		return theme;
		
	}
	
	@Override
	protected void processExtensionAdded(IExtension ext) {
		IConfigurationElement[] configs = ext.getConfigurationElements();
		List<String> list = exts.get(ext.getUniqueIdentifier());
		for (int i = 0; i < configs.length; i++) {
			IConfigurationElement elem = configs[i];
			if((elem != null)&&THEME_ELEMENT_NAME.equalsIgnoreCase(elem.getName())){
				try {
					ITheme theme = createNewTheme(elem);
					if(list == null){
						list = new LinkedList<String>();
						exts.put(ext.getUniqueIdentifier(), list);
					}
					if(!list.contains(theme.getId())){
						list.add(theme.getId());
					}
				} catch (Exception e) {
					log.warn("Failed to create theme from configuration element :"+elem, e);
				}
			}
		}		
	}

	@Override
	protected void processExtensionRemoved(IExtension ext) {
		List<String> list = exts.remove(ext.getUniqueIdentifier());
		if((list == null)||list.isEmpty()){
			return;
		}
		String[] ids = list.toArray(new String[list.size()]);
		for (int i = 0; i < ids.length; i++) {
			String id = ids[i];
			ITheme theme = themes.remove(id);
			if(theme != null){
				theme.destroy();
			}
		}
	}
  
  

}
