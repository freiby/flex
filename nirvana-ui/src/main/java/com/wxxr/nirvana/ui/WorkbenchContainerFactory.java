package com.wxxr.nirvana.ui;

import com.wxxr.nirvana.IWorkbenchContainer;

public class WorkbenchContainerFactory {
	public static IWorkbenchContainer createWorkbench() {
		return new WorkbenchContainerImpl();
	}
}
