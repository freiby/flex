package com.wxxr.nirvana.ui;

import java.io.IOException;

import com.wxxr.nirvana.IUIComponentRender;
import com.wxxr.nirvana.context.IRequestContext;
import com.wxxr.nirvana.workbench.IDispatchUI;
import com.wxxr.nirvana.workbench.impl.UIComponent;

public class DispatchRender implements IUIComponentRender{

	public void render(UIComponent component, IRequestContext context) throws IOException {
		IDispatchUI disppach = (IDispatchUI)component;
		String uri = disppach.getURI();
		context.dispatch(uri);
	}

	public boolean accept(UIComponent component) {
		return component instanceof IDispatchUI;
	}

}
