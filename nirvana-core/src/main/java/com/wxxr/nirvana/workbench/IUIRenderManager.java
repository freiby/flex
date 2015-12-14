package com.wxxr.nirvana.workbench;


public interface IUIRenderManager {
	public IRender find(String id);
	void start();
	void destroy();
}
