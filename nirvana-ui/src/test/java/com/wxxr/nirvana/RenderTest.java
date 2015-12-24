package com.wxxr.nirvana;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import junit.framework.Assert;

import org.apache.commons.io.output.StringBuilderWriter;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.wxxr.nirvana.context.NirvanaServletContext;
import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.platform.WorkbenchTest;
import com.wxxr.nirvana.ui.WorkbenchContainerImpl;
import com.wxxr.nirvana.workbench.IServiceManager;
import com.wxxr.nirvana.workbench.IWebResource;
import com.wxxr.nirvana.workbench.impl.ServiceManager;
import com.wxxr.nirvana.workbench.impl.Workbench;

public class RenderTest {

	ServletContext servletContext;
	Workbench workbench;
	IServiceManager serviceManaer;

	@Before
	public void setUp() throws Exception {
		ServletContextListener listener = EasyMock
				.createMock(ServletContextListener.class);
		servletContext = EasyMock.createMock(ServletContext.class);

		serviceManaer = new ServiceManager();
		
		WorkbenchTest workbenchTest = new WorkbenchTest();
		workbenchTest.setUp();

		workbench = new Workbench();
		EasyMock.expect(
				servletContext
						.getAttribute(ContainerAccess.WORKBENCH_ATTRIBUTE))
				.andReturn(workbench).anyTimes();
		servletContext.setAttribute(ContainerAccess.WORKBENCH_ATTRIBUTE,
				workbench);
		
		EasyMock.expect(
				servletContext.getAttribute(ContainerAccess.WORKBENCH_SERVICE_ATTRIBUTE)).andReturn(serviceManaer).anyTimes();
		
		// EasyMock.expectLastCall().andThrow(new RuntimeException("aaa"));
		EasyMock.replay(servletContext);
		ContainerAccess.setWorkbench(servletContext, workbench);

	}

	@Test
	public void testBoostrap() throws NirvanaException, ServletException,
			IOException {
		String product = "nirvana";
		String page = "tiger";

		HttpServletRequest request = EasyMock
				.createMock(HttpServletRequest.class);
		HttpServletResponse response = EasyMock
				.createMock(HttpServletResponse.class);
		HttpSession session = EasyMock.createMock(HttpSession.class);
		RequestDispatcher dispatcher = EasyMock
				.createMock(RequestDispatcher.class);

		StringBuilderWriter sbw = new StringBuilderWriter();
		PrintWriter pw = new PrintWriter(sbw);
		EasyMock.expect(response.getWriter()).andReturn(pw).anyTimes();
		EasyMock.expect(request.getSession()).andReturn(session).anyTimes();
		EasyMock.expect(session.getServletContext()).andReturn(servletContext)
				.anyTimes();
		EasyMock.expect(
				session.getAttribute(ContainerAccess.CONTAINER_ATTRIBUTE))
				.andReturn(null).anyTimes();

		EasyMock.expect(
				request.getRequestDispatcher("plugins/com.wxxr.nirvana.test/1.0.0/html/view/chart1.jsp"))
				.andReturn(dispatcher).anyTimes();
		EasyMock.expect(
				request.getRequestDispatcher("plugins/com.wxxr.nirvana.test/1.0.0/html/view/chart3.jsp"))
				.andReturn(dispatcher).anyTimes();
		EasyMock.expect(
				request.getRequestDispatcher("plugins/com.wxxr.nirvana.test/1.0.0/html/view/c3-1.html"))
				.andReturn(dispatcher).anyTimes();
		EasyMock.expect(
				request.getRequestDispatcher("/plugins/com.wxxr.nirvana.style/1.0.0/html/desktopuri"))
				.andReturn(dispatcher).anyTimes();
		EasyMock.expect(
				request.getRequestDispatcher("plugins/com.wxxr.nirvana.style/1.0.0/html/pagelayouturi"))
				.andReturn(dispatcher).anyTimes();
		
		request.setAttribute("org.apache.tiles.servlet.context.ServletTilesRequestContext.FORCE_INCLUDE", true);
		
		EasyMock.expect(request.getRequestDispatcher("index.jsp")).andReturn(dispatcher).anyTimes();
		dispatcher.include(request, response);

		InvokeContext invokeContext = new InvokeContext();
		final RenderMock rm = new RenderMock(invokeContext);
		IWorkbenchContainer container = new WorkbenchContainerImpl() {
			@Override
			protected void initProvider() {
				super.initProvider();
				renderProviders.put(rm.processComponent(), rm);
			}
		};

		Stack<IUIComponentContext> contextStack = new Stack<IUIComponentContext>();
		EasyMock.expect(
				request.getAttribute("org.apache.tiles.AttributeContext.STACK"))
				.andReturn(contextStack).anyTimes();


		session.setAttribute(ContainerAccess.CONTAINER_ATTRIBUTE, container);
		session.setAttribute(ContainerAccess.WORKBENCH_SESSION_ATTRIBUTE,
				workbench);

		EasyMock.expect(
				session.getAttribute(ContainerAccess.WORKBENCH_SESSION_ATTRIBUTE))
				.andReturn(workbench).anyTimes();

		PageContext pageContext = new PageContextMock(request, response,
				session, servletContext);

		EasyMock.replay(request, response, session);

		NirvanaServletContext nirvanaContext = new NirvanaServletContext(
				new HashMap<String, Object>());
		NirvanaServletContext.setContext(nirvanaContext);
		NirvanaServletContext.setRequest(request);
		NirvanaServletContext.setResponse(response);
		NirvanaServletContext.setServletContext(session.getServletContext());
		NirvanaServletContext.setHttpSession(session);

		container.init(request, response);
		ContainerAccess.setContainer(container);
		ContainerAccess.setSessionWorkbench(workbench);

		container.bootstrap(request, response, product, page);

		JspTagMock desktop = new JspTagMock(container, "desktop", pageContext,
				null);
		JspTagMock pageTag = new JspTagMock(container, "page", pageContext,
				null);
		JspTagMock navTag = new JspTagMock(container, "navigation",
				pageContext, null);

		desktop.addTag(navTag);
		desktop.addTag(pageTag);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("anchor", "headerone");
		JspTagMock viewTag1 = new JspTagMock(container, "view", pageContext,
				map);
		map = new HashMap<String, Object>();
		map.put("anchor", "headertwo");
		JspTagMock viewTag2 = new JspTagMock(container, "view", pageContext,
				map);
		map = new HashMap<String, Object>();
		map.put("anchor", "headerthree");
		JspTagMock viewTag3 = new JspTagMock(container, "view", pageContext,
				map);

		pageTag.addTag(viewTag1);
		pageTag.addTag(viewTag2);
		pageTag.addTag(viewTag3);

		invokeContext.setCurrentNode(desktop);

		IWebResourceContainer webResourceContainer = container
				.getWebResourceContainer();
		IWebResource[] resources = webResourceContainer.getResources("header");
		Assert.assertEquals(2, resources.length);
		resources = webResourceContainer.getResources("before");
		Assert.assertEquals(1, resources.length);
	}

}
