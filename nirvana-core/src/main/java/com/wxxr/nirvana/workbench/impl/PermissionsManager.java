/*
 * @(#)PermissionsManager.java	 2007-11-3
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.workbench.impl;

import java.awt.LayoutManager;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wxxr.nirvana.platform.CoreException;
import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.platform.IExtension;
import com.wxxr.nirvana.platform.IPluginDescriptor;
import com.wxxr.nirvana.workbench.IPermissionDescriptor;
import com.wxxr.nirvana.workbench.IPermissionsManager;
import com.wxxr.nirvana.workbench.UIConstants;
import com.wxxr.nirvana.workbench.config.BaseExtensionPointManager;

/**
 * @author neillin
 *
 */
public class PermissionsManager extends BaseExtensionPointManager implements
		IPermissionsManager {
	private static final Log log = LogFactory.getLog(LayoutManager.class);
	private static final IPermissionDescriptor[] NO_CHILD = new IPermissionDescriptor[0];
	
	private static final String ELEMENT_NAME="permission";
	private static final String ATT_CLASS="class";
		
	protected Map<String, Map<String,IPermissionDescriptor>> permissions = new ConcurrentHashMap<String, Map<String,IPermissionDescriptor>>();
	protected Map<String, List<String>> exts = new ConcurrentHashMap<String, List<String>>();
	
	public PermissionsManager(){
		super(UIConstants.UI_NAMESPACE,UIConstants.EXTENSION_POINT_PERMISSIONS);
	}

	protected IPermissionDescriptor createNewPermission(IConfigurationElement elem) throws Exception{
		String clazz = elem.getAttribute(ATT_CLASS);
		IPermissionDescriptor permission = null;
		if(StringUtils.isBlank(clazz)){
			permission = new PermissionDescriptor();
		}else{
	    	IPluginDescriptor plugin = getUIPlatform().getPluginDescriptor(elem.getNamespaceIdentifier());
	    	permission = (IPermissionDescriptor)createPluginObject(clazz, plugin);				
		}
		permission.init(this, elem);
		addPermission(permission);
		return permission;
	}
	
	@Override
	protected void processExtensionAdded(IExtension ext) {
		IConfigurationElement[] configs = ext.getConfigurationElements();
		List<String> list = exts.get(ext.getUniqueIdentifier());
		for (int i = 0; i < configs.length; i++) {
			IConfigurationElement elem = configs[i];
			if((elem != null)&&ELEMENT_NAME.equalsIgnoreCase(elem.getName())){
				try {
					IPermissionDescriptor permission = createNewPermission(elem);
					if(list == null){
						list = new LinkedList<String>();
						exts.put(ext.getUniqueIdentifier(), list);
					}
					if(!list.contains(permission.getTargetId())){
						list.add(permission.getTargetId());
					}
				} catch (Exception e) {
					log.warn("Failed to create permission from configuration element :"+elem, e);
				}
			}
		}		
	}

	@Override
	protected void processExtensionRemoved(IExtension ext) {
		List<String> list = exts.remove(ext.getUniqueIdentifier());
		if((list == null)||list.isEmpty()){
			return;
		}
		String[] ids = list.toArray(new String[list.size()]);
		for (int i = 0; i < ids.length; i++) {
			String id = ids[i];
			IPermissionDescriptor per = removePermission(ext.getNamespaceIdentifier(),id);
			if(per != null){
				per.destroy();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.web.ui.permissions.IPermissionsManager#addPermission(com.wxxr.web.ui.permissions.IPermissionDescriptor)
	 */
	public void addPermission(IPermissionDescriptor permission) {
		if(permission == null){
			throw new IllegalArgumentException();
		}
		synchronized(permissions){
			Map<String, IPermissionDescriptor> map = permissions.get(permission.getContributorId());
			if(map == null){
				map = new HashMap<String, IPermissionDescriptor>();
				permissions.put(permission.getContributorId(), map);
			}
			map.put(permission.getTargetId(), permission);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.web.ui.permissions.IPermissionsManager#destroy()
	 */
	public void destroy() {
		super.stop();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.web.ui.permissions.IPermissionsManager#getAllPermissionIds()
	 */
	public String[] getAllNamespaces() {
		synchronized(permissions){
			if(permissions.isEmpty()){
				return UIConstants.EMPTY_STRING_ARRAY;
			}
			return permissions.keySet().toArray(new String[permissions.size()]);
		}
	}

	public String[]  getAllPermissionIds(String namespace) {
		synchronized(permissions){
			if(permissions.isEmpty()){
				return UIConstants.EMPTY_STRING_ARRAY;
			}
			Map<String, IPermissionDescriptor> map = permissions.get(namespace);
			if((map == null)||map.isEmpty()){
				return UIConstants.EMPTY_STRING_ARRAY;
			}
			return map.keySet().toArray(new String[map.size()]);
		}
		
	}
	/* (non-Javadoc)
	 * @see com.wxxr.web.ui.permissions.IPermissionsManager#getAllPermissions()
	 */
	public IPermissionDescriptor[] getAllPermissions() {
		if(permissions.isEmpty()){
			return NO_CHILD;
		}
		LinkedList<IPermissionDescriptor> all = new LinkedList<IPermissionDescriptor>();
		for (Map<String, IPermissionDescriptor> m : permissions.values()) {
			if((m != null)&&(!m.isEmpty())){
				all.addAll(m.values());
			}
		}
		return all.toArray(new IPermissionDescriptor[all.size()]);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.web.ui.permissions.IPermissionsManager#getDefaultPermission()
	 */
	public IPermissionDescriptor getDefaultPermission() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.web.ui.permissions.IPermissionsManager#getPermission(java.lang.String)
	 */
	public IPermissionDescriptor getPermission(String pluginId,String targetId) {
		synchronized(permissions){
			if(permissions.isEmpty()){
				return null;
			}
			Map<String, IPermissionDescriptor> map = permissions.get(pluginId);
			if((map == null)||map.isEmpty()){
				return null;
			}
			return map.get(targetId);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.web.ui.permissions.IPermissionsManager#removePermission(java.lang.String)
	 */
	public IPermissionDescriptor removePermission(String namespace, String targetId) {
		synchronized(permissions){
			if(permissions.isEmpty()){
				return null;
			}
			Map<String, IPermissionDescriptor> map = permissions.get(namespace);
			if((map == null)||map.isEmpty()){
				return null;
			}
			return map.remove(targetId);
		}
	}

}
