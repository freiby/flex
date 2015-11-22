package org.apache.tiles.impl.ext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.ITilesContainerLifecycle;
import org.apache.tiles.Product;
import org.apache.tiles.impl.BasicTilesContainer;

public class BasicSiteContainer extends BasicTilesContainer implements ITilesContainerLifecycle {
	
	private PageNavigation navigation;
	private ProductManager productManager;
	private ContainerContext context;
	
	/**
     * Log instance for all BasicTilesContainer
     * instances.
     */
    private static final Log LOG =
        LogFactory.getLog(BasicSiteContainer.class);
	
	public class PageNavigationContext{
		private ProductManager pManager;
		public PageNavigationContext(ProductManager pManager) {
			super();
			this.pManager = pManager;
		}
		public Product getCurrentProduct(){
			return pManager.getCurrentProduct();
		}
	}
	
	public void afterInitialize(){
		productManager = new ProductManager();
		try {
			context = new ContainerContext(this);
			productManager.init(context);
		} catch (NirvanaException e) {
			LOG.error(e.getMessage());
		}
		PageNavigationContext pc = new PageNavigationContext(productManager);
		navigation = new PageNavigation();
		navigation.init(pc);
	}
	
	public void beforeInitialize(){
		
	}
	
	public PageNavigation getPageNavigation(){
		return navigation;
	}
	
	public ProductManager getProductManager(){
		return productManager;
	}
}
