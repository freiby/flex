package com.wxxr.nirvana;

import com.wxxr.nirvana.theme.IPageLayout;

/**
 * 扩展的环境，page 需要知道page的layout是哪个
 * 
 * @author fudapeng
 *
 */
public interface IPageContext extends IUIComponentContext {
	IPageLayout getPageLayout();
}
