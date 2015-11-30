/*
 * @(#)AbstractWorkbenchPage.java	 2007-9-28
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.workbench.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.workbench.IView;
import com.wxxr.nirvana.workbench.IViewManager;
import com.wxxr.nirvana.workbench.IWorkbenchPage;
import com.wxxr.nirvana.workbench.IWorkbenchPageManager;

/**
 * @author neillin
 *
 */
public class WorkbenchPage extends UIComponent implements IWorkbenchPage {

	protected static final Log log = LogFactory.getLog(WorkbenchPage.class);

	protected IWorkbenchPageManager manager;
	protected String defaultRegionId;
	protected Map<String, List<String>> viewsOfRegion = new HashMap<String, List<String>>();
	
	public static final String VIEW_ELEMENT = "view";
	
	private String[] viewids;
	
	private IViewManager viewManager;
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wxxr.web.platform.interfaces.WorkbenchPage#getWorkbenchWindow()
	 */
	public IWorkbenchPageManager getWorkbenchPageManager() {
		return manager;
	}

	public void init(IWorkbenchPageManager manager, IConfigurationElement config) {
		super.init(config);
		initViews(config);
	}
	
	
	protected void initViews(IConfigurationElement config) {
		IConfigurationElement[] viewConfigs = config.getChildren(VIEW_ELEMENT);
		viewids = new String[viewConfigs.length];
		for (int j = 0; j < viewConfigs.length; j++) {
			IConfigurationElement viewConfig = viewConfigs[j];
			String viewid = viewConfig.getAttribute(UIComponent.ATT_REF);
			viewids[j] = viewid;
		}
	}
	
	public boolean hasView(String vid){
		if(viewids != null){
			for(String id : viewids){
				if(id.equals(vid)){
					return true;
				}
			}
		}
		return false;
	}

	public IView getViewsById(String id) {
		if(hasView(id)){
			return viewManager.find(id);
		}
		return null;
	}

	public String[] getAllViewIds() {
		return viewids;
	}

	public void destroy() {
		
	}

}
