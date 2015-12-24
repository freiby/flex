package com.wxxr.nirvana.workbench.impl;

import java.lang.reflect.Method;

import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.workbench.IActionProxy;
import com.wxxr.nirvana.workbench.config.BaseContributionItem;

public class ActionProxy extends BaseContributionItem implements IActionProxy {
	private Object action;
	public ActionProxy() {
	}

	public void init(IConfigurationElement elem) {
		if (elem == null) {
			throw new IllegalArgumentException();
		}
		setConfigurationElement(elem);
	}

	public void setAction(Object action) {
		this.action = action;
	}

	public Object getAction() {
		return action;
	}

	public void invoke(String method) throws NirvanaException {
		if(action != null){
			try {
				Method m = action.getClass().getMethod(method);
				m.invoke(action);
			} catch (Exception e) {
				throw new NirvanaException(e);
			} 
		}
	}

	public void destory() {
		
	}
}
