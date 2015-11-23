package org.apache.tiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Views extends Definition implements ILifecycle{
	private String id;
	private String description;
	private Resource resource;
	
	
	
	public List<String> getResourceRefs(){
		List<String> rs = new ArrayList<String>();
		Map<String, Attribute> views = getAttributes();
		Set<String> keys = views.keySet();
		for(String key : keys){
			String resource = views.get(key).getResourceref();
			rs.add(resource);
			
		}
		return rs;
	}
	
	public List<String> getResourcesByType(String type) throws ModelException{
		if(resource == null){
			throw new ModelException("the resource of views is null");
		}
		List<String> rss = new ArrayList<String>();
		Map<String, Attribute> rs = resource.getAttributes();
		Set<String> keys = rs.keySet();
		for(String key : keys){
			if(rs.get(key).getRtype() == null){
				throw new ModelException("resource type is null");
			}
			if(type.equals(rs.get(key).getRtype())){
				rss.add((String) rs.get(key).getValue());
			}
		}
		return rss;
	}
	
	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void open() {
	}
	public void show() {
	}
	public void hiden() {
	}
	public void close() {
	}
	
}
