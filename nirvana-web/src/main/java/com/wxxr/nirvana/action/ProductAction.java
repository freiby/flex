package com.wxxr.nirvana.action;

import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author fudapeng
 *
 */
public class ProductAction extends ActionSupport {
	
	private String productName;
	private String pageName;
	public String startProduct(){
		Map<String, Object> params = ActionContext.getContext().getParameters();
		productName = ((String[]) params.get("productName"))[0];
		return "start";
	}
	public String goPage(){
		Map<String, Object> params = ActionContext.getContext().getParameters();
		pageName = ((String[]) params.get("pageName"))[0];
		return "start";
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
}

