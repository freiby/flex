package com.wxxr.nirvana.result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.ServletDispatcherResult;

import com.opensymphony.xwork2.ActionInvocation;
import com.wxxr.nirvana.ContainerAccess;
import com.wxxr.nirvana.IWorkbenchContainer;

@SuppressWarnings("serial")
public class ProductResult extends ServletDispatcherResult {
	
	public void doExecute(String location, ActionInvocation invocation) throws Exception {
		HttpSession session = ServletActionContext.getRequest().getSession();
		IWorkbenchContainer container = ContainerAccess.getContainer(session);
		HttpServletRequest request = ServletActionContext.getRequest();
	    HttpServletResponse response = ServletActionContext.getResponse();
		container.bootstrap(request, response, location, null);
		setLocation(location);
    }
}
