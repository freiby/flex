/*
 * @(#)SecurityManager.java	 2007-11-3
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.workbench.impl;

import java.security.Principal;

import org.apache.commons.lang3.StringUtils;

import com.wxxr.nirvana.platform.CoreException;
import com.wxxr.nirvana.workbench.ISecurityManager;

/**
 * @author fudapeng
 *
 */
public class SecurityManager implements ISecurityManager {

	/* (non-Javadoc)
	 * @see com.wxxr.web.ui.ISecurityManager#currentUserHasARoleOf(java.lang.String[])
	 */
	public boolean currentUserHasARoleOf(String[] roles) {
		if((roles == null)||(roles.length == 0)){
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < roles.length; i++) {
			String role = roles[i];
			if(StringUtils.isNotBlank(role)&&currentUserHasRole(role)){
				return true;
			}			
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.web.ui.ISecurityManager#currentUserHasRole(java.lang.String)
	 */
	public boolean currentUserHasRole(String role) {
		if(StringUtils.isBlank(role)){
			throw new IllegalArgumentException();
		}
		return false;//FacesContext.getCurrentInstance().getExternalContext().isUserInRole(role);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.web.ui.ISecurityManager#currentUserHasRoles(java.lang.String[])
	 */
	public boolean currentUserHasRoles(String[] roles) {
		if((roles == null)||(roles.length == 0)){
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < roles.length; i++) {
			String role = roles[i];
			if(StringUtils.isNotBlank(role)&&(!currentUserHasRole(role))){
				return false;
			}			
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.web.ui.ISecurityManager#destroy()
	 */
	public void destroy() {

	}

	/* (non-Javadoc)
	 * @see com.wxxr.web.ui.ISecurityManager#getCurrentUser()
	 */
	public Principal getCurrentUser() {
		return null;//FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
	}

}
