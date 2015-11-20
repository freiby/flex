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
	public String startProduct(){
		Map<String, Object> params = ActionContext.getContext().getParameters();
		productName = (String) params.get("name");
		return productName;
	}
}

