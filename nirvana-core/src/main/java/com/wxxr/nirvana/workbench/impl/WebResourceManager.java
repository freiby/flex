/*
 * @(#)WebResourceManager.java	 2007-11-1
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.workbench.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.management.jmx.Trace;
import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.platform.IExtension;
import com.wxxr.nirvana.platform.IRegistryChangeListener;
import com.wxxr.nirvana.workbench.IWebResource;
import com.wxxr.nirvana.workbench.IWorkbenchManager;
import com.wxxr.nirvana.workbench.UIConstants;
import com.wxxr.nirvana.workbench.config.BaseExtensionPointManager;

/**
 * @author neillin
 *
 */
public class WebResourceManager extends BaseExtensionPointManager implements IRegistryChangeListener {

	private static final String RESOURCE_ELEMENT_NAME="webresource";
	private static final Log log = LogFactory.getLog(WebResourceManager.class);
	private IWorkbenchManager workbenchManager;
	private Map<String,List<IWebResource>> resources = new ConcurrentHashMap<String,List<IWebResource>>();
	private Timer timer;
	private SyncTask task;

	public WebResourceManager() {
		super(UIConstants.UI_NAMESPACE,UIConstants.EXTENSION_POINT_RESOURCES);
	}

	private class SyncTask extends TimerTask {
		@Override
		public void run() {
			try {
				doSynchronize();
			} catch (Exception e) {
				log.warn("Caught Exception in web resource sync thread!", e);
			}		
		}
		
	}
	
	public void init(IWorkbenchManager manager){
		super.start();
		this.workbenchManager = manager;
		timer = new Timer();
		task = new SyncTask();
		timer.schedule(task, 5000L,5000L);
	}
	
	protected void processExtensionAdded(IExtension ext){
		IConfigurationElement[] configs = ext.getConfigurationElements();
		List<IWebResource> list = resources.get(ext.getUniqueIdentifier());
		for (int i = 0; i < configs.length; i++) {
			IConfigurationElement elem = configs[i];
			if((elem != null)&&RESOURCE_ELEMENT_NAME.equalsIgnoreCase(elem.getName())){
				WebResource res = new WebResource(elem);
				if(list == null){
					list = new LinkedList<IWebResource>();
					resources.put(ext.getUniqueIdentifier(),list);
				}
				list.add(res);
			}
		}
	}
	
	protected void syncResources(IWebResource resource){
		if(resource == null){
			return;
		}
		String srcDir = resource.getSourceDirectory();
		String destDir = resource.getRealPathOfMapTo();
		File src = new File(srcDir);
		File dest = new File(destDir);
		if(!src.exists()){
			if(log.isInfoEnabled()){
				log.info("web resource directory :"+srcDir+" is not existing, skip resource synchronization !");
			}
			return;
		}
		if(!src.isDirectory()){
			if(log.isInfoEnabled()){
				log.info("web resource directory :"+srcDir+" is not a directory, skip resource synchronization !");
			}
			return;
		}
		try {
			syncDirectory(src, dest, true);
		} catch (Exception e) {
			if(log.isInfoEnabled()){
				log.info("Failed to sync source directory :"+destDir+" to target directory :"+destDir+", skip resource synchronization !",e);
			}
		}
	}
	
    private void syncDirectory(File srcDir, File destDir, boolean preserveFileDate) throws IOException {
        if (destDir.exists()) {
            if (destDir.isDirectory() == false) {
                throw new IOException("Destination '" + destDir + "' exists but is not a directory");
            }
        } else {
            if (destDir.mkdirs() == false) {
                throw new IOException("Destination '" + destDir + "' directory cannot be created");
            }
            if (preserveFileDate) {
                destDir.setLastModified(srcDir.lastModified());
            }
        }
        if (destDir.canWrite() == false) {
            throw new IOException("Destination '" + destDir + "' cannot be written to");
        }
        // recurse
        File[] files = srcDir.listFiles();
        if (files == null) {  // null if security restricted
            throw new IOException("Failed to list contents of " + srcDir);
        }
        for (int i = 0; i < files.length; i++) {
            File copiedFile = new File(destDir, files[i].getName());
            if (files[i].isDirectory()) {
                syncDirectory(files[i], copiedFile, preserveFileDate);
            } else {
            	syncFile(files[i], copiedFile, preserveFileDate);
            }
        }
        files = destDir.listFiles();
        if (files == null) {  // null if security restricted
            throw new IOException("Failed to list contents of " + srcDir);
        }
        for (int i = 0; i < files.length; i++) {
            File srcFile = new File(srcDir, files[i].getName());
            if (!srcFile.exists()) {
                if(files[i].isDirectory()){
                	FileUtils.deleteDirectory(files[i]);
                }else{
                	files[i].delete();
                }
            }
        }
    }

    private void syncFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        if (destFile.exists() && destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' exists but is a directory");
        }
        if(destFile.exists() && (destFile.lastModified() >= srcFile.lastModified())){
        	if(log.isDebugEnabled()){
        		log.debug("dest file :"+destFile.getName()+" is same or newer than srcFile :"+srcFile.getName()+", skip synchronization");
        	}
        }
        FileInputStream input = new FileInputStream(srcFile);
        try {
            FileOutputStream output = new FileOutputStream(destFile);
            try {
                IOUtils.copy(input, output);
            } finally {
                IOUtils.closeQuietly(output);
            }
        } finally {
            IOUtils.closeQuietly(input);
        }

        if (srcFile.length() != destFile.length()) {
            throw new IOException("Failed to copy full contents from '" +
                    srcFile + "' to '" + destFile + "'");
        }
        if (preserveFileDate) {
            destFile.setLastModified(srcFile.lastModified());
        }
    }

    protected void processExtensionRemoved(IExtension ext){
    	List<IWebResource> list = resources.remove(ext.getUniqueIdentifier());
    	if((list == null)||list.isEmpty()){
    		return;
    	}
		IWebResource[] resources = list.toArray(new IWebResource[list.size()]);
		for (int j = 0; j < resources.length; j++) {
			removeResources(resources[j]);
		}
    }
    
	protected void removeResources(IWebResource resource){
		if(resource == null){
			return;
		}
		String destDir = resource.getRealPathOfMapTo();
		try {
			FileUtils.deleteDirectory(new File(destDir));
		} catch (Exception e) {
			if(log.isInfoEnabled()){
				log.info("Failed to delete source directory :"+destDir,e);
			}
		}		
	}
	
	protected void doSynchronize() {
		if(resources.isEmpty()){
			return;
		}
		List<IWebResource>[] allResources = resources.values().toArray(new List[resources.size()]);
		for (int i = 0; i < allResources.length; i++) {
			List<IWebResource> list = allResources[i];
			if((list == null)||list.isEmpty()){
				continue;
			}
			IWebResource[] resources = list.toArray(new IWebResource[list.size()]);
			for (int j = 0; j < resources.length; j++) {
				syncResources(resources[j]);
			}
		}
	}

	
	public void destroy(){
		super.stop();
		if(task != null){
			task.cancel();
		}
		if(timer != null){
			timer.cancel();
		}
		resources.clear();
		resources = null;
	}

}
