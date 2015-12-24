package com.wxxr.nirvana.workbench;

public interface IActionManager {
	IActionProxy getAction(String actionId);

	IActionProxy[] getAllActions();

	void destroy();

	void start();
}
