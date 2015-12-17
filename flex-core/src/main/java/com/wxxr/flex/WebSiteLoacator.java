package com.wxxr.flex;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 
 * @author fudapeng
 *
 */
public class WebSiteLoacator {
	private ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();
	public static WebSiteLoacator locator = new WebSiteLoacator();
	private Map<String, WebSiteDef> toSite = new HashMap<String, WebSiteDef>();
	
	public WebSiteDef getSite(String name){
		rwlock.readLock().lock();
		try{
			if(locator.toSite.containsKey(name)){
				return locator.getSite(name);
			}else{
				return null;
			}
		}finally{
			rwlock.readLock().unlock();
		}
		
	}
	
	public void registrySite(String name, WebSiteDef site){
		rwlock.writeLock().lock();
		try{
			locator.toSite.put(name, site);
		}finally{
			rwlock.writeLock().unlock();
		}
		
	}
	
}
