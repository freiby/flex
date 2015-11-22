package org.apache.tiles;

public class View extends Definition implements ILifecycle{
	private String id;
	private String description;
	private String icon;
	
	private String contribute;
	
	
	
	
	public String getContribute() {
		return contribute;
	}
	public void setContribute(String contribute) {
		this.contribute = contribute;
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
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
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
