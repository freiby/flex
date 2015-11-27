package com.wxxr.nirvana.platform;

import java.net.URL;

import static junit.framework.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PlatformTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testPlatformDeploy() throws Exception {
		String parfile = "test.par";
		URL url = PlatformTest.class.getResource(parfile);
		PlatformLocator.getPlatform().deployPlugin(url);
		PlatformLocator.getPlatform().activatePlugins();
		IPluginDescriptor pluginDescription = PlatformLocator.getPlatform().getPluginDescriptor("com.wxxr.nirvana.test");
		assertNotNull(pluginDescription);
	}
	

}
