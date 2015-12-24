package com.wxxr.nirvana;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import com.wxxr.nirvana.exception.NirvanaException;

public class JspTagMock {
	private IWorkbenchContainer container;
	public String component;
	private PageContext context;
	private Map<String, Object> parameters = new HashMap<String, Object>();
	public List<JspTagMock> children = new ArrayList<JspTagMock>();

	public JspTagMock(IWorkbenchContainer container, String component,
			PageContext context, Map<String, Object> parameters) {
		super();
		this.container = container;
		this.component = component;
		this.context = context;
		this.parameters = parameters;
	}

	public void addTag(JspTagMock tag) {
		children.add(tag);
	}

	public void show() throws NirvanaException {
		container.startContext(component, context);
		container.render(component, context, parameters);
		container.endContext(context);
	}
}
