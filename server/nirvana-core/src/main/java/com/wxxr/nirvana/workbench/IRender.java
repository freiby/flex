package com.wxxr.nirvana.workbench;

import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.workbench.impl.UIComponent;

public interface IRender {
	/**
	 * 渲染组件
	 * 
	 * @param component
	 * @param context
	 * @throws NirvanaException
	 */
	void render(UIComponent component,IRenderContext context) throws NirvanaException;

	String getUri();

	public String getContributorId();

	public String getContributorVersion();

	void init(IConfigurationElement config);
}
