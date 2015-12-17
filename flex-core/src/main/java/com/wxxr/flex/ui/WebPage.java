package com.wxxr.flex.ui;

import java.io.IOException;
import java.io.Writer;

import org.apache.struts2.views.annotations.StrutsTag;

import com.opensymphony.xwork2.util.ValueStack;

@StrutsTag(name="webpage", tldTagClass="com.wxxr.flex.tags.WebPageTag", description="Execute an action from within a view")
public class WebPage extends CommonBean {

	public WebPage(ValueStack stack) {
		super(stack);
	}
	
	@Override
	public boolean start(Writer writer) {
		try {
			writer.write("<div>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	@Override
	public boolean end(Writer writer, String body) {
		super.end(writer, body);
		try {
			writer.write("</div>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

}
