package com.wxxr.nirvana.workbench.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.platform.IExtension;
import com.wxxr.nirvana.workbench.IProduct;
import com.wxxr.nirvana.workbench.IProductManager;
import com.wxxr.nirvana.workbench.UIConstants;
import com.wxxr.nirvana.workbench.config.BaseExtensionPointManager;

public class ProductManager extends BaseExtensionPointManager implements
		IProductManager {

	private static final String PRODUCT_ELEMENT_NAME = "product";

	private static final String ATT_PRODUCT_ID = "id";
	
	private Log log = LogFactory.getLog(ProductManager.class);
	
//	private List<IProduct> products = new ArrayList<IProduct>();
	
	private Map<String, IProduct> products = new HashMap<String, IProduct>();
	
	private Product currentProduct;

	public ProductManager() {
		super(UIConstants.UI_NAMESPACE,UIConstants.EXTENSION_POINT_PRODUCTS);
	}

	public IProduct[] getAllProducts() {
		return products.values().toArray(new IProduct[products.size()]);
	}

	public IProduct getProductById(String id) {
		if(products.containsKey(id))
			return products.get(id);
		return null;
	}

	@Override
	protected void processExtensionAdded(IExtension ext) {
		IConfigurationElement[] configs = ext.getConfigurationElements();
		for (int i = 0; i < configs.length; i++) {
			IConfigurationElement elem = configs[i];
			if((elem != null) && PRODUCT_ELEMENT_NAME.equalsIgnoreCase(elem.getName())){
				try {
					IProduct product = createNewPrduct(elem);
				} catch (Exception e) {
					log.error("Failed to create layout from configuration element :"+ elem, e);
				}
			}
		}		
	}

	private IProduct createNewPrduct(IConfigurationElement elem) {
		String pid = elem.getNamespaceIdentifier() + "." + elem.getAttribute(ATT_PRODUCT_ID);
		IProduct product = getProductById(pid);
		if (product != null) {
			return product;
		}
		product = new Product();
		product.init(this, elem);
		synchronized (products) {
			products.put(pid, product);
		}
		return product;
	}

	@Override
	protected void processExtensionRemoved(IExtension ext) {
		IConfigurationElement[] configs = ext.getConfigurationElements();
		for (int i = 0; i < configs.length; i++) {
			IConfigurationElement elem = configs[i];
			if((elem != null) && PRODUCT_ELEMENT_NAME.equalsIgnoreCase(elem.getName())){
				products.remove(elem.getAttribute(ATT_PRODUCT_ID));
				destroyProduct(elem.getAttribute(ATT_PRODUCT_ID));
			}
		}
	}

	private void destroyProduct(String pid) {
		IProduct pro = getProductById(pid);
		if (pro == null) {
			return;
		}
		pro.destroy();
	}

	public IProduct getProductByName(String name) {
		Set<String> keys = products.keySet();
		Iterator<String> keyIt = keys.iterator();
		while(keyIt.hasNext()){
			String key = keyIt.next();
			String hname = products.get(key).getName();
			if(name.equalsIgnoreCase(hname)){
				return products.get(name);
			}
		}
		return null;
	}

}
