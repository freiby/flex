/*
 * @(#)WorkbenchPageManager.java	 2007-10-31
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.workbench.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.Page;

import com.wxxr.nirvana.platform.CoreException;
import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.platform.IExtension;
import com.wxxr.nirvana.platform.IPluginDescriptor;
import com.wxxr.nirvana.workbench.IWorkbench;
import com.wxxr.nirvana.workbench.IWorkbenchManager;
import com.wxxr.nirvana.workbench.IWorkbenchPage;
import com.wxxr.nirvana.workbench.IWorkbenchPageManager;
import com.wxxr.nirvana.workbench.UIConstants;
import com.wxxr.nirvana.workbench.config.BaseExtensionPointManager;

/**
 * @author neillin
 *
 */
public class WorkbenchPageManager extends BaseExtensionPointManager implements IWorkbenchPageManager {
	  private static final IWorkbenchPage[] NO_CHILD = new IWorkbenchPage[0];
	
	  public static final String PAGE_ELEMENT_NAME="page";
	  private static final String ATT_PAGE_ID="id";
	  private static final String ATT_CLASS_NAME="class";
	  
	  private static final Log log = LogFactory.getLog(WorkbenchPageManager.class);
	  
	  protected Map<String, IWorkbenchPage> pages = new HashMap<String, IWorkbenchPage>();
	  protected String currentPageId;
	  protected IWorkbench workbench;
	  
	  public WorkbenchPageManager(){
		  super(UIConstants.UI_NAMESPACE,UIConstants.EXTENSION_POINT_PAGES);
	  }
	  /* (non-Javadoc)
	   * @see com.wxxr.web.platform.interfaces.WorkbenchWindow#createNewPage(java.lang.String)
	   */
	  protected IWorkbenchPage createNewPage(IConfigurationElement elem) throws Exception {
		String pageId = elem.getAttribute(ATT_PAGE_ID);
	    IWorkbenchPage page = getWorkbenchPage(pageId);
	    if(page != null){
	      return page;
	    }
	    String clazz = elem.getAttribute(ATT_CLASS_NAME);
	    if(StringUtils.isBlank(clazz)){
//	    	page = workbench.getWorkbenchManager().createDefaultWorkbenchPage(pageId);
	    }else{
	    	IPluginDescriptor plugin = getUIPlatform().getPluginDescriptor(elem.getNamespaceIdentifier());
//	    	page = (IWorkbenchPage)createPluginObject(clazz, plugin);
	    }
	    page.init(this,elem);
//	    workbench.fireWorkbenchEvent(new WorkbenchPageEvent(page,WorkbenchEventType.CREATED));
	    synchronized(pages){
	      pages.put(pageId, page);
	    }
	    return page;
	  }

	  /* (non-Javadoc)
	   * @see com.wxxr.web.platform.interfaces.WorkbenchWindow#destroyPage(java.lang.String)
	   */
	  public void destroyPage(String pageId) {
	    IWorkbenchPage page = getWorkbenchPage(pageId);
	    if(page == null){
	      return;
	    }
//	    workbench.fireWorkbenchEvent(new WorkbenchPageEvent(page,WorkbenchEventType.DESTROY));
	    page.destroy();
	    if(currentPageId != null){	    	
	    	if(currentPageId.equals(pageId)){
	    		currentPageId = null;
	    	}
	    }
	    	
	  }

	  /* (non-Javadoc)
	   * @see com.wxxr.web.platform.interfaces.WorkbenchWindow#getAllPageIds()
	   */
	  public String[] getAllPageIds() {
	    synchronized(pages){
	      if(pages.isEmpty()){
	        return UIConstants.EMPTY_STRING_ARRAY;
	      }
	      return pages.keySet().toArray(new String[pages.size()]);
	    }
	  }

	  /* (non-Javadoc)
	   * @see com.wxxr.web.platform.interfaces.WorkbenchWindow#getAllPages()
	   */
	  public IWorkbenchPage[] getAllPages() {
		synchronized (pages) {
			if (pages.isEmpty()) {
				return NO_CHILD;
			}
			
			IWorkbenchPage[] ps = pages.values().toArray(new IWorkbenchPage[pages.size()]);
			return pages.values().toArray(new IWorkbenchPage[pages.size()]);
		}
	}
	  

