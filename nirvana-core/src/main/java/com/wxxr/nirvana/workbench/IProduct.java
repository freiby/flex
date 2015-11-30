package com.wxxr.nirvana.workbench;

import com.wxxr.nirvana.platform.IConfigurationElement;

public interface IProduct extends IContributionItem{
	String getTheme();
	String getDefaultPage();
	String[] getAllPages();
	String getName();
	void init(IProductManager manager, IConfigurationElement config);
	void destroy();
}
