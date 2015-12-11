package com.wxxr.nirvana.jsp.taglib;

import java.util.HashMap;
import java.util.Map;

public class ViewTag extends UITag {

	private String anchor;

	public String getAnchor() {
		return anchor;
	}

	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}

	protected Map<String, Object> getParameters() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("anchor", anchor);
		return parameters;
	}

}
