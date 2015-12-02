package com.wxxr.nirvana.jsp.taglib;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;

import com.wxxr.nirvana.exception.NirvanaException;

public class UITag extends RenderTagSupport {

	

	@Override
	protected void render() throws JspException, NirvanaException, IOException {
		container.render(getName(), pageContext, getParameters());
	}
	
	protected Map<String, Object> getParameters(){
		return null;
	}

}
