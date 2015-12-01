/*
 * @(#)ViewManager.java	 2007-10-31
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.workbench.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wxxr.nirvana.platform.CoreException;
import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.platform.IExtension;
import com.wxxr.nirvana.workbench.IView;
import com.wxxr.nirvana.workbench.IViewManager;
import com.wxxr.nirvana.workbench.IWorkbench;
import com.wxxr.nirvana.workbench.UIConstants;
import com.wxxr.nirvana.workbench.config.BaseExtensionPointManager;

/**
 * @author neillin
 *
 */
public class ViewManager extends BaseExtensionPointManager implements IViewManager {
	
	private static final Log log = LogFactory.getLog(ViewManager.class);
	private static final IView[] EMPTY_VIEWS = new IView[0];
	
	private static final String VIEW_TYPE_SIMPLE="simple";
	private static final String VIEW_TYPE_COMPOSITE="composite";
	
	private static final String VIEW_ELEMENT_NAME="view";
	private static final String ATT_VIEW_ID="id";
	private static final String ATT_TYPE="type";
	private static final String ATT_VIEW_CLASS="class";
	
	
	protected Map<String, IView> views = new ConcurrentHashMap<String, IView>();
	protected Map<String, List<String>> exts = new ConcurrentHashMap<String, List<String>>();
	protected IWorkbench workbench;
	
	public ViewManager(){
		super(UIConstants.UI_NAMESPACE,UIConstants.EXTENSION_POINT_VIEWS);
	}
	public IView find(String id) {
		return views.get(id);
	}

	public IView[] getViews() {
		if(views.isEmpty()){
			return EMPTY_VIEWS;
		}
		return views.values().toArray(new IView[views.size()]);
	}

	public void destroy() {
		super.stop();
		views.clear();
		exts.clear();
	}

	protected IView createNewView(IConfigurationElement elem) throws Exception {
		String id = elem.getNamespaceIdentifier() + "." + elem.getAttribute(ATT_VIEW_ID);
		IView view = find(id);
		if(view == null){
			view = new View();
			view.init(this, elem);
			views.put(view.getUniqueIndentifier(), view);	
		}

		return view;
		
	}
	
	@Override
	protected void processExtensionAdded(IExtension ext) {
		IConfigurationElement[] configs = ext.getConfigurationElements();
		List<String> list = exts.get(ext.getUniqueIdentifier());
		for (int i = 0; i < configs.length; i++) {
			IConfigurationElement elem = configs[i];
			if((elem != null)&&VIEW_ELEMENT_NAME.equalsIgnoreCase(elem.getName())){
				try {
					IView view = createNewView(elem);
					if(list == null){
						list = new LinkedList<String>();
						exts.put(ext.getUniqueIdentifier(), list);
					}
					if(!list.contains(view.getId())){
						list.add(view.getId());
					}
				} catch (Exception e) {
					log.warn("Failed to create layout from configuration element :"+elem, e);
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
			IView view = views.remove(id);
			if(view != null){
				view.destroy();
			}
		}
	}
  


	public IWorkbench getWorkbench() {
		return workbench;
	}

	public String[] getViewIds() {
		if(views.isEmpty()){
			return UIConstants.EMPTY_STRING_ARRAY;
		}
		return views.keySet().toArray(new String[views.size()]);
	}

	public IView removeView(String id) {
		IView view = find(id);
		if(view != null){
			views.remove(id);
		}
		return view;
	}

}
