package com.wxxr.nirvana;

import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.workbench.impl.UIComponent;

/**
 * 渲染组件接口
 * @author fudapeng
 *
 */
public interface IUIComponentRender {
	/**
	 * 首先看看能不能渲染这个组件
	 * @param component
	 * @return
	 */
	boolean accept(UIComponent component);
	
	/**
	 * 渲染组件
	 * @param component
	 * @param context
	 * @throws NirvanaException
	 */
	void render(UIComponent component,IRenderContext context)throws NirvanaException;
}
