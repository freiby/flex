package com.wxxr.nirvana.result;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.views.tiles.TilesResult;
import org.apache.tiles.Product;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;

import com.opensymphony.xwork2.ActionInvocation;
import com.wxxr.nirvana.integration.BasicSiteContainer;

public class ProductResult extends TilesResult {
	
	public void doExecute(String location, ActionInvocation invocation) throws Exception {
		location = getPage(location);
		
		setLocation(location);

        ServletContext servletContext = ServletActionContext.getServletContext();
        TilesContainer container = TilesAccess.getContainer(servletContext);

        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        container.render(location, request, response);
    }
	
	private String getPage(String product){
		ServletContext servletContext = ServletActionContext.getServletContext();
        BasicSiteContainer container = (BasicSiteContainer) TilesAccess.getContainer(servletContext);
		return container.getProductDefautPage(product);
	}
}
