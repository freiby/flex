package com.wxxr.nirvana.action;

import org.apache.commons.lang3.StringUtils;

import com.wxxr.nirvana.ContainerAccess;
import com.wxxr.nirvana.context.NirvanaServletContext;
import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.workbench.IActionProxy;
import com.wxxr.nirvana.workbench.IWorkbenchPage;

/**
 * @author fudapeng
 *
 */
public class PageAction extends AbstractAction {
	private String pageName;

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String invokeAction() throws Exception {
		setUpContext();
		IWorkbenchPage currentPage = ContainerAccess.getSessionWorkbench()
				.getCurrentPage();
		if (currentPage == null) {
			throw new NirvanaException("not init product and open page!");
		}
		pageName = currentPage.getName();
		String actionId = NirvanaServletContext.getContext().getRequest()
				.getParameter("actionId");
		String pageId = NirvanaServletContext.getContext().getRequest()
				.getParameter("toPage");
		String method = NirvanaServletContext.getContext().getRequest()
				.getParameter("method");
		IActionProxy proxy = ContainerAccess.getSessionWorkbench()
				.getActionManager().getAction(actionId);
		proxy.invoke(StringUtils.isBlank(method) ? "doAction" :  method);
		if (StringUtils.isNoneBlank(pageId)) {
			IWorkbenchPage toPage = ContainerAccess.getSessionWorkbench()
					.getWorkbenchPageManager().getWorkbenchPage(pageId);
			pageName = toPage.getName();
		}
		return "start";
	}
}
