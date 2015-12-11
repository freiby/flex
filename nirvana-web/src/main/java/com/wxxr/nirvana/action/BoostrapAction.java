package com.wxxr.nirvana.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.wxxr.nirvana.context.NirvanaServletContext;

/**
 * @author fudapeng
 *
 */
public class BoostrapAction extends ActionSupport {

	private String productName;
	private String pageName;

	public String startProduct() {
		setContext();
		Map<String, Object> params = ActionContext.getContext().getParameters();
		productName = ((String[]) params.get("name"))[0];
		return "start";
	}

	public String startPage() {
		setContext();
		Map<String, Object> params = ActionContext.getContext().getParameters();
		pageName = ((String[]) params.get("page"))[0];
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

	private void setContext() {
		Map<String, Object> map = new HashMap<String, Object>();
		NirvanaServletContext context = new NirvanaServletContext(map);
		NirvanaServletContext.setContext(context);
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpSession session = request.getSession();
		NirvanaServletContext.setRequest(request);
		NirvanaServletContext.setResponse(response);
		NirvanaServletContext.setHttpSession(session);
		NirvanaServletContext.setServletContext(session.getServletContext());
	}
}
