package com.wxxr.nirvana.jsp.taglib;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;

import com.wxxr.nirvana.exception.NirvanaException;

public class UITag extends RenderTagSupport {

	public final static String RENDER = "render";
	private Map<String, Object> parameters = new HashMap<String, Object>();

	@Override
	protected void render() throws JspException, NirvanaException, IOException {
		container.render(getName(), pageContext, getParameters());
	}

	protected Map<String, Object> getParameters() {
		if (StringUtils.isNoneBlank(getRender())) {
			parameters.put("render", getRender());
		}
		return parameters;
	}

}
