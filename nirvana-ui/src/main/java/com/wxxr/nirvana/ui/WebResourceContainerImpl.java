package com.wxxr.nirvana.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wxxr.nirvana.ContainerAccess;
import com.wxxr.nirvana.IWebResourceContainer;
import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.workbench.IProduct;
import com.wxxr.nirvana.workbench.IView;
import com.wxxr.nirvana.workbench.IWebResource;
import com.wxxr.nirvana.workbench.IWorkbench;
import com.wxxr.nirvana.workbench.IWorkbenchPage;
import com.wxxr.nirvana.workbench.impl.ResourceRef;
import com.wxxr.nirvana.workbench.impl.UIComponent;
import com.wxxr.nirvana.workbench.impl.WorkbenchPage.ViewRef;

/**
 * 
 * @author fudapeng
 *
 */
public class WebResourceContainerImpl implements IWebResourceContainer {
	private IWorkbenchPage page;
	private IProduct product;

	private IWebResource[] resources;

	private Map<String, WebResourceInfo[]> viewResourceMap = new HashMap<String, WebResourceInfo[]>();

	private Map<String, List<IWebResource>> pointMap = new HashMap<String, List<IWebResource>>();

	private IWebResource[] themeResources = null;

	private ResourceRef[] themeRefs = null;

	public void init(HttpServletRequest request, HttpServletResponse response)
			throws NirvanaException {
		page = ContainerAccess.getSessionWorkbench().getCurrentPage();
		product = ContainerAccess.getSessionWorkbench().getCurrentProduct();
		String themeref = product.getTheme();
		themeRefs = ContainerAccess.getServiceManager().getThemeManager().getTheme(themeref)
				.getResourceRefs();
		if (themeRefs != null) {
			this.themeResources = new IWebResource[themeRefs.length];
			for (int i = 0; i < themeRefs.length; i++) {
				this.themeResources[i] = ContainerAccess.getServiceManager().getWebResourceManager()
						.getResource(themeRefs[i].getRef());
				if(this.themeResources[i] == null){
					throw new NirvanaException("theme import resource " + themeRefs[i].getRef() + " not found resource! please cheouk out webplugin.xml!!!");
				}
				collection(themeRefs[i].getPoint(), this.themeResources[i]);
			}
		}
		ViewRef[] viewrefs = page.getAllViewRefs();
		for (ViewRef viewref : viewrefs) {
			IView view = page.getViewsById(viewref.getId());
			ResourceRef[] viewRrefs = view.getResourcesRef();
			if (viewRrefs == null)
				continue;
			WebResourceInfo[] infos = new WebResourceInfo[viewRrefs.length];
			for (int i = 0; i < viewRrefs.length; i++) {
				IWebResource webr = ContainerAccess.getServiceManager().getWebResourceManager()
						.getResource(viewRrefs[i].getRef());
				WebResourceInfo info = new WebResourceInfo(webr,
						viewRrefs[i].getPoint());
				infos[i] = info;
				if(webr == null){
					throw new NirvanaException("view import resource " + viewRrefs[i].getRef() + " not found resource! please cheouk out webplugin.xml!!!");
				}
				collection(viewRrefs[i].getPoint(), webr);
			}
			viewResourceMap.put(view.getUniqueIndentifier(), infos);
		}
	}

	private void collection(String point, IWebResource r) {
		if (!pointMap.containsKey(point)) {
			List<IWebResource> rs = new ArrayList<IWebResource>();
			rs.add(r);
			pointMap.put(point, rs);
		} else {
			List<IWebResource> webrs = pointMap.get(point);
			webrs.add(r);
		}
	}

	public void reset(HttpServletRequest request, HttpServletResponse response)
			throws NirvanaException {

	}

	public void destroy() {
		viewResourceMap.clear();
		pointMap.clear();
		themeRefs = null;
		themeResources = null;
		resources = null;
		page = null;
		product = null;
	}

	public IWebResource[] getResources(String point) {
		if (pointMap.containsKey(point)) {
			return pointMap.get(point).toArray(
					new IWebResource[pointMap.get(point).size()]);
		}
		return null;
	}

	public WebResourceInfo[] getComponentResource(UIComponent component) {
		String uid = component.getUniqueIndentifier();
		if (viewResourceMap.containsKey(uid)) {
			return viewResourceMap.get(uid);
		}
		return null;
	}

	private class Ref {
		public ResourceRef[] refs;
		public IWebResource[] resources;
	}

}
