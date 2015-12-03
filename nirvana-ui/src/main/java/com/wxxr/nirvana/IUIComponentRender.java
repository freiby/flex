package com.wxxr.nirvana;

import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.workbench.impl.UIComponent;

public interface IUIComponentRender {
	boolean accept(UIComponent component);
	void render(UIComponent component,IRenderContext context) throws NirvanaException ;
}
