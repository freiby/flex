package com.wxxr.nirvana.context;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * servlet的上下文，不保证session的线程安全。
 * 
 * @author fudapeng
 *
 */
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

	public HttpServletRequest getRequest() {
		return (HttpServletRequest) getContext().context.get(HTTP_REQUEST);
	}

	public static void setRequest(HttpServletRequest request) {
		getContext().put(HTTP_REQUEST, request);
	}

	private void put(String key, Object value) {
		context.put(key, value);
	}

	public HttpServletResponse getResponse() {
		return (HttpServletResponse) getContext().context.get(HTTP_RESPONSE);
	}

	public static void setResponse(HttpServletResponse response) {
		NirvanaServletContext.getContext().put(HTTP_RESPONSE, response);
	}

	public ServletContext getServletContext() {
		return (ServletContext) getContext().context.get(SERVLET_CONTEXT);
	}

	public static void setServletContext(ServletContext servletContext) {
		getContext().put(SERVLET_CONTEXT, servletContext);
	}

	public HttpSession getSession() {
		return (HttpSession) getContext().context.get(HTTP_SESSION);
	}

	public static void setHttpSession(HttpSession session) {
		getContext().put(HTTP_SESSION, session);
	}

	public Object get(String key) {
		return context.get(key);
	}

}
