package org.apache.tiles.impl.ext;

import java.util.List;

import org.apache.tiles.Page;
import org.apache.tiles.Product;
import org.apache.tiles.impl.ext.BasicSiteContainer.PageNavigationContext;

public class PageNavigation {
	
	private Page currentPage;
	
	private PageNavigationContext context;
	
	
	public PageNavigation() {
		super();
	}
	
	public void init(PageNavigationContext context){
		this.context = context;
	}

	public void openPage(String pageName){
		Page page = getPage(pageName);
		if(page != null)
		page.open();
	}
	
	public void showPage(String pageName){
		if(currentPage != null)
		currentPage.show();
	}
	
	private Page getPage(String pageName){
		if(pageName == null) return null;
		Product p = context.getCurrentProduct();
		if(p != null){
			List<Page> pages = p.getPages();
			for(Page page : pages){
				if(page.getId().equals(pageName)){
					currentPage = page;
					return page;
				}
			}
		}
		return null;
	}
	
	public void closePage(String pageName){
		if(currentPage.equals(pageName)){
			currentPage = null;
			Page page = getPage(pageName);
			if(page != null)
			page.open();
		}
	}
	
	public Page getCurrentPage(){
		return currentPage;
	}
	
	public String getProductDefautPage(String product) {
		Product pro = context.getCurrentProduct();
		List<Page> pages = pro.getPages();
		for (Page item : pages) {
			if (item.isDefautPage()) {
				String name = item.getName();
				currentPage = item;
				return name;
			}
		}
		return null;
	}
}
