package com.wxxr.nirvana.ui;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.nirvana.IRenderContext;
import com.wxxr.nirvana.IUIComponentRender;
import com.wxxr.nirvana.IWebResourceContainer;
import com.wxxr.nirvana.IWebResourceContainer.WebResourceInfo;
import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.workbench.IWebResource;
import com.wxxr.nirvana.workbench.impl.UIComponent;

public abstract class UIComponentRender implements IUIComponentRender {

	public void render(UIComponent component, IRenderContext context)
			throws NirvanaException {
		WebResourceInfo[] wrs = context.getComponentResource(component);

		IWebResource[] bwrs = injecPoint(wrs,true);
		if (bwrs != null && bwrs.length > 0) {
			ResourceUIComponent resourceui = new ResourceUIComponent(bwrs){
				@Override
				public String getName() {
					return "view nest Resource component for inject resource";
				}
			};
			context.render(resourceui, context);
		}
		
		doRenderComponent(component, context);
		
		IWebResource[] afwrs = injecPoint(wrs,false);
		if (afwrs != null && afwrs.length > 0) {
			ResourceUIComponent resourceui = new ResourceUIComponent(afwrs){
				@Override
				public String getName() {
					return "view nest Resource component for inject resource";
				}
			};
			context.render(resourceui, context);
		}
		
	}

	protected abstract void doRenderComponent(UIComponent component, IRenderContext context)throws NirvanaException;

	private IWebResource[] injecPoint(WebResourceInfo[] wrs,boolean before) {
		if(wrs == null) return null;
		List<IWebResource> b = new ArrayList<IWebResource>();
		List<IWebResource> a = new ArrayList<IWebResource>();
		for(WebResourceInfo r : wrs){
			if(r.getPoint().equals(IWebResourceContainer.PREFIX_BEFORE) && before){
				b.add(r.getR());
			}else{
				a.add(r.getR());
			}
		}
		if(before){
			return b.toArray(new IWebResource[b.size()]);
		}else{
			return a.toArray(new IWebResource[a.size()]);
		}
		
	}
}
