package com.wxxr.nirvana;

import com.wxxr.nirvana.workbench.IProduct;
import com.wxxr.nirvana.workbench.IWorkbench;
import com.wxxr.nirvana.workbench.IWorkbenchPage;

public interface ISessionWorkbench extends IWorkbench {
	IProduct getCurrentProduct();

	IWorkbenchPage getCurrentPage();
}
