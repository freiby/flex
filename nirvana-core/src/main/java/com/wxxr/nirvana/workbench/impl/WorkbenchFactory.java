package com.wxxr.nirvana.workbench.impl;

import com.wxxr.nirvana.workbench.IWorkbench;

public class WorkbenchFactory {
	public static IWorkbench createWorkbenchFactory() {
		return new Workbench();
	}
}
