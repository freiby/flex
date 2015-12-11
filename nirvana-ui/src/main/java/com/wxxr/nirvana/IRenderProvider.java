package com.wxxr.nirvana;

import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.workbench.impl.UIComponent;

public interface IRenderProvider {
	/**
	 * 渲染组件
	 * 
	 * @param component
	 * @param context
	 * @throws NirvanaException
	 */
	void render(UIComponent component, IRenderContext context)
			throws NirvanaException;

	String processComponent();
}