	  public IWorkbenchPage[] getMyPages() {
			synchronized (pages) {
				if (pages.isEmpty()) {
					return NO_CHILD;
				}
				LinkedList<IWorkbenchPage> myPages = new LinkedList<IWorkbenchPage>();
				for (IWorkbenchPage workbenchPage : pages.values()) {
//					if(workbenchPage.isVisible()){
//						myPages.add(workbenchPage);
//					}
				}
				if (myPages.isEmpty()) {
					return NO_CHILD;
				}
				IWorkbenchPage[] ps = myPages.toArray(new IWorkbenchPage[myPages.size()]);
				Arrays.sort(ps);
				return ps;
			}
		}
	  
	  protected String[] getMyPageIds() {
		  IWorkbenchPage[] myPages = getMyPages();
		  if((myPages== null)||(myPages.length == 0)){
			  return UIConstants.EMPTY_STRING_ARRAY;
		  }
		  String[] ids = new String[myPages.length];
		  for (int i = 0; i < myPages.length; i++) {
			ids[i] = myPages[i].getId();
		  }
		  return ids;
	  }

	  /*
		 * (non-Javadoc)
		 * @see com.wxxr.web.platform.interfaces.WorkbenchWindow#getCurrentWorkbenchPage()
		 */
	  public String getCurrentWorkbenchPageId() {
	    return currentPageId;
	  }

	  /* (non-Javadoc)
	   * @see com.wxxr.web.platform.interfaces.WorkbenchWindow#getWorkbenchPage(java.lang.String)
	   */
	  public IWorkbenchPage getWorkbenchPage(String pageId) {
	    synchronized(pages){
	      return pages.get(pageId);
	    }
	  }

	  /* (non-Javadoc)
	   * @see com.wxxr.web.platform.interfaces.WorkbenchWindow#showPage(java.lang.String)
	   */
	  public void showPage(String pageId) {
	    IWorkbenchPage page = getWorkbenchPage(pageId);
	    if(page == null){
	      throw new IllegalArgumentException("Invalid page id!");
	    }
//	    workbench.fireWorkbenchEvent(new WorkbenchPageEvent(page,WorkbenchEventType.ABOUT_TO_DISPLAY));
//	    page.prepareToShow();
	    currentPageId = pageId;
	  }

	public void setCurrentWorkbenchPageId(String currentPageId) {
		IWorkbenchPage page = getWorkbenchPage(currentPageId);
		if((page != null)){
			this.currentPageId = currentPageId;
		}
	}

	public IWorkbenchPage getCurrentWorkbenchPage() {
//		if(currentPageId == null){
//			currentPageId = workbench.getWorkbenchManager().getPreferencesManager().getUserDefaultPage(null, getMyPageIds());
//		}
//		IWorkbenchPage page = getWorkbenchPage(currentPageId);
//		if((page != null)&&(page.isVisible())){
//			return page;
//		}
//		this.currentPageId = workbench.getWorkbenchManager().getPreferencesManager().getUserDefaultPage(null, getMyPageIds());
//		return getWorkbenchPage(currentPageId);
		return null;
	}
	
	public void destroy() {
		super.stop();
		synchronized(pages){
			pages.clear();
		}
	}
	
	public void init(IWorkbench owner) throws CoreException {
		  this.workbench = owner;
		  super.start();
	}
	@Override
	protected void processExtensionAdded(IExtension ext) {
		if(ext == null){
			return;
		}
		IConfigurationElement[] configs = ext.getConfigurationElements();
		for (int i = 0; i < configs.length; i++) {
			IConfigurationElement elem = configs[i];
			if((elem != null)&&PAGE_ELEMENT_NAME.equalsIgnoreCase(elem.getName())){
				try {
					createNewPage(elem);
				} catch (Exception e) {
					log.warn("Failed to create workbench page from configuration element :"+elem, e);
				}
			}
		}		
	}
	
	@Override
	protected void processExtensionRemoved(IExtension ext) {
		IConfigurationElement[] configs = ext.getConfigurationElements();
		for (int i = 0; i < configs.length; i++) {
			IConfigurationElement elem = configs[i];
			if((elem != null)&&PAGE_ELEMENT_NAME.equalsIgnoreCase(elem.getName())){
				pages.remove(elem.getAttribute(ATT_PAGE_ID));
				destroyPage(elem.getAttribute(ATT_PAGE_ID));
			}
		}
	}
	public IWorkbench getWorkbench() {
		return workbench;
	}
	public Page getPage(String pageId) {
		return null;
	}
	public Page getPage(String pageId, IConfigurationElement elem) {
		return null;
	}
	public void addPage(Page page) {
		
	}
	public Page removeLayout(String layoutId) {
		return null;
	}
	public Page[] getPages() {
		return null;
	}
	public String getDefaultPageId() {
		return null;
	}
	public void init(IWorkbenchManager owner) throws CoreException {
		
	}


}
