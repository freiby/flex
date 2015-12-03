package com.wxxr.nirvana;

import com.wxxr.nirvana.context.IRequestContext;

public interface IRenderContext {
	IRequestContext getRequestContext();
	IUIComponentContext getComponentContext();
}
