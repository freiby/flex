package com.wxxr.nirvana.ui.render;

import com.wxxr.nirvana.IRenderProvider;
import com.wxxr.nirvana.IUIComponentContext;
import com.wxxr.nirvana.IUIRenderContext;
import com.wxxr.nirvana.context.IRequestContext;
import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.workbench.IRenderContext;
import com.wxxr.nirvana.workbench.impl.UIComponent;

public abstract class CommonRenderProvider implements IRenderProvider {

	public final void render(UIComponent component, IUIRenderContext context)
			throws NirvanaException {
		if (component.getRender() != null) {
			IRenderContext rcontext = createContext(context);
			component.getRender().render(component,rcontext);
			return;
		}
		doRender(component, context);
	}

	private IRenderContext createContext(final IUIRenderContext context) {
		IRenderContext rcontext = new IRenderContext() {
			public <T> T get(Class<T> type) {
				if(type == IUIRenderContext.class){
					return type.cast(context);
				}
				return null;
			}
		};
		return rcontext;
	}

	protected abstract void doRender(UIComponent component,
			IUIRenderContext context) throws NirvanaException;

}
