package com.wxxr.nirvana.jsp.taglib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang3.StringUtils;

import com.wxxr.nirvana.ContainerAccess;
import com.wxxr.nirvana.IWebResourceContainer;
import com.wxxr.nirvana.IWorkbenchContainer;
import com.wxxr.nirvana.workbench.IWebResource;

public abstract class ResourceTag extends BodyTagSupport {
	private IWebResourceContainer webResourceContainer;
	private IWorkbenchContainer workbenchContainer;

	private String point;
	private List<IWebResource> rs = new ArrayList<IWebResource>();

	@Override
	public int doStartTag() throws JspException {
		workbenchContainer = ContainerAccess.getContainer();
		webResourceContainer = workbenchContainer.getWebResourceContainer();
		IWebResource[] resources = webResourceContainer.getResources(point);
		if (resources == null || resources.length == 0)
			return EVAL_BODY_BUFFERED;
		for (IWebResource item : resources) {
			rs.add(item);
		}
		return EVAL_BODY_BUFFERED;
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			for (IWebResource item : getResources()) {
				JspWriter writer = pageContext.getOut();
				String script = getScript(item);
				if (StringUtils.isNoneBlank(script)) {
					writer.append(script);
				}

			}
		} catch (IOException e) {
			new JspException("write resource tag error ", e);
		}
		return super.doEndTag();
	}

	protected abstract String getScript(IWebResource item) throws IOException;

	protected List<IWebResource> getResources() {
		return rs;
	}

	protected String getScriptVar() {
		return "var";
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	// /**
	// * Implementation of <code>TagExtraInfo</code> which identifies the
	// * scripting object(s) to be made visible.
	// */
	// public static class Tei extends TagExtraInfo {
	//
	// /** {@inheritDoc} */
	// @Override
	// public VariableInfo[] getVariableInfo(TagData data) {
	// String classname = data.getAttributeString("classname");
	// if (classname == null) {
	// classname = "java.util.ArrayList";
	// }
	// String var = data.getAttributeString("var");
	//
	// return new VariableInfo[] { new VariableInfo(var, classname, true,
	// VariableInfo.AT_END) };
	//
	// }
	// }
}
