/*
 * @(#)SecurityManager.java	 2007-9-27
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.workbench;

import java.security.Principal;

/**
 * @author fudapeng
 *
 */
public interface ISecurityManager {
  boolean currentUserHasRole(String role);
  boolean currentUserHasARoleOf(String[] roles);
  boolean currentUserHasRoles(String[] roles);
  Principal getCurrentUser();
  void destroy();
}
