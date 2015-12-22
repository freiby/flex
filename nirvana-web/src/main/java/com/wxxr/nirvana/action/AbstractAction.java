package com.wxxr.nirvana.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.wxxr.nirvana.context.NirvanaServletContext;

public class AbstractAction extends ActionSupport{
	
	protected void setUpContext() {
		Map<String, Object> map = new HashMap<String, Object>();
		NirvanaServletContext context = new NirvanaServletContext(map);
		NirvanaServletContext.setContext(context);
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpSession session = request.getSession();
		NirvanaServletContext.setRequest(request);
		NirvanaServletContext.setResponse(response);
		NirvanaServletContext.setHttpSession(session);
		NirvanaServletContext.setServletContext(session.getServletContext());
	}
}
