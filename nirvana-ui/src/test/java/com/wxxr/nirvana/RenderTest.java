package com.wxxr.nirvana;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.wxxr.nirvana.context.NirvanaServletContext;
import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.platform.WorkbenchTest;
import com.wxxr.nirvana.ui.WorkbenchContainerFactory;
import com.wxxr.nirvana.ui.WorkbenchContainerImpl;
import com.wxxr.nirvana.workbench.IWorkbench;
import com.wxxr.nirvana.workbench.impl.Workbench;

public class RenderTest {
	
	
	ServletContext servletContext;
	Workbench workbench;
	@Before
	public void setUp() throws Exception {
		ServletContextListener listener = EasyMock.createMock(ServletContextListener.class);
		servletContext = EasyMock.createMock(ServletContext.class);
		
		WorkbenchTest workbenchTest = new WorkbenchTest();
		workbenchTest.setUp();
		
		workbench = new Workbench();
		EasyMock.expect(servletContext.getAttribute(ContainerAccess.WORKBENCH_ATTRIBUTE)).andReturn(workbench).anyTimes();
		servletContext.setAttribute(ContainerAccess.WORKBENCH_ATTRIBUTE, workbench);
//		EasyMock.expectLastCall().andThrow(new RuntimeException("aaa"));
		EasyMock.replay(servletContext);
		ContainerAccess.setWorkbench(servletContext,workbench);
		
		
	}
	
	@Test
	public void testBoostrap() throws NirvanaException{
		String product = "nirvana";
		String page = "niravanaPage";
		
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		
		HttpSession session = EasyMock.createMock(HttpSession.class);
		EasyMock.expect(request.getSession()).andReturn(session).anyTimes();//.andStubReturn(session).anyTimes();
		
		EasyMock.expect(session.getServletContext()).andReturn(servletContext).anyTimes();
		
		EasyMock.expect(session.getAttribute(ContainerAccess.CONTAINER_ATTRIBUTE)).andReturn(null).anyTimes();
		
		IWorkbenchContainer container = null;
		container = WorkbenchContainerFactory.createWorkbench();
		
		EasyMock.expect(session.getAttribute(IWorkbenchContainer.WORKBENCH_CONTAINER)).andReturn(container).anyTimes();
		
		container.init(request, response);
		session.setAttribute(IWorkbenchContainer.WORKBENCH_CONTAINER,container);
		
		EasyMock.expect(session.getAttribute(IWorkbenchContainer.WORKBENCH_PROXY)).andReturn(null).anyTimes();
		
		NirvanaServletContext nirvanaContext = new NirvanaServletContext(new HashMap<String, Object>());
		NirvanaServletContext.setContext(nirvanaContext);
		
		NirvanaServletContext.setRequest(request);
		NirvanaServletContext.setResponse(response);
		NirvanaServletContext.setServletContext(session.getServletContext());
		NirvanaServletContext.setHttpSession(session);
		
		
		IWorkbench workbench = ContainerAccess.getWorkbench();
		synchronized (workbench) {
			IWorkbenchContainer workbenchContainer = ContainerAccess.getContainer(session);
			if(workbenchContainer == null){
				
				ContainerAccess.setContainer(session, container);
			}
		}
		
		EasyMock.replay(request,response,session);
		container.bootstrap(product, request, response);
		
	}

}
