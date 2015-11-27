package com.wxxr.nirvana.workbench.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wxxr.nirvana.context.NirvanaContext;
import com.wxxr.nirvana.servlet.context.NirvanaContextFactory;

public class WorkbenchContext {
	
	private static ThreadLocal<WorkbenchContext> instance = new ThreadLocal<WorkbenchContext>();
	
	private NirvanaContext nirvanaContext = null;
	
	/**
     * The request object to use.
     */
    private HttpServletRequest request;

    /**
     * The response object to use.
     */
    private HttpServletResponse response;

	
	
	public WorkbenchContext(HttpServletRequest request,
			HttpServletResponse response) {
		super();
		this.request = request;
		this.response = response;
	}


	public NirvanaContext getNirvanaContext(){
		if(nirvanaContext == null){
			nirvanaContext = NirvanaContextFactory.createNirvanaContext(request, response);
		}
		return nirvanaContext;
	}
}
