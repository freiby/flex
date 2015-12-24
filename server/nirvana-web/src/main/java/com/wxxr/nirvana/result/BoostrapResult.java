package com.wxxr.nirvana.result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.ServletDispatcherResult;

import com.opensymphony.xwork2.ActionInvocation;
import com.wxxr.nirvana.ContainerAccess;
import com.wxxr.nirvana.IWorkbenchContainer;
import com.wxxr.nirvana.ui.WorkbenchContainerFactory;
import com.wxxr.nirvana.workbench.IWorkbench;
import com.wxxr.nirvana.workbench.impl.Workbench;
import com.wxxr.nirvana.workbench.impl.WorkbenchFactory;

@SuppressWarnings("serial")
public class BoostrapResult extends ServletDispatcherResult {

	private Object lock = new Object();
	public void doExecute(String location, ActionInvocation invocation)
			throws Exception {
		IWorkbenchContainer container = ContainerAccess.getContainer();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		synchronized (lock) {
			if (container == null) {
				IWorkbench workbench = WorkbenchFactory.createWorkbench();
				ContainerAccess.setSessionWorkbench(workbench);
				container = WorkbenchContainerFactory.createWorkbench();
				container.init(request, response);
				ContainerAccess.setContainer(container);
			}
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
