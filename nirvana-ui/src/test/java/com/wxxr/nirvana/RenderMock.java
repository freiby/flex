package com.wxxr.nirvana;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.workbench.impl.UIComponent;

public class RenderMock implements IRenderProvider {

	private InvokeContext invokeContext;
	private Log log = LogFactory.getLog(RenderMock.class);

	public RenderMock(InvokeContext invokeContext) {
		super();
		this.invokeContext = invokeContext;
	}

	public boolean accept(UIComponent component) {
		return true;
	}

	public void render(UIComponent component, IRenderContext context)
			throws NirvanaException {
		if (component == null) {
			log.info(" rendering " + null);
		}
		log.info(" === rendering " + component.getName() + " ===");
		for (JspTagMock child : invokeContext.getCurrentNode().children) {
			try {
				invokeContext.setCurrentNode(child);
			} catch (NirvanaException e) {
				e.printStackTrace();
			}
		}
	}

	public String processComponent() {
		return "*";
	}

}
