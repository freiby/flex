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
import com.wxxr.nirvana.platform.IPlatform;
import com.wxxr.nirvana.platform.PlatformLocator;
import com.wxxr.nirvana.theme.ITheme;
import com.wxxr.nirvana.theme.IThemeManager;
import com.wxxr.nirvana.workbench.IPageLayoutManager;
import com.wxxr.nirvana.workbench.IPermissionsManager;
import com.wxxr.nirvana.workbench.IProductManager;
import com.wxxr.nirvana.workbench.IRender;
import com.wxxr.nirvana.workbench.ISecurityManager;
import com.wxxr.nirvana.workbench.IUIRenderManager;
import com.wxxr.nirvana.workbench.IViewManager;
import com.wxxr.nirvana.workbench.IWebResource;
import com.wxxr.nirvana.workbench.IWebResourceManager;
import com.wxxr.nirvana.workbench.IWorkbench;
import com.wxxr.nirvana.workbench.IWorkbenchPageManager;

public class Workbench implements IWorkbench {

	private IThemeManager themeManager;

	private ISecurityManager securityManager;

	private IPermissionsManager permissionsManager;

	private IWorkbenchPageManager workbenchPageManager;

	private IViewManager viewManager;

	private IProductManager productManager;

	private IWebResourceManager webresourceManager;

	private IPageLayoutManager pageLayoutManager;

	private IUIRenderManager renderManager;

	private Log log = LogFactory.getLog(Workbench.class);

	private Map<String, Object> config = new HashMap<String, Object>();

	public Workbench(Map<String, Object> config) {
		this.config = config;
	}

	public interface ICreateRenderContext {
		IRender createRender(String id);
	};

	public IWebResourceManager getWebResourceManager() {
		if (webresourceManager == null) {
			webresourceManager = new WebResourceManager();
			webresourceManager.start();
		}
		return webresourceManager;
	}

	public IWorkbenchPageManager getWorkbenchPageManager() {
		if (workbenchPageManager == null) {
			workbenchPageManager = new WorkbenchPageManager(getCreateContext());
			workbenchPageManager.start();
		}
		return workbenchPageManager;
	}

	public IProductManager getProductManager() {
		if (productManager == null) {
			productManager = new ProductManager();
			productManager.start();
		}
		return productManager;
	}

	public IThemeManager getThemeManager() {
		if (themeManager == null) {
			themeManager = new ThemeManager(getCreateContext());
			themeManager.start();
		}
		return themeManager;
	}

	public IPermissionsManager getPermissionsManager() {
		if (permissionsManager == null) {
			permissionsManager = new PermissionsManager();
			permissionsManager.start();
		}
		return permissionsManager;
	}

	public ISecurityManager getSecurityManager() {
		return securityManager;
	}

	public IPlatform getUIPlatform() {
		return PlatformLocator.getPlatform();
	}

	public IViewManager getViewManager() {
		if (viewManager == null) {
			viewManager = new ViewManager(getCreateContext());
			viewManager.start();
		}
		return viewManager;
	}

	private ICreateRenderContext getCreateContext() {
		ICreateRenderContext context = new ICreateRenderContext() {
			public IRender createRender(String id) {
				return getUIRenderManager().find(id);
			}
		};
		return context;
	}

	public ITheme getCurrentTheme() {
		return themeManager.getDefaultTheme();
	}

	public void destroy() {
	}

	public IPageLayoutManager getPageLayoutManager() {
		if (pageLayoutManager == null) {
			pageLayoutManager = new PageLayoutManager(getCreateContext());
			pageLayoutManager.start();
		}
		return pageLayoutManager;
	}

	public IUIRenderManager getUIRenderManager() {
		if (renderManager == null) {
			renderManager = new UIRenderManager();
			renderManager.start();
		}
		return renderManager;
	}

	public void start() throws NirvanaException {
		preproccessResources();
	}

	private String separatorChar = "/";

	private void preproccessResources() throws NirvanaException {
		List<IWebResource> res = getWebResourceManager().getResources();
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
