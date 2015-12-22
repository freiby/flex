package com.wxxr.nirvana.ui.render;

import java.io.IOException;

import com.wxxr.nirvana.IPageContext;
import com.wxxr.nirvana.IUIRenderContext;
import com.wxxr.nirvana.IRenderProvider;
import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.theme.IPageLayout;
import com.wxxr.nirvana.util.JspUtil;
import com.wxxr.nirvana.workbench.IRender;
import com.wxxr.nirvana.workbench.impl.UIComponent;
import com.wxxr.nirvana.workbench.impl.WorkbenchPage;

public class PageRenderProvider extends CommonRenderProvider{

	public void doRender(UIComponent component, IUIRenderContext context)
			throws NirvanaException {
		IPageContext pagecontext = (IPageContext) context.getComponentContext();
		IPageLayout layout = pagecontext.getPageLayout();
		IRender layoutRender = layout.getRender();
		if(layoutRender != null){
			layoutRender.render(component, createContext(context));
			return;
		}
		String uri = layout.getURI();
		try {
			context.getRequestContext().dispatch(
					JspUtil.getRealPath(layout.getContributorId(),
							component.getContributorVersion(), uri));
		} catch (IOException e) {
			throw new NirvanaException(e);
		}
	}

	public String processComponent() {
		return "page";
	}

}
