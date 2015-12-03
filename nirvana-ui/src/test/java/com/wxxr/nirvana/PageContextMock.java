package com.wxxr.nirvana;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;

public class PageContextMock extends PageContext {

	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	private ServletContext servletContext;
	private Servlet servelt;
	
	
	
	public PageContextMock(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			ServletContext servletContext) {
		super();
		this.request = request;
		this.response = response;
		this.session = session;
		this.servletContext = servletContext;
	}

	@Override
	public void forward(String arg0) throws ServletException, IOException {
		request.getRequestDispatcher(arg0).forward(request, response);
	}

	@Override
	public Exception getException() {
		return null;
	}

	@Override
	public Object getPage() {
		return null;
	}

	@Override
	public ServletRequest getRequest() {
		return request;
	}

	@Override
	public ServletResponse getResponse() {
		return response;
	}

	@Override
	public ServletConfig getServletConfig() {
		return servelt.getServletConfig();
	}

	@Override
	public ServletContext getServletContext() {
		return servletContext;
	}

	@Override
	public HttpSession getSession() {
		return session;
	}

	@Override
	public void handlePageException(Exception arg0) throws ServletException,
			IOException {
	}

	@Override
	public void handlePageException(Throwable arg0) throws ServletException,
			IOException {
	}

	@Override
	public void include(String arg0) throws ServletException, IOException {
		request.getRequestDispatcher(arg0).include(request, response);
	}

	@Override
	public void include(String arg0, boolean arg1) throws ServletException,
			IOException {
		request.getRequestDispatcher(arg0).include(request, response);
		if(arg1){
			response.getOutputStream().flush();
		}
	}

	@Override
	public void initialize(Servlet arg0, ServletRequest arg1,
			ServletResponse arg2, String arg3, boolean arg4, int arg5,
			boolean arg6) throws IOException, IllegalStateException,
			IllegalArgumentException {
	}

	@Override
	public void release() {
		
	}

	@Override
	public Object findAttribute(String arg0) {
		return null;
	}

	@Override
	public Object getAttribute(String arg0) {
		return null;
	}

	@Override
	public Object getAttribute(String arg0, int arg1) {
		return null;
	}

	@Override
	public Enumeration getAttributeNamesInScope(int arg0) {
		return null;
	}

	@Override
	public int getAttributesScope(String arg0) {
		
		return 0;
	}

	@Override
	public ExpressionEvaluator getExpressionEvaluator() {
		return null;
	}

	@Override
	public JspWriter getOut() {
		
		return null;
	}

	@Override
	public VariableResolver getVariableResolver() {
		
		return null;
	}

	@Override
	public void removeAttribute(String arg0) {
	}

	@Override
	public void removeAttribute(String arg0, int arg1) {
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
	}

	@Override
	public void setAttribute(String arg0, Object arg1, int arg2) {
	}

}
