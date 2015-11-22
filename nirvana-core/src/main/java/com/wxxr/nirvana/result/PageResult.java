package com.wxxr.nirvana.result;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.views.tiles.TilesResult;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.impl.ext.BasicSiteContainer;

import com.opensymphony.xwork2.ActionInvocation;

public class PageResult extends TilesResult {
	
	public void doExecute(String location, ActionInvocation invocation) throws Exception {
		ServletContext servletContext = ServletActionContext.getServletContext();
        BasicSiteContainer container = (BasicSiteContainer) TilesAccess.getContainer(servletContext);
		setLocation(location);

        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        openPage(location,container);
        container.render(location, request, response);
        showPage(location,container);
    }
	
	private void openPage(String location,BasicSiteContainer container){
		container.getPageNavigation().openPage(location);
	}
	
	private void showPage(String location,BasicSiteContainer container){
		container.getPageNavigation().showPage(location);
	}
}
