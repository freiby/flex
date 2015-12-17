package com.wxxr.flex.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;

import com.opensymphony.xwork2.util.ValueStack;
import com.wxxr.flex.ui.WebSiteUI;

/**
 * 
 * @author fudapeng
 *
 */
public class WebSiteTag extends CommonUITag {

	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req,
			HttpServletResponse res) {
		return new WebSiteUI(stack, req, res);
	}

}
