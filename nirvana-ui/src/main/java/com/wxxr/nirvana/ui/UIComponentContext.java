package com.wxxr.nirvana.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxxr.nirvana.IUIComponentContext;
import com.wxxr.nirvana.context.IRequestContext;
import com.wxxr.nirvana.workbench.IContributionItem;

public abstract class UIComponentContext implements IUIComponentContext {

	private String uiId;
	
	private IContributionItem uiContribute;
	
	private IUIComponentContext parent;
	
	private Map<String, IUIComponentContext> contexts = new HashMap<String, IUIComponentContext>();
	
	private IRequestContext requestContext;
	
	public UIComponentContext(IContributionItem uiContribute) {
		super();
		this.uiContribute = uiContribute;
	}


	public String getUIId() {
		return uiId;
	}


	public IContributionItem getUiContribute() {
		return uiContribute;
	}
	
	public List<IUIComponentContext> getChildrenContext(){
		return new ArrayList<IUIComponentContext>(contexts.values());
	}
	
	public void init(IUIComponentContext parent,IRequestContext request) {
		this.parent =  parent;
		if(request != null)
		this.requestContext = request;
	}


	public void addChildContext(String name, IUIComponentContext child){
		if(!contexts.containsKey(name)){
			contexts.put(name, child);
		}
	}
	
	public void removeChildContext(String name){
		if(contexts.containsKey(name)){
			contexts.remove(name);
		}
	}
	
	public IUIComponentContext getContext(String name){
		if(contexts.containsKey(name)){
			return contexts.get(name);
		}
		return null;
	}
	
	public IUIComponentContext getParentUIContext() {
		return parent;
	}
	
	public IUIComponentContext getChildUIContext(String componentName) {
		IUIComponentContext result = getContext(componentName);
		if(result != null){
			return result;
		}
		return null;
	}
	
	public IRequestContext getRequestContext(){
		if(requestContext == null){
			return getParentUIContext().getRequestContext();
		}
		return requestContext;
	}
	
}
