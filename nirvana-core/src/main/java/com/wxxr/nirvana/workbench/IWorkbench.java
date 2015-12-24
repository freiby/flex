/*
 * @(#)WebWorkbench.java	 2007-9-27
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.workbench;

import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.theme.ITheme;

/**
 * @author fudapeng
 *
 */
public interface IWorkbench {
	
	void start()  throws NirvanaException;
	IWorkbenchPageManager getWorkbenchPageManager();
	IActionManager getActionManager();
	IProduct getCurrentProduct();
	IWorkbenchPage getCurrentPage();
	
	ITheme getCurrentTheme();
	void destroy();
}
