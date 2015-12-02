package com.wxxr.nirvana;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import com.wxxr.nirvana.context.IRequestContext;
import com.wxxr.nirvana.exception.NirvanaException;

public interface IWorkbenchContainer {
	public final static String WORKBENCH_PROXY = "_workbench_proxy_";
	public final static String WORKBENCH_CONTAINER = "_workbench_container_";
	
	
	void init(HttpServletRequest request, HttpServletResponse response) throws NirvanaException;
	void render(String component, PageContext context, Map<String,Object> parameters)throws  NirvanaException;
	void endContext(PageContext context);
	IUIComponentContext startContext(String componentName,PageContext context);
	
	void prepare(String preparer,PageContext context) throws NirvanaException;
	
	void registryUIRender(IUIComponentRender render);
	void unregistryUIRender(IUIComponentRender render);
	
	void bootstrap(String product, HttpServletRequest request, HttpServletResponse response)throws  NirvanaException;
	IUIComponentContext getUIComponentContext(IRequestContext rcontext);
	void destroy();
	
}
