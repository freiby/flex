package com.wxxr.nirvana.integration;

import org.apache.struts2.tiles.StrutsTilesContainerFactory;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesException;

public class SiteTilesContainerFactory extends StrutsTilesContainerFactory {
	
	/**
     * Creates an immutable Tiles container.
     *
     * @param context The (application) context object.
     * @return The created Tiles container.
     * @throws TilesException If something goes wrong during initialization.
     */
    public TilesContainer createTilesContainer(Object context)
        throws TilesException {
    	BasicSiteContainer container = new BasicSiteContainer();
        initializeContainer(context, container);
        return container;
    }
}
