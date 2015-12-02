package com.wxxr.nirvana.context;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class NirvanaServletContext {
	
	public static final String HTTP_REQUEST = "javax.servlet.http.HttpServletRequest";
	
	public static final String HTTP_RESPONSE = "javax.servlet.http.HttpServletResponse";
	
	public static final String HTTP_SESSION = "javax.servlet.http.HttpSession";
	
	public static final String SERVLET_CONTEXT = "javax.servlet.ServletContext";


	static ThreadLocal<NirvanaServletContext> workbenchContext = new ThreadLocal<NirvanaServletContext>();
	
	private Map<String, Object> context;

	public NirvanaServletContext(Map<String, Object> context) {
		this.context = context;
	}

	public static NirvanaServletContext getContext() {
		return workbenchContext.get();
	}
	
	public static void setContext(NirvanaServletContext context) {
		workbenchContext.set(context);
	}
	
	public HttpServletRequest getRequest(){
		return (HttpServletRequest) NirvanaServletContext.getContext().context.get(HTTP_REQUEST);
	}
	
    public static void setRequest(HttpServletRequest request) {
    	NirvanaServletContext.getContext().put(HTTP_REQUEST, request);
    }
	
	private void put(String key, Object value) {
		context.put(key, value);
	}

	public HttpServletResponse getResponse(){
		return (HttpServletResponse) NirvanaServletContext.getContext().context.get(HTTP_RESPONSE);
	}
	
	public static void setResponse(HttpServletResponse response) {
		NirvanaServletContext.getContext().put(HTTP_RESPONSE, response);
    }
	
	public ServletContext getServletContext(){
		return (ServletContext) NirvanaServletContext.getContext().context.get(SERVLET_CONTEXT);
	}
	
	public static void setServletContext(ServletContext servletContext) {
		NirvanaServletContext.getContext().put(SERVLET_CONTEXT, servletContext);
    }
	
	public HttpSession getSession(){
		return (HttpSession) NirvanaServletContext.getContext().context.get(HTTP_SESSION);
	}
	
	public static void setHttpSession(HttpSession session) {
		NirvanaServletContext.getContext().put(HTTP_SESSION, session);
    }
	
    public Object get(String key) {
        return context.get(key);
    }

}
