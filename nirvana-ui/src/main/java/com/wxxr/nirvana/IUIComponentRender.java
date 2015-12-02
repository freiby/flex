package com.wxxr.nirvana;

import java.io.IOException;

import com.wxxr.nirvana.context.IRequestContext;
import com.wxxr.nirvana.workbench.impl.UIComponent;

public interface IUIComponentRender {
	boolean accept(UIComponent component);
	void render(UIComponent component,IRequestContext context) throws IOException ;
}
