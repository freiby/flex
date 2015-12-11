package com.wxxr.nirvana.jsp.taglib;

import java.io.IOException;

import com.wxxr.nirvana.workbench.IWebResource;

public class CssResourceTag extends ResourceTag {

	@Override
	protected String getScript(IWebResource r) throws IOException {
		if (r.getType().equals("css")) {
			String uri = r.getUri();
			return "<link href=" + uri + " rel=\"stylesheet\">";
		}
		return null;
	}

}
