package com.wxxr.nirvana.workbench.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.workbench.IServiceManager;
import com.wxxr.nirvana.workbench.IWorkbench;

public class WorkbenchBoostrapBefore {

	private Log log = LogFactory.getLog(Workbench.class);

	private Map<String, Object> config = new HashMap<String, Object>();

	private IServiceManager serviceManager;

	private boolean started;
	
	private IWorkbench workbench;
	
	public WorkbenchBoostrapBefore() {
		
	}

	public void start(Map<String, Object> config) throws NirvanaException {
		if (started) return;
		if (config != null)
			this.config = config;

		if (serviceManager == null) {
			serviceManager = new ServiceManager();
		}
//		preproccessResources();
		started = true;
	}
	
	public void destroy() {
	}
	
	public IServiceManager getServiceManager(){
		if(!started){
			throw new IllegalStateException(" WorkbenchBoostrap not started!!");
		}
		return serviceManager;
	}

	
}
