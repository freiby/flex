package com.wxxr.nirvana.workbench;

import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.platform.IConfigurationElement;

public interface IActionProxy  extends IContributionItem {
	void init(IConfigurationElement elem);
	void setAction(Object action);
	Object getAction();
	void invoke(String method)  throws NirvanaException;
	void destory();
	
}
