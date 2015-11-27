/*
 * @(#)PermissionDescriptor.java	 2007-11-3
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.workbench.impl;

import org.apache.commons.lang3.StringUtils;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.workbench.IPermissionDescriptor;
import com.wxxr.nirvana.workbench.IPermissionsManager;
import com.wxxr.nirvana.workbench.config.BaseContributionItem;

/**
 * @author neillin
 *
 */
public class PermissionDescriptor extends BaseContributionItem implements IPermissionDescriptor {
	
	private static final String ELEMENT_RW_NAME="rwRoles";
	private static final String ELEMENT_R_NAME="rRoles";
	private static final String ELEMENT_X_NAME="xRoles";
	private static final String ATT_TRAGET_ID="targetId";

	protected String[] rRoles;
	protected String[] rwRoles;
	protected String[] xRoles;
	protected String targetId;

	/* (non-Javadoc)
	 * @see com.wxxr.web.ui.permissions.IPermissionDescriptor#getRRoles()
	 */
	public String[] getRRoles() {
		return rRoles;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.web.ui.permissions.IPermissionDescriptor#getRWRoles()
	 */
	public String[] getRwRoles() {
		return rwRoles;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.web.ui.permissions.IPermissionDescriptor#getTargetId()
	 */
	public String getTargetId() {
		return targetId;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.web.ui.permissions.IPermissionDescriptor#getXRoles()
	 */
	public String[] getXRoles() {
		return xRoles;
	}


	/**
	 * @param roles the rRoles to set
	 */
	public void setRRoles(String[] roles) {
		rRoles = roles;
	}

	/**
	 * @param rwRoles the rwRoles to set
	 */
	public void setRwRoles(String[] rwRoles) {
		this.rwRoles = rwRoles;
	}

	/**
	 * @param roles the xRoles to set
	 */
	public void setXRoles(String[] roles) {
		xRoles = roles;
	}

	/**
	 * @param targetId the targetId to set
	 */
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	
	public void destroy() {
		
	}

	public void init(IPermissionsManager manager, IConfigurationElement config) {
		setConfigurationElement(config);
		if(config != null){
			targetId = elem.getAttribute(ATT_TRAGET_ID);
			rwRoles = processPermissions(config, ELEMENT_RW_NAME);
			rRoles = processPermissions(config, ELEMENT_R_NAME);
			xRoles = processPermissions(config, ELEMENT_X_NAME);
		}
	}

	private String[] processPermissions(IConfigurationElement config,String elemName) {
		IConfigurationElement[] elems = config.getChildren(elemName);
		StringBuffer buf = new StringBuffer();
		int cnt = 0;
		for (int i = 0; (elems != null)&&(i < elems.length); i++) {
			IConfigurationElement elem = elems[i];
			if(elem != null){
				String s = elem.getValue();
				if(StringUtils.isNotBlank(s)){
					if(cnt > 0){
						buf.append(',');
					}
					buf.append(StringUtils.trim(s));
					cnt++;
				}
			}
		}
		if(buf.length() != 0){
			return buf.toString().split(",");
		}
		return null;
	}

}
