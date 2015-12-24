package com.wxxr.nirvana.inspinia;

import com.wxxr.nirvana.ContainerAccess;
import com.wxxr.nirvana.workbench.IWorkbenchPage;

public class SomethingAction {
	public void addView(){
		IWorkbenchPage page = ContainerAccess.getSessionWorkbench().getCurrentPage();
		page.addView("com.wxxr.nirvana.test.helloWorld");
	}
}
