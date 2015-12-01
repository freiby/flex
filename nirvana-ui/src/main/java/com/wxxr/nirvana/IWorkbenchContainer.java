package com.wxxr.nirvana;

import javax.servlet.jsp.PageContext;

import com.wxxr.nirvana.context.ApplicationContext;
import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.workbench.impl.UIComponent;

public interface IWorkbenchContainer {
	void render(UIComponent component,PageContext context);
	IUIComponentContext startContext(PageContext context);
	ApplicationContext getApplicationContext();
	void endContext(PageContext context);
	void prepare(String preparer,PageContext context) throws NirvanaException;
}
