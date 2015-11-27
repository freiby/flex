/*
 * @(#)AbstractTheme.java	 2007-9-30
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.workbench.impl;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.theme.ITheme;
import com.wxxr.nirvana.theme.IThemeManager;
import com.wxxr.nirvana.workbench.config.BaseContributionItem;

/**
 * @author neillin
 *
 */
public class ThemeImpl extends BaseContributionItem implements ITheme {

  protected String id;
  protected String description;
  protected IThemeManager manager;

  /* (non-Javadoc)
   * @see com.wxxr.web.platform.interfaces.Theme#getDescription()
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }


	public final void applyConfigure(IConfigurationElement config) {
		applyConfigure(config, false);		
	}
	
	public void destroy() {
		
	}
	
	public void init(IThemeManager manager, IConfigurationElement config) {
		this.manager = manager;
		applyConfigure(config, true);
	}
	
	protected void applyConfigure(IConfigurationElement config, boolean init) {
		if(init){
			id = config.getAttribute("id");
			description = config.getAttribute("desc");
			setConfigurationElement(config);
		}		
	}

	public IThemeManager getThemeManager() {
		return manager;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}


}
