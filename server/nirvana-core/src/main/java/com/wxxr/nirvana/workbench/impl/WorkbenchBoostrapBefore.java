package com.wxxr.nirvana.workbench.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.workbench.IServiceManager;
import com.wxxr.nirvana.workbench.IWebResource;
import com.wxxr.nirvana.workbench.IWorkbench;

public class WorkbenchBoostrapBefore {
	private String separatorChar = "/";

	private Log log = LogFactory.getLog(Workbench.class);

	private Map<String, Object> config = new HashMap<String, Object>();

	private IServiceManager serviceManager;

	private boolean started;
	
	private IWorkbench workbench;
	
	public WorkbenchBoostrapBefore() {
		
	}

	public void start(Map<String, Object> config) throws NirvanaException {
		if (started) return;
		if (config != null)
			this.config = config;

		if (serviceManager == null) {
			serviceManager = new ServiceManager();
		}
		preproccessResources();
		started = true;
	}
	
	public void destroy() {
	}
	
	public IServiceManager getServiceManager(){
		if(!started){
			throw new IllegalStateException(" WorkbenchBoostrap not started!!");
		}
		return serviceManager;
	}

	private void preproccessResources() throws NirvanaException {
		List<IWebResource> res = serviceManager.getWebResourceManager()
				.getResources();
		for (IWebResource r : res) {
			if (r.getType().equals("css") || r.getType().equals("js")) {
				String webroot = (String) (config.containsKey("webRoot") ? config
						.get("webRoot") : "");
				String plugindir = (String) (config.containsKey("plugindir") ? config
						.get("plugindir") : "");
				String htmldir = (String) (config.containsKey("htmldir") ? config
						.get("htmldir") : "");
				String id = r.getContributorId();
				String version = r.getContributorVersion();
				String rpath = webroot + File.separatorChar + plugindir
						+ File.separatorChar + r.getContributorId()
						+ File.separatorChar + r.getContributorVersion()
						+ File.separatorChar + htmldir + File.separatorChar
						+ r.getUri();
				String replace = plugindir + separatorChar
						+ r.getContributorId() + separatorChar
						+ r.getContributorVersion() + separatorChar + htmldir;
				processFile(rpath, replace);
			}
		}
	}

	private void processFile(String rpath, String replace)
			throws NirvanaException {
		if (StringUtils.isNoneBlank(rpath)) {
			log.info("processing resource file " + rpath);
			File rf = new File(rpath);
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
