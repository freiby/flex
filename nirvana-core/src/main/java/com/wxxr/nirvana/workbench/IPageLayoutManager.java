package com.wxxr.nirvana.workbench;

import com.wxxr.nirvana.theme.IPageLayout;

public interface IPageLayoutManager {
	IPageLayout getPageLayout(String id);
	IPageLayout[] getAllPageLayout();
	void start();
	void destroy();
}
