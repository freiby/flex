package org.apache.tiles.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.tiles.Attribute;

public class ContributeManager {
	
	private class ContributeProvider{
		
		private Map<String,Def> contributeToDef = new HashMap<String,Def>();
		
		public List<Attribute> getAttribute(String to){
			Path p = parsePath(to);
			if(p.def != null && contributeToDef.containsKey(p.def)){
				List<Attribute> attrsDef = contributeToDef.get(p.def).getAttributes();
				
				if(p.attr != null){
					Def d = contributeToDef.get(p.def);
					Attr at = d.getAttri(p.attr, false);
					if(at != null){
						List<Attribute> attrsAttri = at.getAttributes();
						return attrsAttri;
					}
					
				}else{
					return attrsDef;
				}
			}
			return null;
		}
		
		public void parseContribute(String to, Attribute attr,boolean operation){// true -- add false--- remove
			Path p = parsePath(to);
			if(p.attr == null && p.def != null){// contribute to  def
				Def d = getDef(p.def,operation);
				if(operation){
					d.addAttribute(attr);
				}else{
					if(d != null){
						d.removeAttribute(attr);
					}
				}
			}else if(p.attr != null && p.def != null){// contribute to  attr
				Def d = getDef(p.def,operation);
				if(operation){
					Attr at = d.getAttri(p.attr,operation);
					at.addAttribute(attr);
				}else if(d != null){
					Attr at = d.getAttri(p.attr,!operation);
					at.removeAttribute(attr);
				}
			}
			
		}
		
		
		private Def getDef(String name,boolean create){
			if(contributeToDef.containsKey(name)){
				return contributeToDef.get(name);
			}else if(create){
				Def df = new Def();
				df.name = name;
				contributeToDef.put(name, df);
				return df;
			}
			return null;
		}
		
		private class AttriContext{
			private List<Attribute> attrs = new ArrayList<Attribute>();
			public void addAttribute(Attribute attr){
				if(!attrs.contains(attr)){
					attrs.add(attr);
				}
			}
			
			public void removeAttribute(Attribute attr){
				if(attrs.contains(attr)){
					attrs.remove(attr);
				}
			}
			
			public List<Attribute> getAttributes(){
				return attrs;
			}
		}
		
		private class Def extends AttriContext{
			private String name;
			private Map<String,Attr> ats = new HashMap<String,Attr>();
			
			private Attr getAttri(String name,boolean create){
				if(ats.containsKey(name)){
					return ats.get(name);
				}else if(create){
					Attr at = new Attr();
					at.name = name;
					ats.put(name, at);
					return at;
				}
				return null;
			}
			
		}
		
		private class Attr extends AttriContext{
			private String name;
		}
		
	}
	private static ContributeManager singleton;
	
	
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	
	private ContributeProvider cp = new ContributeProvider();
	
	public static synchronized ContributeManager getSingleton(){
		if(singleton == null){
			singleton = new ContributeManager();
		}
		return singleton;
	}
	
	private class Path{
		public String def;
		public String attr;
	}
	
	public Path parsePath(String path){
		Path p = new Path();
		if(path.indexOf(".") > -1){//contribute attri
			p.def = path.split("\\.")[0];
			p.attr = path.split("\\.")[1];
		}else{//contribute def
			p.def = path;
		}
		return p;
	}
	
	public void registryContribute(String to, Attribute attr){
		Lock lc = lock.writeLock();
		try{
			lc.lock();
			cp.parseContribute(to, attr, true);
		}finally{
			lc.unlock();
		}
		
	}
	
	public void unregistryContribute(String to, Attribute attr){
		Lock lc = lock.writeLock();
		try{
			lc.lock();
			cp.parseContribute(to, attr, false);
		}finally{
			lc.unlock();
		}
		
	}
	
	public List<Attribute> getContribute(String to){
		Lock lc = lock.readLock();
		try{
			lc.lock();
			return cp.getAttribute(to);
		}finally{
			lc.unlock();
		}
	}
}
