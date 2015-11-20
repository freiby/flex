package org.apache.tiles.definition;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.tiles.Attribute;
import org.apache.tiles.Page;
import org.apache.tiles.Product;
import org.apache.tiles.impl.ContributeManager;

import junit.framework.TestCase;

public class TestMyUrlDefinitionsFactory extends TestCase {
	
	public void testDefProductAndPage() throws Exception {
    	DefinitionsFactory factory = new UrlDefinitionsFactory();

        // Set up multiple data sources.
        URL url1 = this.getClass().getClassLoader().getResource(
                "org/apache/tiles/config/productpage.xml");
        assertNotNull("Could not load defs1 file.", url1);

        factory.init(Collections.EMPTY_MAP);
        factory.addSource(url1);
        // Parse files.
        Definitions definitions = factory.readDefinitions();

        assertNotNull("test.def1 definition not found.", definitions.getDefinition("baseLayout"));
        assertNotNull("test.def2 definition not found.", definitions.getDefinition("tiger"));
        assertNotNull("test.def3 definition not found.", definitions.getDefinition("lion"));
        
        List<Attribute> attrs = ContributeManager.getSingleton().getContribute("nirvana.tiger");
        assertNotNull(attrs);
        assertEquals(3,attrs.size());
        
//        attrs = ContributeManager.getSingleton().getContribute("content");
//        assertNotNull(attrs);
//        assertEquals(0,attrs.size());
        
        Map<String,Product> products = definitions.getProducts();
        
        assertEquals(1, products.size());
        Product product = products.entrySet().iterator().next().getValue();
        
        List<Page> pages = product.getPages();
        
        assertEquals(2,pages.size());
        
        ContributeManager.getSingleton().clear();
    }
}
