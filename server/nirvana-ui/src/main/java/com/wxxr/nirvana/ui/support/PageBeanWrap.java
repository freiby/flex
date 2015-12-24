package com.wxxr.nirvana.ui.support;

import com.wxxr.nirvana.ContainerAccess;
import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.workbench.IWorkbenchPage;

public class PageBeanWrap {
	private String name;
	private boolean active;
	private String link;
	private IWorkbenchPage page;
	
	public PageBeanWrap(IWorkbenchPage page) throws NirvanaException {
		super();
		this.page = page;
		init();
	}
	
	private void init() throws NirvanaException {
		IWorkbenchPage currentPage = ContainerAccess.getSessionWorkbench().getCurrentPage();
		setLink("startPage.action?page=" + page.getName());
		if(currentPage == null){
			throw new NirvanaException("currentPage is null, can not get active page");
		}
		if(currentPage.getId() .equals(page.getId())){
			setActive(true);
		}
		setName(page.getName());		
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
}
