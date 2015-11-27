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
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.workbench.IView;
import com.wxxr.nirvana.workbench.IWorkbenchPage;
import com.wxxr.nirvana.workbench.IWorkbenchPageManager;
import com.wxxr.nirvana.workbench.UIComponent;
import com.wxxr.nirvana.workbench.UIConstants;

/**
 * @author neillin
 *
 */
public class WorkbenchPage extends UIComponent implements IWorkbenchPage, Comparable<WorkbenchPage> {

	protected static final Log log = LogFactory.getLog(WorkbenchPage.class);
	protected static final String ATTR_NAME_DEFAULT_REGION = "defaultRegion";
	protected static final String REQ_ATTR_NAME_PREPARED = "_pg_done_";

	
	protected IWorkbenchPageManager manager;
	protected String defaultRegionId;
	protected Map<String, List<String>> viewsOfRegion = new HashMap<String, List<String>>();

	/* (non-Javadoc)
	 * @see com.wxxr.web.platform.interfaces.WorkbenchPage#getWorkbenchWindow()
	 */
	public IWorkbenchPageManager getWorkbenchPageManager() {
		return manager;
	}

	public int compareTo(WorkbenchPage o) {
		
		return 0;
	}

	public void init(IWorkbenchPageManager manager, IConfigurationElement config) {
		
		
	}

	public IView[] getViewsById(String id) {
		
		return null;
	}

	public String[] getAllViewIds() {
		
		return null;
	}

	public void addViewToRegion(String regionId, String viewId,
			IConfigurationElement data) {
		
		
	}

	public IView removeViewById(String viewId) {
		
		return null;
	}

	public void destroy() {
		
		
	}
}
