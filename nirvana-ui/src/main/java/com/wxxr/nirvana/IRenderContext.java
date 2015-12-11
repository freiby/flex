package com.wxxr.nirvana;

import java.util.Map;

import com.wxxr.nirvana.IWebResourceContainer.WebResourceInfo;
import com.wxxr.nirvana.context.IRequestContext;
import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.ui.ResourceUIComponent;
import com.wxxr.nirvana.workbench.IWebResource;
import com.wxxr.nirvana.workbench.impl.UIComponent;

/**
 * 渲染组件按的环境
 * 
 * @author fudapeng
 *
 */
public interface IRenderContext {
	/**
	 * 获取用户请求环境
	 * 
	 * @return
	 */
	IRequestContext getRequestContext();

	/**
	 * 组件的环境
	 * 
	 * @return
	 */
	IUIComponentContext getComponentContext();

	/**
	 * 获取某个组件的资源
	 * 
	 * @param component
	 * @return
	 */
	WebResourceInfo[] getComponentResource(UIComponent component);

	/**
	 * 页面传下来的参数
	 * 
	 * @return
	 */
	Map<String, Object> getParameterMap();

	void render(UIComponent component, IRenderContext context)
			throws NirvanaException;
}
