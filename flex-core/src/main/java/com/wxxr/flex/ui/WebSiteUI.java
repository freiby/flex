/**
 * 
 */
package com.wxxr.flex.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.views.annotations.StrutsTag;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * @author fudapeng
 * 绘制整个页面
 */
@StrutsTag(
	    name="website",
	    tldTagClass="com.wxxr.flex.tags.WebSiteTag",
	    description="web site",
	    allowDynamicAttributes=true)
public class WebSiteUI extends CommonUI {
	
	final public static String TEMPLATE = "website";
	
	public WebSiteUI(ValueStack stack, HttpServletRequest request,
			HttpServletResponse response) {
		super(stack, request, response);
		
	}
	
	@Override
	public void setDefaultTemplateDir(String dir) {
		super.setDefaultTemplateDir("uitemplate");
	}
	
	@Override
	protected String getDefaultTemplate() {
		return TEMPLATE;
	}

}
