package com.wxxr.nirvana.ui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import com.wxxr.nirvana.ContainerAccess;
import com.wxxr.nirvana.IRenderContext;
import com.wxxr.nirvana.IRenderProvider;
import com.wxxr.nirvana.IUIComponentContext;
import com.wxxr.nirvana.IWebResourceContainer;
import com.wxxr.nirvana.IWebResourceContainer.WebResourceInfo;
import com.wxxr.nirvana.IWorkbenchContainer;
import com.wxxr.nirvana.context.IRequestContext;
import com.wxxr.nirvana.context.JspRequestContext;
import com.wxxr.nirvana.context.NirvanaServletContext;
import com.wxxr.nirvana.context.ServletRequestContext;
import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.workbench.IProduct;
import com.wxxr.nirvana.workbench.IWorkbench;
import com.wxxr.nirvana.workbench.IWorkbenchPage;
import com.wxxr.nirvana.workbench.impl.Product.PageRef;
import com.wxxr.nirvana.workbench.impl.UIComponent;

public class WorkbenchContainerImpl implements IWorkbenchContainer {

	// private List<IUIComponentRender> renders = new
	// ArrayList<IUIComponentRender>();
	private static final String ATTRIBUTE_CONTEXT_STACK = "org.apache.tiles.AttributeContext.STACK";
	private boolean initialization = false;

	private IWebResourceContainer resourceContainer;
	private IRenderProvider renderProvider;
	protected Map<String, IRenderProvider> renderProviders = new HashMap<String, IRenderProvider>();

	public void init(HttpServletRequest request, HttpServletResponse response)
			throws NirvanaException {
		IWorkbench workbench = ContainerAccess.getWorkbench();
		if (workbench == null) {
			throw new NirvanaException("workbench is not init");
		}
		initProvider();
	}

	protected void initProvider() {
		ServiceLoader<IRenderProvider> services = ServiceLoader
				.load(IRenderProvider.class);
		if (services != null) {
			for (IRenderProvider service : services) {
				renderProviders.put(service.processComponent(), service);
			}
		}
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
		Stack<IUIComponentContext> contextStack = (Stack<IUIComponentContext>) NirvanaServletContext
				.getContext().getRequest()
				.getAttribute(ATTRIBUTE_CONTEXT_STACK);
		if (contextStack == null) {
			contextStack = new Stack<IUIComponentContext>();
			NirvanaServletContext.getContext().getRequest()
					.setAttribute(ATTRIBUTE_CONTEXT_STACK, contextStack);
		}

		return contextStack;
	}

	public IUIComponentContext startContext(String componentName,
			PageContext context) {
		IRequestContext rcontext = getRequestContext(context);
		IUIComponentContext pContext = getContext(rcontext);
		IUIComponentContext uicontext = pContext
				.getChildUIContext(componentName);
		pushContext(uicontext, rcontext);
		return uicontext;
	}

	protected void pushContext(IUIComponentContext context,
			IRequestContext rcontext) {
		Stack<IUIComponentContext> contextStack = getContextStack(rcontext);
		contextStack.push(context);
	}

	protected IUIComponentContext popContext(IRequestContext context) {
		Stack<IUIComponentContext> contextStack = getContextStack(context);
		return contextStack.pop();
	}

	public void endContext(PageContext context) {
		IRequestContext rcontext = getRequestContext(context);
		popContext(rcontext);
	}

	public void prepare(String preparer, PageContext context)
			throws NirvanaException {
	}

	public void render(String component, PageContext context,
			final Map<String, Object> parameters) throws NirvanaException {
		if (!initialization) {
			throw new NirvanaException("workbench not bootstrap");
		}
		final IRequestContext rc = getRequestContext(context);
		final IUIComponentContext pContext = getContext(rc);
		UIComponent uicomponent = pContext.getCurrentComponent(parameters);
		if (renderProviders.containsKey(component)
				|| renderProviders.containsKey("*")) {
			IRenderProvider render = renderProviders.get(component);
			if (render == null) {
				render = renderProviders.get("*");
			}
			final IRenderContext renderContext = new IRenderContext() {
				public IRequestContext getRequestContext() {
					return rc;
				}

				public IUIComponentContext getComponentContext() {
					return pContext;
				}

				public WebResourceInfo[] getComponentResource(
						UIComponent component) {
					return resourceContainer.getComponentResource(component);
				}

				public void render(UIComponent component, IRenderContext context)
						throws NirvanaException {
					if (renderProviders.containsKey(component.getName())) {
						renderProviders.get(component.getName()).render(
								component, context);
					}
				}

				public Map<String, Object> getParameterMap() {
					return parameters;
				}
			};
			render.render(uicomponent, renderContext);
		}
	}

