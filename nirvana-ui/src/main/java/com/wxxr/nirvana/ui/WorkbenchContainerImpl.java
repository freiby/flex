package com.wxxr.nirvana.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import com.wxxr.nirvana.IUIComponentContext;
import com.wxxr.nirvana.IWorkbenchContainer;
import com.wxxr.nirvana.context.ApplicationContext;
import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.workbench.impl.UIComponent;

public class WorkbenchContainerImpl implements IWorkbenchContainer {

	
	public void render(UIComponent component, HttpServletRequest request,
			HttpServletResponse response) {

	}

	public IUIComponentContext startContext(HttpServletRequest request,
			HttpServletResponse response) {
		return null;
	}

	public void render(String component, PageContext context) {
		
	}

	public IUIComponentContext startContext(PageContext context) {
		return null;
	}

	public ApplicationContext getApplicationContext() {
		return null;
	}

	public void endContext(PageContext context) {
		
	}

	public void prepare(String preparer, PageContext context)
			throws NirvanaException {
	}

}
