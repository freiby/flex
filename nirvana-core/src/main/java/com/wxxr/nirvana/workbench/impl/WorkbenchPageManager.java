/*
 * @(#)WorkbenchPageManager.java	 2007-10-31
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.workbench.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wxxr.nirvana.platform.CoreException;
import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.platform.IExtension;
import com.wxxr.nirvana.workbench.ICreateRenderContext;
import com.wxxr.nirvana.workbench.IRender;
import com.wxxr.nirvana.workbench.IWorkbench;
import com.wxxr.nirvana.workbench.IWorkbenchPage;
import com.wxxr.nirvana.workbench.IWorkbenchPageManager;
import com.wxxr.nirvana.workbench.UIConstants;
import com.wxxr.nirvana.workbench.config.BaseExtensionPointManager;

/**
 * @author fudapeng
 *
 */
public class WorkbenchPageManager extends BaseExtensionPointManager implements
		IWorkbenchPageManager {
	private static final IWorkbenchPage[] NO_CHILD = new IWorkbenchPage[0];

	public static final String PAGE_ELEMENT_NAME = "page";
	public static final String ATT_PAGE_ID = "id";
	public static final String ATT_TOOLTIP = "toolTip";
	public static final String ATT_NAME = "name";
	private static final String ATT_CLASS = "class";
	private static final String ATT_RENDER="render";

	private static final Log log = LogFactory
			.getLog(WorkbenchPageManager.class);

	protected Map<String, IWorkbenchPage> pages = new HashMap<String, IWorkbenchPage>();
	protected String currentPageId;
	protected IWorkbench workbench;
	private ICreateRenderContext context;

	public WorkbenchPageManager(ICreateRenderContext iCreateRenderContext) {
		super(UIConstants.UI_NAMESPACE, UIConstants.EXTENSION_POINT_PAGES);
		this.context = iCreateRenderContext;
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wxxr.web.platform.interfaces.WorkbenchWindow#createNewPage(java.lang
	 * .String)
	 */
	protected IWorkbenchPage createNewPage(IConfigurationElement elem)
			throws Exception {
		String pageId = elem.getNamespaceIdentifier() + "."
				+ elem.getAttribute(ATT_PAGE_ID);
		IWorkbenchPage page = getWorkbenchPage(pageId);
		if (page != null) {
			return page;
		}
		page = new WorkbenchPage(context);
		String rid = elem.getAttribute(ATT_RENDER);
		IRender render = null;
		if (StringUtils.isNotBlank(rid)) {
			render = context.createRender(rid);
		}
		page.init(this, elem, render);
		synchronized (pages) {
			pages.put(pageId, page);
		}
		return page;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wxxr.web.platform.interfaces.WorkbenchWindow#destroyPage(java.lang
	 * .String)
	 */
	public void destroyPage(String pageId) {
		IWorkbenchPage page = getWorkbenchPage(pageId);
		if (page == null) {
			return;
		}
		// workbench.fireWorkbenchEvent(new
		// WorkbenchPageEvent(page,WorkbenchEventType.DESTROY));
		page.destroy();
		if (currentPageId != null) {
			if (currentPageId.equals(pageId)) {
				currentPageId = null;
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wxxr.web.platform.interfaces.WorkbenchWindow#getAllPageIds()
	 */
	public String[] getAllPageIds() {
		synchronized (pages) {
			if (pages.isEmpty()) {
				return UIConstants.EMPTY_STRING_ARRAY;
			}
			return pages.keySet().toArray(new String[pages.size()]);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wxxr.web.platform.interfaces.WorkbenchWindow#getAllPages()
	 */
	public IWorkbenchPage[] getAllPages() {
		synchronized (pages) {
			if (pages.isEmpty()) {
				return NO_CHILD;
			}

			IWorkbenchPage[] ps = pages.values().toArray(
					new IWorkbenchPage[pages.size()]);
			return pages.values().toArray(new IWorkbenchPage[pages.size()]);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wxxr.web.platform.interfaces.WorkbenchWindow#getCurrentWorkbenchPage
	 * ()
	 */
	public String getCurrentWorkbenchPageId() {
		return currentPageId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wxxr.web.platform.interfaces.WorkbenchWindow#getWorkbenchPage(java
	 * .lang.String)
	 */
	public IWorkbenchPage getWorkbenchPage(String pageId) {
		synchronized (pages) {
			return pages.get(pageId);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wxxr.web.platform.interfaces.WorkbenchWindow#showPage(java.lang.String
	 * )
	 */
	public void showPage(String pageId) {
		IWorkbenchPage page = getWorkbenchPage(pageId);
		if (page == null) {
			throw new IllegalArgumentException("Invalid page id!");
		}
		// workbench.fireWorkbenchEvent(new
		// WorkbenchPageEvent(page,WorkbenchEventType.ABOUT_TO_DISPLAY));
		// page.prepareToShow();
		currentPageId = pageId;
	}

	public void setCurrentWorkbenchPageId(String currentPageId) {
		IWorkbenchPage page = getWorkbenchPage(currentPageId);
		if ((page != null)) {
			this.currentPageId = currentPageId;
		}
	}

	public IWorkbenchPage getCurrentWorkbenchPage() {
		if (currentPageId != null)
			return getWorkbenchPage(currentPageId);
		return null;
	}

	public void destroy() {
		super.stop();
		synchronized (pages) {
			pages.clear();
		}
	}

	public void init(IWorkbench owner) throws CoreException {
		this.workbench = owner;
		super.start();
	}

	@Override
	protected void processExtensionAdded(IExtension ext) {
		if (ext == null) {
			return;
		}
		IConfigurationElement[] configs = ext.getConfigurationElements();
		for (int i = 0; i < configs.length; i++) {
			IConfigurationElement elem = configs[i];
			if ((elem != null)
					&& PAGE_ELEMENT_NAME.equalsIgnoreCase(elem.getName())) {
				try {
					createNewPage(elem);
				} catch (Exception e) {
					log.warn(
							"Failed to create workbench page from configuration element :"
									+ elem, e);
				}
			}
		}
	}

	@Override
	protected void processExtensionRemoved(IExtension ext) {
		IConfigurationElement[] configs = ext.getConfigurationElements();
		for (int i = 0; i < configs.length; i++) {
			IConfigurationElement elem = configs[i];
			if ((elem != null)
					&& PAGE_ELEMENT_NAME.equalsIgnoreCase(elem.getName())) {
				pages.remove(elem.getNamespaceIdentifier() + "."
						+ elem.getAttribute(ATT_PAGE_ID));
				destroyPage(elem.getNamespaceIdentifier() + "."
						+ elem.getAttribute(ATT_PAGE_ID));
			}
		}
	}

	public IWorkbench getWorkbench() {
		return workbench;
	}

	public String getDefaultPageId() {
		return null;
	}

}
