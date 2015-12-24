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
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.workbench.ICreateRenderContext;
import com.wxxr.nirvana.workbench.IRender;
import com.wxxr.nirvana.workbench.IView;
import com.wxxr.nirvana.workbench.IViewManager;
import com.wxxr.nirvana.workbench.IWorkbenchPage;
import com.wxxr.nirvana.workbench.IWorkbenchPageManager;

/**
 * @author fudapeng
 *
 */
public class WorkbenchPage extends UIComponent implements IWorkbenchPage {

	protected static final Log log = LogFactory.getLog(WorkbenchPage.class);

	protected IWorkbenchPageManager manager;
	protected String defaultRegionId;
	protected Map<String, ViewRef> viewMap = new HashMap<String, ViewRef>();

	public static final String VIEW_ELEMENT = "view";

	private IViewManager viewManager;
	
	private ICreateRenderContext createContext;

	
	public WorkbenchPage(ICreateRenderContext createContext) {
		super();
		this.createContext = createContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wxxr.web.platform.interfaces.WorkbenchPage#getWorkbenchWindow()
	 */
	public IWorkbenchPageManager getWorkbenchPageManager() {
		return manager;
	}

	public void init(IWorkbenchPageManager manager,
			IConfigurationElement config, IRender render) {
		super.init(config, render);
		startViewManager();
		initViews(config);
	}
	
	public void startViewManager() {
		if (viewManager == null) {
			viewManager = new ViewManager(createContext);
			viewManager.start();
		}
	}

	protected void initViews(IConfigurationElement config) {
		IConfigurationElement[] viewConfigs = config.getChildren(VIEW_ELEMENT);
		if (viewConfigs == null)
			return;
		for (int j = 0; j < viewConfigs.length; j++) {
			IConfigurationElement viewConfig = viewConfigs[j];
			String viewid = viewConfig.getAttribute(UIComponent.ATT_REF);
			ViewRef v = new ViewRef(viewid, viewConfig);
			viewMap.put(viewid, v);
		}
	}

	public boolean hasView(String vid) {
		return viewMap.keySet().contains(vid);
	}

	public IView getViewsById(String id) {
		if (hasView(id)) {
			return viewManager.find(id);
		}
		return null;
	}

	public String[] getAllViewIds() {
		return viewMap.keySet().toArray(new String[viewMap.keySet().size()]);
	}

	public void destroy() {

	}

	public class ViewRef {

		private String id;
		private String secondaryId;
		private IConfigurationElement elem;

		public ViewRef(String id, IConfigurationElement elem) {
			super();
			this.id = id;
			this.elem = elem;
		}

		public String get(String attr) {
			if (elem != null) {
				return elem.getAttribute(attr);
			}
			return null;
		}

		public String getId() {
			return id;
		}

	}

	public ViewRef[] getAllViewRefs() {
		return this.viewMap.values().toArray(new ViewRef[viewMap.values().size()]);
	}

	public ViewRef getViewRefById(String id) {
		if(this.viewMap.containsKey(id)){
			return viewMap.get(id);
		}
		return null;
	}

	public void addView(String vid) {
		ViewRef vrf = new ViewRef(vid,null);
		if(!hasView(vid)){
			viewMap.put(vid, vrf);
		}
	}

	public void removeView(String vid) {
		if(hasView(vid)){
			viewMap.remove(vid);
		}
	}

	public IView createViewIfPrimaryIdView(String id) throws Exception {
		return viewManager.createViewIfPrimaryIdView(id);
	}

	public IView[] getViews() {
		return viewManager.getViews();
	}

	public void addView(ViewRef viewRef) {
		if(!hasView(viewRef.id)){
			viewMap.put(viewRef.id, viewRef);
		}
	}

}
