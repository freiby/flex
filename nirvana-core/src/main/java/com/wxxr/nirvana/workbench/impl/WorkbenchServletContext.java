package com.wxxr.nirvana.workbench.impl;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class WorkbenchServletContext {
	
	public static final String HTTP_REQUEST = "javax.servlet.http.HttpServletRequest";
	
	public static final String HTTP_RESPONSE = "javax.servlet.http.HttpServletResponse";
	
	public static final String HTTP_SESSION = "javax.servlet.http.HttpSession";
	
	public static final String SERVLET_CONTEXT = "javax.servlet.ServletContext";


	static ThreadLocal<WorkbenchServletContext> workbenchContext = new ThreadLocal<WorkbenchServletContext>();
	
	private Map<String, Object> context;

	public WorkbenchServletContext() {
		super();
	}

	public static WorkbenchServletContext getContext() {
		return workbenchContext.get();
	}
	
	public static void setContext(WorkbenchServletContext context) {
		workbenchContext.set(context);
	}
	
	public HttpServletRequest getRequest(){
		return (HttpServletRequest) WorkbenchServletContext.getContext().context.get(HTTP_REQUEST);
	}
	
    public static void setRequest(HttpServletRequest request) {
    	WorkbenchServletContext.getContext().put(HTTP_REQUEST, request);
    }
	
	private void put(String key, Object value) {
		context.put(key, value);
	}

	public HttpServletResponse getResponse(){
		return (HttpServletResponse) WorkbenchServletContext.getContext().context.get(HTTP_RESPONSE);
	}
	
	public static void setResponse(HttpServletResponse response) {
		WorkbenchServletContext.getContext().put(HTTP_RESPONSE, response);
    }
	
	public ServletContext getServletContext(){
		return (ServletContext) WorkbenchServletContext.getContext().context.get(SERVLET_CONTEXT);
	}
	
	public static void setServletContext(ServletContext servletContext) {
		WorkbenchServletContext.getContext().put(SERVLET_CONTEXT, servletContext);
    }
	
	public HttpSession getSession(){
		return (HttpSession) WorkbenchServletContext.getContext().context.get(HTTP_SESSION);
	}
	
	public static void setHttpSession(HttpSession session) {
		WorkbenchServletContext.getContext().put(HTTP_SESSION, session);
    }
	
    public Object get(String key) {
        return context.get(key);
    }

}
