package com.wxxr.nirvana.workbench.impl;

import java.util.Arrays;
import java.util.Comparator;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.workbench.IProduct;
import com.wxxr.nirvana.workbench.IProductManager;

public class Product extends UIComponent implements IProduct {

	private static final String PAGE_ELEMENT = "page";
	private static final String ATT_DEFAULT_PAGE = "defaulPage";
	private static final String ATT_INDEX = "index";
	private static final String ATT_LAYOUT = "layout";
	private String theme;
	private PageRef[] pagerefs;

	private String[] pageIds;

	private String defaultPage;

	public String getTheme() {
		return theme;
	}

	public void init(IProductManager manager, IConfigurationElement config) {
		super.init(config, null);
		theme = config.getAttribute("themeref");
		initPages(config);
	}

	private void initPages(IConfigurationElement config) {
		IConfigurationElement[] pageConfigs = config.getChildren(PAGE_ELEMENT);
		pagerefs = new PageRef[pageConfigs.length];
		for (int j = 0; j < pageConfigs.length; j++) {
			PageRef ref = new PageRef();
			IConfigurationElement viewConfig = pageConfigs[j];
			String pageid = viewConfig.getAttribute(UIComponent.ATT_REF);
			ref.id = pageid;
			ref.defaultPage = viewConfig.getAttribute(ATT_DEFAULT_PAGE) == null ? false
					: true;
			ref.index = Integer.valueOf(viewConfig.getAttribute(ATT_INDEX));
			ref.layout = viewConfig.getAttribute(ATT_LAYOUT);
			if (ref.defaultPage) {
				this.defaultPage = ref.id;
			}
			pagerefs[j] = ref;
		}
		Arrays.sort(pagerefs, new PageRef());
		// pageIds = new String[pagerefs.length];
		// int i = 0;
		// for(;i<pagerefs.length; i++){
		// pageIds[i] = pagerefs[i].id;
		// i++;
		// }
	}

	public class PageRef implements Comparator<PageRef> {
		public String id;
		public int index;
		public boolean defaultPage;
		public String layout;

		public int compare(PageRef o1, PageRef o2) {
			return o1.index - o2.index;
		}
	}

	public void destroy() {
	}

	public String getDefaultPage() {
		return defaultPage;
	}

	public PageRef[] getAllPages() {
		return pagerefs;
	}

}
