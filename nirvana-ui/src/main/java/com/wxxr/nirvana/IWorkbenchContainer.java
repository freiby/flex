package com.wxxr.nirvana;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import com.wxxr.nirvana.context.IRequestContext;
import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.workbench.IWebResource;

/**
 * 渲染用户定义的工作平台的，容器。
 * 
 * TODO 是否需要在渲染某个组件之前做个拦截器，比如注入一些js css 一些资源
 * @author fudapeng
 *
 */
public interface IWorkbenchContainer extends IContainer{
	
	/**
	 * 当用户要访问 某个产品 首先启动某个产品
	 * @param request
	 * @param response
	 * @param product
	 * @param page
	 * @throws NirvanaException
	 */
	void bootstrap(HttpServletRequest request, HttpServletResponse response,String product, String page)throws  NirvanaException;
	
	/**
	 * 打开某个页面
	 * open a page 
	 * @param pagename
	 * @throws NirvanaException
	 */
	public void openPage(HttpServletRequest request, HttpServletResponse response, String ...pagename) throws NirvanaException;
	
	/**
	 * 为要渲染的组件的建立一个环境
	 * @param componentName
	 * @param context
	 * @return
	 */
	IUIComponentContext startContext(String componentName,PageContext context);
	
	/**
	 * 渲染之前，可以做些准备，以后再实现 
	 * @param preparer
	 * @param context
	 * @throws NirvanaException
	 */
	void prepare(String preparer,PageContext context) throws NirvanaException;
	
	/**
	 * 渲染指定的组件
	 * @param component
	 * @param context
	 * @param parameters
	 * @throws NirvanaException
	 */
	void render(String component, PageContext context, Map<String,Object> parameters)throws  NirvanaException;
	
	/**
	 * 当渲染某个组件结束吧他的环境从堆栈中去掉，渲染组件结束
	 * @param context
	 */
	void endContext(PageContext context);
	
	/**
	 * 注册渲染组件
	 * @param render
	 */
	void registryUIRender(IUIComponentRender render);
	
	/**
	 * 注销渲染组件
	 * @param render
	 */
	void unregistryUIRender(IUIComponentRender render);
	
	/**
	 * 获取资源容器，资源容器依赖workbenchContaiter存在
	 * @return
	 */
	IWebResourceContainer getWebResourceContainer();

}
