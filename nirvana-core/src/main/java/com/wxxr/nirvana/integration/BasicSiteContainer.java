package com.wxxr.nirvana.integration;

import java.util.List;

import org.apache.tiles.Page;
import org.apache.tiles.Product;
import org.apache.tiles.definition.DefinitionsFactoryException;
import org.apache.tiles.impl.BasicTilesContainer;

public class BasicSiteContainer extends BasicTilesContainer {
	
	public String getProductDefautPage(String product) {
		try {
			Product pro = definitionsFactory.getDefinitions().getProductByName(product);
			List<Page> pages = pro.getPages();
			for(Page item : pages){
				if(item.isDefautPage()){
					return item.getName();
				}
			}
		} catch (DefinitionsFactoryException e) {
			throw new RuntimeException(e);
		}
		return null;
		
	}
}
