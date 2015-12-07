package com.wxxr.nirvana.theme;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.workbench.IDispatchUI;
import com.wxxr.nirvana.workbench.IPageLayoutManager;

public interface IPageLayout extends IDispatchUI{
	void destroy();
	void init(IPageLayoutManager manager, IConfigurationElement config);
}
