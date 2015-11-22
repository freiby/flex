package org.apache.tiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Views extends Definition implements ILifecycle{
	private String id;
	private String description;
	private Resource resource;
	
	
	
	public List<String> getResources(){
		List<String> rs = new ArrayList<String>();
		Map<String, Attribute> views = getAttributes();
		Set<String> keys = views.keySet();
		for(String key : keys){
			String resource = views.get(key).getResourceref();
			rs.add(resource);
			
		}
		return rs;
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
