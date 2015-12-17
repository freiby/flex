package com.wxxr.flex;

import java.util.List;

/**
 * 
 * @author fudapeng
 *
 */
public class WebSiteDef extends WebCoponentDef{
	
	private WebHeaderDef header;
	private WebFooterDef footer;
	private WebNavDef nav;
	private WebSideBarDef sidebar;
	private List<WebPageDef> page;
	private List<WebResource> resources;
	
	public WebHeaderDef getHeader() {
		return header;
	}
	public void setHeader(WebHeaderDef header) {
		this.header = header;
	}
	public WebFooterDef getFooter() {
		return footer;
	}
	public void setFooter(WebFooterDef footer) {
		this.footer = footer;
	}
	public WebNavDef getNav() {
		return nav;
	}
	public void setNav(WebNavDef nav) {
		this.nav = nav;
	}
	public WebSideBarDef getSidebar() {
		return sidebar;
	}
	public void setSidebar(WebSideBarDef sidebar) {
		this.sidebar = sidebar;
	}
	public List<WebPageDef> getPage() {
		return page;
	}
	public void setPage(List<WebPageDef> page) {
		this.page = page;
	}
	public List<WebResource> getResources() {
		return resources;
	}
	public void setResources(List<WebResource> resources) {
		this.resources = resources;
	}
	
}
