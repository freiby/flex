package com.wxxr.nirvana.ui;

import java.util.Map;

import com.wxxr.nirvana.ISessionWorkbench;
import com.wxxr.nirvana.workbench.IContributionItem;
import com.wxxr.nirvana.workbench.IView;
import com.wxxr.nirvana.workbench.impl.UIComponent;
import com.wxxr.nirvana.workbench.impl.WorkbenchPage.ViewRef;

public class ViewContext extends UIComponentContext {

	private ViewRef[] viewrefs;
	private ISessionWorkbench workbench = null;
	public ViewContext(IContributionItem uiContribute,ViewRef[] viewrefs) {
		super(uiContribute);
		this.viewrefs = viewrefs;
		workbench = (ISessionWorkbench) getRequestContext().getSessionScope().get(WorkbenchContainerImpl.WORKBENCH_PROXY);
	}
	
	private IView pickUp(Map<String,Object> parameters){
		if(parameters.containsKey("anchor")){
			String anchor = (String) parameters.get("anchor");
			for(ViewRef item : viewrefs){
				if(item.get("attachment").equals(anchor)){
					IView view = workbench.getViewManager().find(item.getId());
					return view;
				}
			}
		}
		return null;
	}
	
	public UIComponent getCurrentComponent(Map<String,Object> parameters) {
		IView view = pickUp(parameters);
		return (UIComponent) view;
	}

}
