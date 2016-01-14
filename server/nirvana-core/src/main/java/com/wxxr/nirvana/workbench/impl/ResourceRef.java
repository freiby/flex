package com.wxxr.nirvana.workbench.impl;

import com.wxxr.nirvana.platform.IConfigurationElement;

public class ResourceRef {
	private String ATT_REF = "ref";
	private String ATT_POINT = "point";
	private IConfigurationElement elem;

	public ResourceRef(IConfigurationElement elem) {
		this.elem = elem;
	}

	public String getRef() {
		return elem.getAttribute(ATT_REF);
	}

	public String getPoint() {
		return elem.getAttribute(ATT_POINT);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ResourceRef)) {
			return false;
		}
		ResourceRef nr = (ResourceRef) obj;
		if(nr.getRef().equals(nr.getRef())){
			return true;
		}
		return false;
	}
	@Override
    public int hashCode() {
        return getRef().hashCode();
    }

}
