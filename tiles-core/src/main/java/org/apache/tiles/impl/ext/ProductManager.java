package org.apache.tiles.impl.ext;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.tiles.Definition;
import org.apache.tiles.Page;
import org.apache.tiles.Product;
import org.apache.tiles.Resource;
import org.apache.tiles.Views;
import org.apache.tiles.definition.DefinitionsFactoryException;

/**
 * 
 * @author fudapeng
 *
 */
public class ProductManager {
	private Product currentProduct;
	private ContainerContext context;
	private Map<String, Product> products = new HashMap<String, Product>();
	private boolean initFlag = false;
	private Map<String, Views> views = new HashMap<String, Views>();

	public void init(ContainerContext context) throws NirvanaException {
		this.context = context;
		initializeProducts();
		initFlag = true;
	}

	private void initializeProducts() throws NirvanaException {
		Set<Entry<String, Definition>> sets;
		try {
			sets = context.getDefinitionsFactory().getDefinitions()
					.getBaseDefinitions().entrySet();
		} catch (DefinitionsFactoryException e) {
			throw new NirvanaException(e);
		}
		Iterator<Entry<String, Definition>> iterator = sets.iterator();
		while (iterator.hasNext()) {
			Entry<String, Definition> entry = iterator.next();
			if (entry.getValue() instanceof Product) {
				products.put(entry.getKey(), (Product) entry.getValue());
			}

			if (entry.getValue() instanceof Views) {
				views.put(entry.getKey(), (Views) entry.getValue());
			}
		}
		initProduct();
	}

	private void initProduct() throws NirvanaException {
		Iterator<Entry<String, Product>> iterator = products.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, Product> entry = iterator.next();
			Product product = entry.getValue();
			List<Page> pages = product.getPages();
			for (Page item : pages) {
				String viewRef = item.getViewref();
				try {
					Definition def = context.getDefinitionsFactory()
							.getDefinitions().getBaseDefinitions().get(viewRef);
					if (def instanceof Views) {
						Views v = (Views) def;
						List<String> viewResources = v.getResourceRefs();
						initPageResource(v,viewResources);
						item.setView(v);
					}
				} catch (DefinitionsFactoryException e) {
					throw new NirvanaException(e);
				}

			}
		}
	}

	private void initPageResource(Views v,List<String> viewResources) throws NirvanaException {
		Set<Entry<String, Definition>> sets;
		try {
			sets = context.getDefinitionsFactory().getDefinitions()
					.getBaseDefinitions().entrySet();
		} catch (DefinitionsFactoryException e) {
			throw new NirvanaException(e);
		}
		Iterator<Entry<String, Definition>> iterator = sets.iterator();
		while (iterator.hasNext()) {
			Entry<String, Definition> entry = iterator.next();
			if (entry.getValue() instanceof Resource) {
				v.setResource((Resource) entry.getValue());
				break;
			}
		}
	}

	public Product getCurrentProduct() {
		return currentProduct;
	}

	public void setCurrentProduct(String currentProduct)
			throws NirvanaException {
		this.currentProduct = getProductByName(currentProduct);
	}

	/**
	 * add by fudapeng for lookup product
	 * 
	 * @return
	 * @throws NirvanaException
	 */
	Map<String, Product> getProducts() throws NirvanaException {
		if (!initFlag) {
			throw new NirvanaException("not initialize product");
		}
		return products;
	}

	Product getProductByName(String name) throws NirvanaException {
		if (!initFlag) {
			throw new NirvanaException("not initialize product");
		}
		if (products.containsKey(name)) {
			return products.get(name);
		}
		return null;
	}
}
