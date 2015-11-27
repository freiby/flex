package com.wxxr.nirvana.servlet.context;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.wxxr.nirvana.context.NirvanaContext;

public class NirvanaContextFactory {
	public static NirvanaContext createNirvanaContext(ServletRequest request,ServletResponse response){
		return new NirvanaServletContext(request, response);
	}
}
