package com.wxxr.nirvana;

import com.wxxr.nirvana.workbench.IWebResource;
import com.wxxr.nirvana.workbench.impl.UIComponent;

/**
 * 获取资源
 * 
 * @author fudapeng
 *
 */
public interface IWebResourceContainer extends IContainer {

	public static String HEADER_POINT = "header";
	public static String FOOTER_POINT = "footer";
	public static String PREFIX_BEFORE = "before";
	public static String PREFIX_AFTER = "after";

	/**
	 * 得到某些点的资源
	 * 
	 * @param point
	 * @return
	 */
	IWebResource[] getResources(String point);

	WebResourceInfo[] getComponentResource(UIComponent component);

	public class WebResourceInfo {
		private IWebResource r;
		private String point;

		public WebResourceInfo(IWebResource r, String point) {
			super();
			this.r = r;
			this.point = point;
		}

		public IWebResource getR() {
			return r;
		}

		public String getPoint() {
			return point;
		}

	}
}
