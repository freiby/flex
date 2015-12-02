package com.wxxr.nirvana.ui;

import java.util.Map;

import com.wxxr.nirvana.ContainerAccess;
import com.wxxr.nirvana.IUIComponentContext;
import com.wxxr.nirvana.theme.ITheme;
import com.wxxr.nirvana.workbench.IContributionItem;
import com.wxxr.nirvana.workbench.IWorkbench;
import com.wxxr.nirvana.workbench.impl.Product;
import com.wxxr.nirvana.workbench.impl.Product.PageRef;
import com.wxxr.nirvana.workbench.impl.UIComponent;

public class ProductContext extends UIComponentContext{

	private Product product;
	public ProductContext(IContributionItem uiContribute) {
		super(uiContribute);
		product = (Product) uiContribute;
	}

	public IUIComponentContext getChildUIContext(String componentName) {
		IUIComponentContext result = super.getChildUIContext(componentName);
		if(result != null){
			return result;
		}
		if(componentName.equals(IUIComponentContext.THEME)){
			String theme = product.getTheme();
			IWorkbench workbench = ContainerAccess.getWorkbench();
			ITheme th = workbench.getThemeManager().getTheme(theme);
			result = new ThemeContext(th);
			result.init(this,null);
			addChildContext(IUIComponentContext.THEME, result);
		}else if(componentName.equals(IUIComponentContext.PAGE)){
			PageRef[] pagerefs = product.getAllPages();
			IWorkbench workbench = ContainerAccess.getWorkbench();
			result = new PageContext(null,pagerefs);
			result.init(this,null);
			addChildContext(IUIComponentContext.PAGE, result);
		}
		return result;
	}

	public UIComponent getCurrentComponent(Map<String,Object> parameters) {
		return product;
	}

}
