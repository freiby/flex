package org.apache.tiles;

import java.util.ArrayList;
import java.util.List;

public class Page extends Attribute{
	
	private String id;
	private String name;
	private String description;
	private String icon;
	private boolean defautPage;
	
	private List<View> views = new ArrayList<View>();
	
	
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
	
	public void addView(View view){
		if(view != null && !views.contains(view)){
			views.add(view);
		}
	}
	
	public void removeView(View view){
		if(view != null && views.contains(view)){
			views.remove(view);
		}
	}
	
}
