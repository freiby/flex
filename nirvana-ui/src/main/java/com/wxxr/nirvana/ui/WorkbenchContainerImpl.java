package com.wxxr.nirvana.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import com.wxxr.nirvana.ContainerAccess;
import com.wxxr.nirvana.IUIComponentContext;
import com.wxxr.nirvana.IUIComponentRender;
import com.wxxr.nirvana.IWorkbenchContainer;
import com.wxxr.nirvana.context.IRequestContext;
import com.wxxr.nirvana.context.JspRequestContext;
import com.wxxr.nirvana.context.ServletRequestContext;
import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.theme.ITheme;
import com.wxxr.nirvana.workbench.IProduct;
import com.wxxr.nirvana.workbench.IWorkbench;
import com.wxxr.nirvana.workbench.impl.UIComponent;

public class WorkbenchContainerImpl implements IWorkbenchContainer {

	private List<IUIComponentRender> renders = new ArrayList<IUIComponentRender>();
	private WorkbenchProxy workbenchProxy = null;
	private static final String ATTRIBUTE_CONTEXT_STACK =
	        "org.apache.tiles.AttributeContext.STACK";
	
	
	private boolean initialization = false;
	
	public void init(HttpServletRequest request, HttpServletResponse response) throws NirvanaException{
		getRequestContext(request,response).getSessionScope().put(WORKBENCH_CONTAINER, this);
		
		IWorkbench workbench = ContainerAccess.getWorkbench();
		if(workbench == null){
			throw new NirvanaException("workbench is not init");
		}
		workbenchProxy = new WorkbenchProxy(workbench);
		getRequestContext(request,response).getSessionScope().put(WORKBENCH_PROXY, workbenchProxy);
	}
	
	public IUIComponentContext getUIComponentContext(IRequestContext rcontext) {
		IUIComponentContext context = getContext(rcontext);
        return context;

    }
	
	
	protected IUIComponentContext getContext(IRequestContext tilesContext) {
        Stack<IUIComponentContext> contextStack = getContextStack(tilesContext);
        if (!contextStack.isEmpty()) {
            return contextStack.peek();
        } else {
            return null;
        }
    }
	
	protected Stack<IUIComponentContext> getContextStack(IRequestContext context) {
        @SuppressWarnings("unchecked")
		Stack<IUIComponentContext> contextStack =
            (Stack<IUIComponentContext>) context
                .getRequestScope().get(ATTRIBUTE_CONTEXT_STACK);
        if (contextStack == null) {
            contextStack = new Stack<IUIComponentContext>();
            context.getRequestScope().put(ATTRIBUTE_CONTEXT_STACK,
                    contextStack);
        }

        return contextStack;
    }

	public IUIComponentContext startContext(String componentName, PageContext context) {
		IRequestContext rcontext = getRequestContext(context);
		IUIComponentContext pContext = getContext(rcontext);
		IUIComponentContext uicontext = pContext.getChildUIContext(componentName);
        pushContext(uicontext, rcontext);
		return uicontext;
	}
	
	protected void pushContext(IUIComponentContext context,
            IRequestContext rcontext) {
        Stack<IUIComponentContext> contextStack = getContextStack(rcontext);
        contextStack.push(context);
    }

	public void endContext(PageContext context) {
	}

	public void prepare(String preparer, PageContext context)
			throws NirvanaException {
	}
	
	public void render(String component, PageContext context, Map<String,Object> parameters) throws  NirvanaException{
		if(!initialization){
			throw new NirvanaException("workbench not bootstrap");
		}
		IRequestContext rc = getRequestContext(context);
		IUIComponentContext pContext = getContext(rc);
		UIComponent uicomponent = pContext.getCurrentComponent(parameters);
		for(IUIComponentRender render : renders){
			if(render.accept(uicomponent)){
				try {
					render.render(uicomponent, rc);
				} catch (IOException e) {
					throw new NirvanaException("render error: " , e);
				}
			}
		}
	}

	public void registryUIRender(IUIComponentRender render) {
		if(!renders.contains(render)){
			renders.add(render);
		}
	}

	public void unregistryUIRender(IUIComponentRender render) {
		if(renders.contains(render)){
			renders.remove(render);
		}
	}

	public void bootstrap(String p, HttpServletRequest request, HttpServletResponse response)throws  NirvanaException{
		if(initialization) return;
		IProduct product = workbenchProxy.getCurrentProduct();
		if(product == null){
			product = workbenchProxy.getProductManager().getProductById(p);
			workbenchProxy.setCurrentProduct(product);
		}
		
		if(product == null){
			throw new NirvanaException("workbench has not product that can visit");
		}
		
		String themeref = workbenchProxy.getCurrentProduct().getTheme();
		ITheme theme = workbenchProxy.getThemeManager().getTheme(themeref);
		
		ProductContext rootcontext = new ProductContext(product);
		IRequestContext rcontext = getRequestContext(request,response);
		rootcontext.init(null, rcontext);
		
		pushContext(rootcontext, rcontext);
		initialization = true;
	}
	
	/**
     *
     * @param requestItems The request items.
     * @return The created Tiles request context.
     */
    private IRequestContext getRequestContext(PageContext context) {
        return new JspRequestContext(context.getServletContext(), context); 
    }
    
    private IRequestContext getRequestContext(HttpServletRequest request, HttpServletResponse response) {
        return new ServletRequestContext(request.getSession().getServletContext(), request, response); 
    }
    
	public void destroy() {
		
	}
}
