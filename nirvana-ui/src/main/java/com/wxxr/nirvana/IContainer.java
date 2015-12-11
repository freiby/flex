package com.wxxr.nirvana;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wxxr.nirvana.exception.NirvanaException;

public interface IContainer {
	/**
	 * 生命周期方法
	 * 
	 * @param request
	 * @param response
	 * @throws NirvanaException
	 */
	void init(HttpServletRequest request, HttpServletResponse response)
			throws NirvanaException;

	/**
	 * 重启container
	 * 
	 * @param request
	 * @param response
	 * @throws NirvanaException
	 */
	void reset(HttpServletRequest request, HttpServletResponse response)
			throws NirvanaException;

	/**
	 * 销毁容器， 生命周期方法
	 */
	void destroy();
}