	public void bootstrap(HttpServletRequest request,
			HttpServletResponse response, String p, String page)
			throws NirvanaException {
		if (initialization)
			return;
		WorkbenchProxy workbenchProxy = (WorkbenchProxy) ContainerAccess
				.getSessionWorkbench();
		IProduct product = workbenchProxy.getCurrentProduct();
		if (product == null) {
			product = workbenchProxy.getProductManager().getProductByName(p);
			workbenchProxy.setCurrentProduct(product);
		}
		if (product == null) {
			throw new NirvanaException(
					"workbench has not product that can visit");
		}
		workbenchProxy.setCurrentProduct(product);
		if (page == null) {
			page = product.getDefaultPage();
		}
		setupProductContext(product, request, response);
		initialization = true;
		openPage(request, response, page);
		afterBootstrop(request, response);
	}

	private void setupProductContext(IProduct product,
			HttpServletRequest request, HttpServletResponse response) {
		ProductContext rootcontext = new ProductContext(product);
		IRequestContext rcontext = getRequestContext(request, response);
		rootcontext.init(null);
		pushContext(rootcontext, rcontext);

		IUIComponentContext themeContext = rootcontext
				.getChildUIContext("theme");
		rootcontext.init(null);
		pushContext(themeContext, rcontext);
	}

	protected void afterBootstrop(HttpServletRequest request,
			HttpServletResponse response) throws NirvanaException {

	}

	public void openPage(HttpServletRequest request,
			HttpServletResponse response, String... pagename)
			throws NirvanaException {
		if (pagename.length > 1)
			throw new NirvanaException("open one page");
		try {
			WorkbenchProxy workbenchProxy = (WorkbenchProxy) ContainerAccess
					.getSessionWorkbench();
			if (workbenchProxy.getProductManager() == null) {
				throw new NirvanaException("please boostrap product");
			}
			setupProductContext(workbenchProxy.getCurrentProduct(), request,
					response);
			PageRef[] pages = workbenchProxy.getCurrentProduct().getAllPages();
			PageRef defaultPage = null;
			for (PageRef p : pages) {
				if (p.defaultPage) {
					defaultPage = p;
				}
				if (pagename.length == 1) {
					IWorkbenchPage page = workbenchProxy
							.getWorkbenchPageManager().getWorkbenchPage(p.id);
					if (page.getUniqueIndentifier().equalsIgnoreCase(p.id)
							&& page.getName().equalsIgnoreCase(pagename[0])) {
						workbenchProxy.setCurrentPage(page);
						return;
					}
				}
				if (pagename.length == 0) {
					IWorkbenchPage page = workbenchProxy
							.getWorkbenchPageManager().getWorkbenchPage(
									defaultPage.id);
					workbenchProxy.setCurrentPage(page);
					return;
				}
			}

			IWorkbenchPage page = workbenchProxy.getWorkbenchPageManager()
					.getWorkbenchPage(defaultPage.id);
			workbenchProxy.setCurrentPage(page);
		} finally {
			if (resourceContainer == null) {
				resourceContainer = new WebResourceContainerImpl();
			}
			if (resourceContainer != null) {
				resourceContainer.destroy();
			}
			resourceContainer.init(request, response);
		}
		try {
			getRequestContext(request, response).goHome();
		} catch (IOException e) {
			throw new NirvanaException(e);
		}
		;
	}

	/**
	 *
	 * @param requestItems
	 *            The request items.
	 * @return The created Tiles request context.
	 */
	private IRequestContext getRequestContext(PageContext context) {
		return new JspRequestContext(context.getServletContext(), context);
	}

	private IRequestContext getRequestContext(HttpServletRequest request,
			HttpServletResponse response) {
		return new ServletRequestContext(request.getSession()
				.getServletContext(), request, response);
	}

	public void destroy() {
		resourceContainer.destroy();
	}

	/**
	 * TODO
	 */
	public void reset(HttpServletRequest request, HttpServletResponse response)
			throws NirvanaException {
		resourceContainer.reset(request, response);
	}

	public IWebResourceContainer getWebResourceContainer() {
		return resourceContainer;
	}

	public boolean isBoostrap() {
		return initialization;
	}
}
