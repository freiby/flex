package com.wxxr.nirvana.platform;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.net.URL;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.wxxr.nirvana.theme.ITheme;
import com.wxxr.nirvana.workbench.IDispatchUI;
import com.wxxr.nirvana.workbench.IProduct;
import com.wxxr.nirvana.workbench.IView;
import com.wxxr.nirvana.workbench.IWebResource;
import com.wxxr.nirvana.workbench.IWorkbenchPage;
import com.wxxr.nirvana.workbench.impl.View;
import com.wxxr.nirvana.workbench.impl.Workbench;

public class WorkbenchTest {

	@Before
	public void setUp() throws Exception {
		System.getProperties().setProperty("data.dir", "target/partest");
		String parfile = "test.par";
		URL url = PlatformTest.class.getResource(parfile);
		PlatformLocator.getPlatform().start();
		
		PlatformLocator.getPlatform().deployPlugin(url);
		
		String parstylefile = "testStyle.par";
		url = PlatformTest.class.getResource(parstylefile);
		PlatformLocator.getPlatform().deployPlugin(url);
		
		PlatformLocator.getPlatform().activatePlugins();
	}

	@Test
	public void testWorkbenchCreate(){
		Workbench workbench = new Workbench();
		IProduct p = workbench.getProductManager().getProductById("com.wxxr.nirvana.test.nirvana");
		assertNotNull(p);
		String themeid = p.getTheme();
		assertEquals("com.wxxr.nirvana.style.nirvanaStyle_theme", themeid);
		ITheme theme = workbench.getThemeManager().getTheme(themeid);
		assertNotNull(theme);
		String themeuri = ((IDispatchUI)theme.getDesktop()).getURI();
		assertEquals("desktopuri", themeuri);
		String pageuri = ((IDispatchUI)theme.getPageLayout()).getURI();
		assertEquals("pagelayouturi", pageuri);
		
		String defaultPage = p.getDefaultPage();
		assertEquals("com.wxxr.nirvana.test.niravanaPage", defaultPage);
		assertEquals(2, p.getAllPages().length);
		
		IWorkbenchPage page = workbench.getWorkbenchPageManager().getWorkbenchPage(defaultPage);
		assertNotNull(page);
		String[] views = page.getAllViewIds();
		assertEquals(3, views.length);
		
		views = workbench.getViewManager().getViewIds();
		assertEquals(3, views.length);
		
		IView view = workbench.getViewManager().find("com.wxxr.nirvana.test.chart2");
		assertNotNull(view);
		View.ResourceRef[] vrr = view.getResourcesRef();
		assertEquals(3, vrr	.length);
		
		List<IWebResource> webrs = workbench.getWebResourceManager().getResources();
		assertEquals(3, webrs.size());
		
		for(IWebResource wr : webrs){
			assertEquals("js", wr.getType());
		}
	}
	

}
