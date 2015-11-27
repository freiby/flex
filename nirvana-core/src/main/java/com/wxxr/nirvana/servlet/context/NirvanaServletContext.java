package com.wxxr.nirvana.servlet.context;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wxxr.nirvana.context.NirvanaContext;

public class NirvanaServletContext implements NirvanaContext{
	 /**
     * The request object to use.
     */
    private HttpServletRequest request;

    /**
     * The response object to use.
     */
    private HttpServletResponse response;
	

	public NirvanaServletContext(ServletRequest request,
			ServletResponse response) {
		super();
		this.request = (HttpServletRequest) request;
		this.response = (HttpServletResponse) response;
	}

	public Object getApplicationValue(String key) {
		return request.getSession().getServletContext().getAttribute(key);
	}

	public String getHeaderValue(String key) {
		
		return request.getHeader(key);
	}

	public Object getParameterValue(String key) {
		
		return request.getParameter(key);
	}

	public Object getSessionValue(String key) {
		
		return request.getSession().getAttribute(key);
	}

	public void setAppicationValue(String key, Object value) {
		request.getSession().getServletContext().setAttribute(key, value);
	}

	public void setSessionValue(String key, Object value) {
		request.getSession().setAttribute(key, value);
	}

	public void setHeaderValue(String key, String value) {
		response.setHeader(key, value);
	}

}
