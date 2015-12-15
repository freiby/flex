package com.wxxr.nirvana.ui.render;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.jsp.JspWriter;

import org.apache.commons.lang3.StringUtils;

import com.wxxr.nirvana.IUIRenderContext;
import com.wxxr.nirvana.IRenderProvider;
import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.ui.ResourceUIComponent;
import com.wxxr.nirvana.util.JspUtil;
import com.wxxr.nirvana.workbench.IWebResource;
import com.wxxr.nirvana.workbench.impl.UIComponent;

public class ResourceRenderProvider implements IRenderProvider {

	public static final String CSS_TYPE = "js";
	public static final String JS_TYPE = "js";

	public void render(UIComponent component, IUIRenderContext context)
			throws NirvanaException {
		try {
			PrintWriter out = context.getRequestContext().getWriter();
			ResourceUIComponent rui = (ResourceUIComponent) component;
			for (IWebResource item : rui.getResorces()) {
				String script = getScript(item);
				if (StringUtils.isNoneBlank(script)) {
					out.append(script);
				}
			}
		} catch (IOException e) {
			throw new NirvanaException("render resource error " + e);
		}
	}

	private String getScript(IWebResource item) {
		if (item.getType().equals(JS_TYPE)) {
			return "<script type=\"text/javascript\" src=\"" + JspUtil.getRealResourcePath(item.getContributorId(), item.getContributorVersion(), item.getUri())
					+ "\"></script>\r\n";
		} else if (item.getType().equals(CSS_TYPE)) {
			return "<link href=" + JspUtil.getRealResourcePath(item.getContributorId(), item.getContributorVersion(), item.getUri()) + " rel=\"stylesheet\">\r\n";
		}
		return "";

	}

	public String processComponent() {
		return "resource";
	}

}
