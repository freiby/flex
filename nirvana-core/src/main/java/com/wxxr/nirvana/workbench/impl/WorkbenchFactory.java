package com.wxxr.nirvana.workbench.impl;

import java.util.Map;

import com.wxxr.nirvana.workbench.IWorkbench;

public class WorkbenchFactory {
	public static IWorkbench createWorkbenchFactory(Map<String,Object> conf) {
		return new Workbench(conf);
	}
}
