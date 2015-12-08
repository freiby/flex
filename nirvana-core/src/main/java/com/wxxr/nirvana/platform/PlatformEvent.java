package com.wxxr.nirvana.platform;

import java.util.EventObject;

@SuppressWarnings("serial")
public class PlatformEvent extends EventObject {
	private String id;
	private String version;
	public PlatformEvent(String id,String version) {
		super("");
		this.id = id;
		this.version =  version;
	}
	public String getId() {
		return id;
	}
	public String getVersion() {
		return version;
	}
	

}
