package com.wxxr.nirvana.ui.render;

import java.io.IOException;

import com.wxxr.nirvana.IRenderContext;
import com.wxxr.nirvana.IRenderProvider;
import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.workbench.IDispatchUI;
import com.wxxr.nirvana.workbench.impl.UIComponent;

public abstract class DispatchRender implements IRenderProvider{

	public void render(UIComponent component, IRenderContext context) throws NirvanaException {
		IDispatchUI disppach = (IDispatchUI)component;
		String uri = disppach.getURI();
		try {
			context.getRequestContext().dispatch(uri);
		} catch (IOException e) {
			throw new NirvanaException(e);
		}
	}

	public String processComponent() {
		return null;
	}


}
