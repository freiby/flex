package com.wxxr.nirvana.result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.ServletDispatcherResult;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.wxxr.nirvana.ContainerAccess;
import com.wxxr.nirvana.IWorkbenchContainer;
import com.wxxr.nirvana.ui.WorkbenchContainerFactory;
import com.wxxr.nirvana.ui.WorkbenchProxy;

@SuppressWarnings("serial")
public class BoostrapResult extends ServletDispatcherResult {

	public void doExecute(String location, ActionInvocation invocation)
			throws Exception {
		IWorkbenchContainer container = ContainerAccess.getContainer();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		if (container == null) {
			container = WorkbenchContainerFactory.createWorkbench();
			container.init(request, response);
			WorkbenchProxy workbenchProxy = new WorkbenchProxy(
					ContainerAccess.getWorkbench());
			ContainerAccess.setSessionWorkbench(workbenchProxy);
			ContainerAccess.setContainer(container);
		}
		String page = request.getParameter("page");
		if (!container.isBoostrap()) {
			container.bootstrap(request, response, location, page);
		} else {
			container.openPage(request, response, location);
		}
		setLocation(location);
	}
}
