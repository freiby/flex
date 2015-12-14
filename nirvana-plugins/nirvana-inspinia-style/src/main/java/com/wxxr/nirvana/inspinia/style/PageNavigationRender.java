package com.wxxr.nirvana.inspinia.style;

import javax.servlet.jsp.PageContext;

import com.wxxr.nirvana.context.IRequestContext;
import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.ui.PageNavigationContext.PageNavigation;
import com.wxxr.nirvana.util.JspUtil;
import com.wxxr.nirvana.workbench.IRenderContext;
import com.wxxr.nirvana.workbench.IWorkbenchPage;
import com.wxxr.nirvana.workbench.impl.CommonRender;
import com.wxxr.nirvana.workbench.impl.UIComponent;

public class PageNavigationRender extends CommonRender {

	public void render(UIComponent ui, IRenderContext context) throws NirvanaException {
		PageNavigation nav = (PageNavigation)ui;
		IWorkbenchPage[] pages = nav.getAllPages();
		try {
			context.get(IRequestContext.class).getPageContext().setAttribute("pages", pages, PageContext.PAGE_SCOPE);
			context.get(IRequestContext.class).dispatch((JspUtil.getRealPath(getContributorId(), getContributorVersion(), getUri())));
		} catch (Exception e) {
			throw new NirvanaException(e);
		}
	}
}
