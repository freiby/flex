package org.apache.tiles;


public class Page extends Attribute implements ILifecycle{
	
	private String id;
	private String name;
	private String description;
	private String icon;
	private boolean defautPage;
	private String viewref;
	
	private String tamplateref;
	private Resource pageResource;
	
	private Views view;
	
	public Resource getResource(){
		return pageResource;
	}
	
	public Views getView() {
		return view;
	}
	public void setView(Views view) {
		this.view = view;
	}
	public String getViewref() {
		return viewref;
	}
	public void setViewref(String viewref) {
		this.viewref = viewref;
	}
	public String getTamplateref() {
		return tamplateref;
	}
	public void setTamplateref(String tamplateref) {
		this.tamplateref = tamplateref;
	}
	public boolean isDefautPage() {
		return defautPage;
	}
	public void setDefautPage(boolean defautPage) {
		this.defautPage = defautPage;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
