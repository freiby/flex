package com.wxxr.nirvana.ui.render;

import com.wxxr.nirvana.IRenderContext;
import com.wxxr.nirvana.ui.UIComponentRender;
import com.wxxr.nirvana.workbench.impl.UIComponent;

public class ViewRender extends UIComponentRender {

	public boolean accept(UIComponent component) {
		return false;
	}

	@Override
	protected void doRenderComponent(UIComponent component,
			IRenderContext context) {
		
	}

}
