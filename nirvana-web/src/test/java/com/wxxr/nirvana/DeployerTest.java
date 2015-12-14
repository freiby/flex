package com.wxxr.nirvana;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.*;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import com.wxxr.nirvana.deploy.tomcat.PluginDeployer;
import com.wxxr.nirvana.platform.IPluginDescriptor;
import com.wxxr.nirvana.platform.PlatformLocator;
import com.wxxr.nirvana.platform.PlatformTest;

public class DeployerTest {

	@Before
	public void setUp() throws Exception {
		System.setProperty("data.dir", "target/partest");
	}

	@Test
	public void testDeployer() throws InterruptedException {
		// 1 boostrap deployer listener platform
		PluginDeployer deployer = PluginDeployer.getPluginDeployer();
		deployer.init("target/web");
		deployer.start();

		// 2 boostrap platform
		try {
			PlatformLocator.getPlatform().start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String parfile = "test.par";
		URL url = PlatformTest.class.getResource(parfile);
		PlatformLocator.getPlatform().deployPlugin(url);

		String parstylefile = "testStyle.par";
		url = PlatformTest.class.getResource(parstylefile);
		PlatformLocator.getPlatform().deployPlugin(url);
		Thread.currentThread().sleep(5000L);

		IPluginDescriptor pluginDescription = PlatformLocator.getPlatform()
				.getPluginDescriptor("com.wxxr.nirvana.test");
		assertNotNull(pluginDescription);

	}

}
