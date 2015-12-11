package com.wxxr.nirvana.workbench;

import com.wxxr.nirvana.exception.NirvanaException;

public interface IRender {
	/**
	 * 渲染组件
	 * 
	 * @param component
	 * @param context
	 * @throws NirvanaException
	 */
	void render() throws NirvanaException;
}
