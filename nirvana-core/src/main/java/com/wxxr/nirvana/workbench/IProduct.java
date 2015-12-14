package com.wxxr.nirvana.workbench;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.workbench.impl.Product.PageRef;

public interface IProduct extends IContributionItem {
	String getTheme();

	String getDefaultPage();

	PageRef[] getAllPages();

	String getName();
	
	String getNavigationRef();

	void init(IProductManager manager, IConfigurationElement config);

	void destroy();
}
