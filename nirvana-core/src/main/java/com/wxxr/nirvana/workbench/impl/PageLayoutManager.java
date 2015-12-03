package com.wxxr.nirvana.workbench.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.platform.IExtension;
import com.wxxr.nirvana.theme.IPageLayout;
import com.wxxr.nirvana.workbench.IPageLayoutManager;
import com.wxxr.nirvana.workbench.UIConstants;
import com.wxxr.nirvana.workbench.config.BaseExtensionPointManager;

public class PageLayoutManager extends BaseExtensionPointManager implements
		IPageLayoutManager {
	
	private Log log = LogFactory.getLog(PageLayoutManager.class);
	
	private static final String ELEMENT_NAME = "pageLayout";
	
	
	protected Map<String, IPageLayout> layouts = new HashMap<String, IPageLayout>();

	public PageLayoutManager() {
		super(UIConstants.UI_NAMESPACE, UIConstants.EXTENSION_POINT_PAGELAYOUT);
	}

	public IPageLayout getPageLayout(String id) {
		return layouts.containsKey(id) ? layouts.get(id) : null;
	}

	public IPageLayout[] getAllPageLayout() {
		return layouts.values().toArray(new IPageLayout[layouts.size()]);
	}

	@Override
	protected void processExtensionAdded(IExtension ext) {
		IConfigurationElement[] configs = ext.getConfigurationElements();
		for (int i = 0; i < configs.length; i++) {
			IConfigurationElement elem = configs[i];
			if ((elem != null) && ELEMENT_NAME.equalsIgnoreCase(elem.getName())) {
				try {
					IPageLayout layout = createNewLayout(elem);
				} catch (Exception e) {
					log.warn(
							"Failed to create permission from configuration element :"
									+ elem, e);
				}
			}
		}
	}

	private IPageLayout createNewLayout(IConfigurationElement elem) {
		String id = elem.getNamespaceIdentifier() + "." + elem.getAttribute("id");
		IPageLayout layout = getPageLayout(id);
		if (layout != null) {
			return layout;
		}
		layout = new PageLayout();
		synchronized (layouts) {
			layouts.put(id, layout);
		}
		return layout;
	}

	@Override
	protected void processExtensionRemoved(IExtension ext) {
		
		IConfigurationElement[] configs = ext.getConfigurationElements();
		for (int i = 0; i < configs.length; i++) {
			IConfigurationElement elem = configs[i];
			if ((elem != null)
					&& ELEMENT_NAME.equalsIgnoreCase(elem.getName())) {
				layouts.remove(elem.getNamespaceIdentifier() + "." + elem.getAttribute("id"));
				destroyLayout(elem.getNamespaceIdentifier() + "." + elem.getAttribute("id"));
			}
		}
	}

	private void destroyLayout(String id) {
		IPageLayout layout = getPageLayout(id);
		layout.destroy();
	}

	public void destroy() {
		
	}

}
