package com.wxxr.nirvana.ui.render;

import java.io.IOException;

import javax.servlet.jsp.PageContext;

import com.wxxr.nirvana.IUIRenderContext;
import com.wxxr.nirvana.IRenderProvider;
import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.util.JspUtil;
import com.wxxr.nirvana.workbench.IDispatchUI;
import com.wxxr.nirvana.workbench.impl.UIComponent;

public abstract class DispatchRender extends CommonRenderProvider {

	public void doRender(UIComponent component, IUIRenderContext context)
			throws NirvanaException {
		IDispatchUI disppach = (IDispatchUI) component;
		String uri = disppach.getURI();
		try {
			context.getRequestContext().getPageContext().setAttribute("path", JspUtil.getRealPath(component.getContributorId(),
							component.getContributorVersion()),PageContext.REQUEST_SCOPE);
			context.getRequestContext().dispatch(
					JspUtil.getRealPath(component.getContributorId(),
							component.getContributorVersion(), uri));
		} catch (IOException e) {
			throw new NirvanaException(e);
		}
	}
}
