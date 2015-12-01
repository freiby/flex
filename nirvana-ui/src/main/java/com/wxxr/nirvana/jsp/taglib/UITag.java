package com.wxxr.nirvana.jsp.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import com.wxxr.nirvana.exception.NirvanaException;

public class UITag extends RenderTagSupport {

	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	protected void render() throws JspException, NirvanaException, IOException {
		container.render(name, pageContext);
	}

}
