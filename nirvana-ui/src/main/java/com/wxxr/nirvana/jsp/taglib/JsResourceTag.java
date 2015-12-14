package com.wxxr.nirvana.jsp.taglib;

import java.io.IOException;

import com.wxxr.nirvana.util.JspUtil;
import com.wxxr.nirvana.workbench.IWebResource;

public class JsResourceTag extends ResourceTag {

	@Override
	protected String getScript(IWebResource r) throws IOException {
		if (r.getType().equals("js")) {
			String uri = r.getUri();
			return "<script type=\"text/javascript\" src=\"" + JspUtil.getRealResourcePath(r.getContributorId(), r.getContributorVersion(), uri)
					+ "\"></script>";
		}
		return null;
	}
}
