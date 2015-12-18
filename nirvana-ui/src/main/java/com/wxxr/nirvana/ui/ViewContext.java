package com.wxxr.nirvana.ui;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wxxr.nirvana.ContainerAccess;
import com.wxxr.nirvana.ISessionWorkbench;
import com.wxxr.nirvana.workbench.IContributionItem;
import com.wxxr.nirvana.workbench.IView;
import com.wxxr.nirvana.workbench.impl.UIComponent;
import com.wxxr.nirvana.workbench.impl.WorkbenchPage.ViewRef;

public class ViewContext extends UIComponentContext {

	private ViewRef[] viewrefs;
	private ISessionWorkbench workbench = null;
	private Log log = LogFactory.getLog(ViewContext.class);

	public ViewContext(IContributionItem uiContribute, ViewRef[] viewrefs) {
		super(uiContribute);
		this.viewrefs = viewrefs;
		workbench = ContainerAccess.getSessionWorkbench();
	}

	private IView pickUp(Map<String, Object> parameters) throws Exception {
		if (parameters.containsKey("anchor")) {
			String anchor = (String) parameters.get("anchor");
			for (ViewRef item : viewrefs) {
				if (item.get("attachment").equals(anchor)) {
					IView view = workbench.getCurrentPage().createViewIfPrimaryIdView(item.getId());
					return view;
				}
			}
		}
		return null;
	}

	public UIComponent getCurrentComponent(Map<String, Object> parameters) {
		IView view = null;
		try {
			view = pickUp(parameters);
		} catch (Exception e) {
			log.error("pickup view error" + e);
		}
		return (UIComponent) view;
	}

}
