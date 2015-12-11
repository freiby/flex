package com.wxxr.nirvana.deploy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.platform.CoreException;
import com.wxxr.nirvana.platform.IPlatform;
import com.wxxr.nirvana.platform.IPlatformListener;
import com.wxxr.nirvana.platform.PlatformEvent;
import com.wxxr.nirvana.platform.PlatformLocator;

/**
 * lib/ ---> WEB-INF/lib
 * 
 * @author fudapeng
 *
 */
public class PluginDeployer {
	private static PluginDeployer ins = null;
	private PlatformListener listener = new PlatformListener();
	private String targetPath;

	private static final String PLUGINS_DIR = "plugins" + File.separatorChar;
	private static final String WEBINFOLIB_DIR = "WEB-INF" + File.separatorChar
			+ "lib" + File.separatorChar;
	private Map<String, List<File>> jars = new HashMap<String, List<File>>();

	private class PlatformListener implements IPlatformListener {
		public void onActivePlugin(PlatformEvent event) throws CoreException {
			try {
				deployPlugin(event.getId(), event.getVersion());
			} catch (IOException e) {
				throw new CoreException(e);
			}
		}

		public void onDeactivePlugin(PlatformEvent event) throws CoreException {
			try {
				undeployPlugin(event.getId(), event.getVersion());
			} catch (IOException e) {
				throw new CoreException(e);
			}
		}

	}

	public static PluginDeployer getPluginDeployer() {
		if (ins == null) {
			ins = new PluginDeployer();
		}
		return ins;
	}

	public void init(String target) {
		this.targetPath = target;
	}

	public void start() {
		IPlatform platform = PlatformLocator.getPlatform();
		platform.addPlatformListener(listener);
	}

	public void stop() {
		IPlatform platform = PlatformLocator.getPlatform();
		platform.removePlatformListener(listener);
	}

	private void deployPlugin(String id, String version) throws IOException {
		IPlatform platform = PlatformLocator.getPlatform();
		String rootPath = platform.getPluginRootDir();

		// 1、lib -- jar file
		String sourceLibdir = rootPath + File.separatorChar + id
				+ File.separatorChar + version + File.separatorChar + "lib";
		String targetLib = targetPath + File.separatorChar + WEBINFOLIB_DIR;
		File slibdir = new File(sourceLibdir);
		File[] files = slibdir.listFiles();
		File targetLibdir = new File(targetLib);
		if (!targetLibdir.exists()) {
			targetLibdir.mkdirs();
		}

		List<File> jarfiles = new ArrayList<File>();
		if (files != null) {
			for (File jarfile : files) {
				FileUtils.copyFileToDirectory(jarfile, new File(targetLib));
				jarfiles.add(jarfile);
			}
		}
		jars.put(id, jarfiles);

		// 2 、html
		String sourceHtmldir = rootPath + File.separatorChar + id
				+ File.separatorChar + version + File.separatorChar + "html";
		String targetHtml = targetPath + File.separatorChar + PLUGINS_DIR + id
				+ File.separatorChar + version + File.separatorChar;
		File shtmldir = new File(sourceHtmldir);
		File targetdir = new File(targetHtml);
		if (!targetdir.exists()) {
			targetdir.mkdirs();
		}
		if (shtmldir.exists())
			FileUtils.copyDirectoryToDirectory(shtmldir, targetdir);
	}

	private void undeployPlugin(String id, String version) throws IOException {
		// delete jar
		if (jars.containsKey(id)) {
			List<File> jarfs = jars.get(id);
			for (File jar : jarfs) {
				FileUtils.deleteQuietly(jar);
			}
		}
		jars.remove(id);
		String targetHtml = targetPath + File.separatorChar + PLUGINS_DIR + id
				+ File.separatorChar + version;
		FileUtils.deleteDirectory(new File(targetHtml));

	}

}
