/**
 * 
 */
package org.apache.tiles;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fudapeng
 *
 */
public class Product extends Definition{
	
	public static final String CURRENT = "_CURRENT_PRODUCT_";
	/**
	 * 1:*
	 */
	private List<Page> pages = new ArrayList<Page>();
	
	private String id;
	private String name;
	private String description;
	
	private String currentPage;
	
	
	
	
	
	public String getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}
	public List<Page> getPages() {
		return pages;
	}
	public void setPages(List<Page> pages) {
		this.pages = pages;
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
	
	public void addPage(Page page){
		if(page != null && !pages.contains(page)){
			pages.add(page);
		}
	}
	
	public void removePage(Page page){
		if(page != null && pages.contains(page)){
			pages.remove(page);
		}
	}
	
}
