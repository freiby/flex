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

	public void invokeAction() throws Exception {
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
		IActionProxy proxy = ContainerAccess.getSessionWorkbench()
				.getActionManager().getAction(actionId);
		proxy.invoke("doAction");
		if (StringUtils.isNoneBlank(pageId)) {
			IWorkbenchPage toPage = ContainerAccess.getSessionWorkbench()
					.getWorkbenchPageManager().getWorkbenchPage(pageId);
			pageName = toPage.getName();
			if (toPage != null) {
				ContainerAccess.getContainer().openPage(
						NirvanaServletContext.getContext().getRequest(),
						NirvanaServletContext.getContext().getResponse(),
						toPage.getName());
			}
		}

	}
}
