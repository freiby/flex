package com.wxxr.nirvana.workbench.impl;

import com.wxxr.nirvana.context.NirvanaContext;
import com.wxxr.nirvana.platform.IPlatform;
import com.wxxr.nirvana.theme.IThemeManager;
import com.wxxr.nirvana.workbench.IPermissionsManager;
import com.wxxr.nirvana.workbench.ISecurityManager;
import com.wxxr.nirvana.workbench.IWorkbench;
import com.wxxr.nirvana.workbench.IWorkbenchManager;
import com.wxxr.nirvana.workbench.IWorkbenchPageManager;

public class WorkbenchManagerImpl implements IWorkbenchManager {

	public IWorkbench getCurrentWorkbench() {
		IWorkbench workbench = (IWorkbench)nirvanaContext.getSessionValue(WORKBENCH_KEY);
		if(workbench == null){
			workbench = createWorkbench();
			nirvanaContext.setSessionValue(WORKBENCH_KEY, workbench);
		}
		return workbench;
	}

	private IWorkbench createWorkbench() {
		return new WorkbenchImpl();
	}

	public IPlatform getUIPlatform() {

		return null;
	}

	public String getDefaultPageId() {

		return null;
	}

	public String getDefaultThemeId() {

		return null;
	}

}
