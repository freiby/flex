/*
 * @(#)AbstractTheme.java	 2007-9-30
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.workbench.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.theme.IDesktop;
import com.wxxr.nirvana.theme.ITheme;
import com.wxxr.nirvana.theme.IThemeManager;
import com.wxxr.nirvana.workbench.ICreateRenderContext;
import com.wxxr.nirvana.workbench.IRender;
import com.wxxr.nirvana.workbench.config.BaseContributionItem;

/**
 * @author fudapeng
 *
 */
public class ThemeImpl extends BaseContributionItem implements ITheme {

	protected String id;
	protected String description;
	protected IThemeManager manager;
	private Desktop desktop;
	private boolean defaultTheme = false;
	private ResourceRef[] resourcerefs;

	private String ATT_RESOURCE = "resource";
	private static final String ATT_CLASS = "class";
	private static final String ATT_RENDER = "render";
	private ICreateRenderContext context;

	private final Log log = LogFactory.getLog(ThemeImpl.class);

	public ThemeImpl(ICreateRenderContext createRender) {
		this.context = createRender;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wxxr.web.platform.interfaces.Theme#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
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
		try {
			initDesktop(config.getChildren("desktop")[0]);
		} catch (Exception e) {
			log.error(" initDesktop error ", e);
		}
		initResources(config);
	}

	private void initResources(IConfigurationElement config) {
		IConfigurationElement[] children = config.getChildren(ATT_RESOURCE);
		if (children != null && children.length > 0) {
			resourcerefs = new ResourceRef[children.length];
			int i = 0;
			for (IConfigurationElement child : children) {
				ResourceRef ref = new ResourceRef(child);
				resourcerefs[i] = ref;
				i++;
			}
		}

	}

	private void initDesktop(IConfigurationElement config) throws Exception {
		if (desktop == null) {
			desktop = new Desktop();
			String renderId = elem.getAttribute(ATT_RENDER);
			IRender render = null;
			if (StringUtils.isNotBlank(renderId)) {
				render = context.createRender(renderId);
			}
			desktop.init(config, render);
		}
	}

	protected void applyConfigure(IConfigurationElement config, boolean init) {
		if (init) {
			id = config.getAttribute("id");
			description = config.getAttribute("description");
			defaultTheme = config.getAttribute("default") == null ? false
					: true;
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
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	public IDesktop getDesktop() {
		return desktop;
	}

	public boolean isDefault() {
		return defaultTheme;
	}

	public ResourceRef[] getResourceRefs() {
		return resourcerefs;
	}

}
