package org.apache.tiles.definition;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.tiles.Definition;
import org.apache.tiles.Page;
import org.apache.tiles.Product;
import org.apache.tiles.Resource;
import org.apache.tiles.Views;
import org.apache.tiles.impl.ContributeManager;

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
        
//        List<Attribute> attrs = ContributeManager.getSingleton().getContribute("nirvana.tiger");
//        assertNotNull(attrs);
//        assertEquals(3,attrs.size());
        
//        attrs = ContributeManager.getSingleton().getContribute("content");
//        assertNotNull(attrs);
//        assertEquals(0,attrs.size());
        
        Map<String,Definition> products = definitions.getBaseDefinitions();
//        
        assertNotNull(products.get("nirvana"));
        Product product = (Product) products.get("nirvana");
        
        List<Page> pages = product.getPages();
        
        assertEquals(2,pages.size());
        
        boolean defaultPage = false;
        for(Page item : pages){
        	defaultPage = item.isDefautPage();
        	if(defaultPage) break;
        }
        assertTrue(defaultPage);
        
        Views views = (Views) products.get("views");
        String rr = views.getAttribute("chart1").getResourceref();
        assertNotNull(rr);
        
        Resource r = (Resource) products.get("cssjs");
        assertNotNull(r);
        assertEquals(2, r.getAttributes().size());
        
        
        ContributeManager.getSingleton().clear();
    }
}
