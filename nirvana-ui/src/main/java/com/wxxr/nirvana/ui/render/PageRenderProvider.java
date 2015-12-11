package com.wxxr.nirvana.ui.render;

import java.io.IOException;

import com.wxxr.nirvana.IPageContext;
import com.wxxr.nirvana.IRenderContext;
import com.wxxr.nirvana.IRenderProvider;
import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.theme.IPageLayout;
import com.wxxr.nirvana.util.JspUtil;
import com.wxxr.nirvana.workbench.impl.UIComponent;
import com.wxxr.nirvana.workbench.impl.WorkbenchPage;

public class PageRenderProvider implements IRenderProvider {

	public boolean accept(UIComponent component) {
		return component instanceof WorkbenchPage;
	}

	public void render(UIComponent component, IRenderContext context)
			throws NirvanaException {
		WorkbenchPage page = (WorkbenchPage) component;
		IPageContext pagecontext = (IPageContext) context.getComponentContext();
		IPageLayout layout = pagecontext.getPageLayout();
		String uri = layout.getURI();
		try {
			context.getRequestContext().dispatch(
					JspUtil.getRealPath(component.getContributorId(),
							component.getContributorVersion(), uri));
		} catch (IOException e) {
			throw new NirvanaException(e);
		}

	}

	public String processComponent() {
		return "page";
	}

}
