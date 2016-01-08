package com.wxxr.nirvana.deploy.tomcat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.platform.CoreException;
import com.wxxr.nirvana.platform.IPlatform;
import com.wxxr.nirvana.platform.IPlatformListener;
import com.wxxr.nirvana.platform.IPluginDeployerContext;
import com.wxxr.nirvana.platform.PlatformEvent;
import com.wxxr.nirvana.platform.PlatformLocator;

/**
 * lib/ ---> WEB-INF/lib
 * 
 * @author fudapeng
 *
 */
public class PluginDeployer implements IPluginDeployerContext {
	private String separatorChar = "/";
	private static PluginDeployer ins = null;
	private PlatformListener listener = new PlatformListener();
	private String targetPath;

	private static final String PLUGINS_DIR = "plugins";
	private static final String WEBINFOLIB_DIR = "WEB-INF" + File.separatorChar
			+ "lib" + File.separatorChar;

	public static final String HTML_DIR = "html";
	
	
	private Log log = LogFactory.getLog(PluginDeployer.class);
	private Map<String, List<File>> jars = new HashMap<String, List<File>>();

	private class PlatformListener implements IPlatformListener {
		public void onActivePlugin(PlatformEvent event) throws CoreException {
			try {
				deployPlugin(event.getId(), event.getVersion());
			} catch (IOException e) {
				throw new CoreException(e);
			} catch (NirvanaException e) {
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

	public void start(IPlatform platform) {
		platform.addPlatformListener(listener);
	}

	public void stop() {
		IPlatform platform = PlatformLocator.getPlatform();
		platform.removePlatformListener(listener);
	}

	private void deployPlugin(String id, String version) throws IOException, NirvanaException {
		String deploy = System.getProperty("start.deploy");
		if (deploy != null && deploy.equals("n")) {
			return;
		}
		IPlatform platform = PlatformLocator.getPlatform();
		String rootPath = platform.getPluginRootDir();

		// 1、lib -- jar file
		// String sourceLibdir = rootPath + File.separatorChar + id
		// + File.separatorChar + version + File.separatorChar + "lib";
		// String targetLib = targetPath + File.separatorChar + PLUGINS_DIR + id
		// + File.separatorChar + version + File.separatorChar;
		// File slibdir = new File(sourceLibdir);
		// File[] files = slibdir.listFiles();
		// File targetLibdir = new File(targetLib);
		// if (!targetLibdir.exists()) {
		// targetLibdir.mkdirs();
		// }

		// List<File> jarfiles = new ArrayList<File>();
		// if (files != null) {
		// for (File jarfile : files) {
		// FileUtils.copyFileToDirectory(jarfile, new File(targetLib));
		// jarfiles.add(jarfile);
		// }
		// }
		// jars.put(id, jarfiles);

		// if (slibdir.exists())
		// FileUtils.copyDirectoryToDirectory(slibdir, targetLibdir);

		// 2 、html
		String sourceHtmldir = rootPath + File.separatorChar + id
				+ File.separatorChar + version + File.separatorChar + HTML_DIR;
		String targetHtml = targetPath + File.separatorChar + PLUGINS_DIR
				+ File.separatorChar + id + File.separatorChar + version
				+ File.separatorChar;
		File shtmldir = new File(sourceHtmldir);
		File targetdir = new File(targetHtml);
		if (!targetdir.exists()) {
			targetdir.mkdirs();
		}
		if (shtmldir.exists())
			FileUtils.copyDirectoryToDirectory(shtmldir, targetdir);
		preproccessResources(id,version);
	}

	private void undeployPlugin(String id, String version) throws IOException {
		// delete jar
		// if (jars.containsKey(id)) {
		// List<File> jarfs = jars.get(id);
		// for (File jar : jarfs) {
		// FileUtils.deleteQuietly(jar);
		// }
		// }
		jars.remove(id);
		String targetHtml = targetPath + File.separatorChar + PLUGINS_DIR
				+ File.separatorChar + id + File.separatorChar + version;
		FileUtils.deleteDirectory(new File(targetHtml));

	}

	public String getPluginDir() {
		return PLUGINS_DIR;
	}

	public String getPluginHtmlDIR() {
		return HTML_DIR;
	}

	private void collectionResource(File src, List<File> resources) {
		if(src == null || !src.exists()){
			return;
		}
		if(src.isDirectory()){
			File[] files = src.listFiles();
			for(File file : files){
				if(file.isDirectory()){
					collectionResource(file,resources);
				}else{
					if(file.getName().endsWith("css") || file.getName().endsWith("js")){
						resources.add(file);
					}
				}
			}
		}
	}

	private void preproccessResources(String id, String version) throws NirvanaException {
		String targetHtml = targetPath + File.separatorChar + PLUGINS_DIR + File.separatorChar + id
				+ File.separatorChar + version;
		File htmldir = new File(targetHtml);
		List<File> resources = new ArrayList<File>();
		collectionResource(htmldir, resources);
		for(File r : resources){
			String replace = getPluginDir() + separatorChar
					+ id + separatorChar
					+ version + separatorChar + getPluginHtmlDIR();
			processFile(r, replace);
		}
	}

	private void processFile(File rpath, String replace)
			throws NirvanaException {
		if (rpath.exists()) {
			log.info("processing resource file " + rpath);
			File rf = rpath;
			if (rf.exists() && rf.isFile()) {
				String srcFileName = rf.getName();
				try {
					FileReader fr = new FileReader(rf);
					BufferedReader br = new BufferedReader(fr);
					StringBuilder content = new StringBuilder();
					while (br.ready()) {
						content.append(br.readLine() + "\r\n");
					}
					fr.close();
					String pattern = "#path";
					Pattern r = Pattern.compile(pattern);
					Matcher m = r.matcher(content);

					if (m.find()) {
						log.info(rpath + " file include #path!!");
						String newContent = m.replaceAll(replace);
						File rfbk = new File(rf.getPath() + ".bk");
						if (rfbk.exists()) {
							rfbk.delete();
						}
						if (rf.renameTo(rfbk)) {
							FileWriter fw = new FileWriter(rf);
							fw.write(newContent.toString());
							fw.flush();
							fw.close();
						}
					}
				} catch (Exception e) {
					throw new NirvanaException(e);
				}

			}
		}
	}

}
