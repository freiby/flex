package com.wxxr.nirvana.result;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.views.tiles.TilesResult;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.impl.ext.BasicSiteContainer;
import org.apache.tiles.impl.ext.NirvanaException;

import com.opensymphony.xwork2.ActionInvocation;

public class ProductResult extends TilesResult {
	
	public void doExecute(String location, ActionInvocation invocation) throws Exception {
		ServletContext servletContext = ServletActionContext.getServletContext();
        BasicSiteContainer container = (BasicSiteContainer) TilesAccess.getContainer(servletContext);
        container.getProductManager().setCurrentProduct(location);
		location = getPage(location,container);
		if(location == null || "".equals(location)){
			throw new NirvanaException("no default page");
		}
		setLocation(location);
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        container.render(location, request, response);
    }
	
	
	private String getPage(String product,BasicSiteContainer container){
		return container.getPageNavigation().getProductDefautPage(product);
	}
}
