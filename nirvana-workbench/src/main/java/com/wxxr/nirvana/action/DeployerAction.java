package com.wxxr.nirvana.action;

import java.io.File;
import java.net.MalformedURLException;

import org.apache.commons.lang3.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.wxxr.nirvana.platform.CoreException;
import com.wxxr.nirvana.platform.PlatformLocator;

public class DeployerAction extends ActionSupport {
	private String url;
	private String id;
	private String version;
	private String show;

	public String doDeploy() throws CoreException, MalformedURLException {
		if (StringUtils.isNoneBlank(url)) {
			PlatformLocator.getPlatform().deployPlugin(
					new File(url).toURI().toURL());
			show = "deploy success";
			return SUCCESS;
		} else {
			show = "deploy error";
			return ERROR;
		}
	}

	public String doUndeploy() throws CoreException, MalformedURLException {
		if (StringUtils.isNoneBlank(id) && StringUtils.isNoneBlank(version)) {
			PlatformLocator.getPlatform().undeployPlugin(id, version);
			show = "undeploy success";
			return SUCCESS;
		} else {
			show = "undeploy error";
			return ERROR;
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getShow() {
		return show;
	}

	public void setShow(String show) {
		this.show = show;
	}

}
