package com.wxxr.nirvana.ui.render;

import com.wxxr.nirvana.IRenderContext;
import com.wxxr.nirvana.IUIComponentRender;
import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.ui.ResourceUIComponent;
import com.wxxr.nirvana.workbench.IWebResource;
import com.wxxr.nirvana.workbench.impl.UIComponent;

public class InjectResourceRender implements IUIComponentRender {

	public boolean accept(UIComponent component) {
		return component instanceof ResourceUIComponent;
	}

	public void render(UIComponent component, IRenderContext context)
			throws NirvanaException {
		ResourceUIComponent r = (ResourceUIComponent)component;
		IWebResource[] webrs = r.getResorces();
	}

}
