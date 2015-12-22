package com.wxxr.nirvana.inspinia.style;

import javax.servlet.jsp.PageContext;

import com.wxxr.nirvana.IUIRenderContext;
import com.wxxr.nirvana.context.IRequestContext;
import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.ui.PageNavigationContext.PageNavigation;
import com.wxxr.nirvana.ui.support.PageBeanWrap;
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
			if(pages != null){
				PageBeanWrap[] pagebeans = new PageBeanWrap[pages.length];
				int i = 0;
				for(IWorkbenchPage page : pages){
					PageBeanWrap pbw = new PageBeanWrap(page);
					pagebeans[i] = pbw;
					i++;
				}
				context.get(IUIRenderContext.class).getRequestContext().getPageContext().setAttribute("pages", pagebeans, PageContext.REQUEST_SCOPE);
			}
			context.get(IUIRenderContext.class).getRequestContext().dispatch((JspUtil.getRealPath(getContributorId(), getContributorVersion(), getUri())));
		} catch (Exception e) {
			throw new NirvanaException(e);
		}
	}
}
