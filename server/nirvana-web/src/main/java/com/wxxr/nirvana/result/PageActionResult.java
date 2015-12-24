package com.wxxr.nirvana.result;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.dispatcher.ServletDispatcherResult;

import com.opensymphony.xwork2.ActionInvocation;
import com.wxxr.nirvana.ContainerAccess;
import com.wxxr.nirvana.context.NirvanaServletContext;

@SuppressWarnings("serial")
public class PageActionResult extends ServletDispatcherResult {

	public void doExecute(String location, ActionInvocation invocation)
			throws Exception {
		if (StringUtils.isNoneBlank(location)) {
				ContainerAccess.getContainer().openPage(
						NirvanaServletContext.getContext().getRequest(),
						NirvanaServletContext.getContext().getResponse(),
						location);
		}
	}
}
