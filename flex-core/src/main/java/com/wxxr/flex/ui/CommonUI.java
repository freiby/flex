package com.wxxr.flex.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.UIBean;

import com.opensymphony.xwork2.util.ValueStack;

public abstract class CommonUI extends UIBean {
	
	public CommonUI(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack,request,response);
    }
}
