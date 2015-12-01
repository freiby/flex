package com.wxxr.nirvana.tiles.integration;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.tiles.ConfiguredServletContext;
import org.apache.struts2.tiles.StrutsTilesListener;
import org.apache.tiles.factory.TilesContainerFactory;


public class NiranaTilesListener extends StrutsTilesListener {
	private static final Logger LOG = LogManager.getLogger(NiranaTilesListener.class);

    private static final Map<String, String> INITN;

    static {
        INITN = new HashMap<String, String>();
        INITN.put(TilesContainerFactory.CONTAINER_FACTORY_INIT_PARAM,
        		SiteTilesContainerFactory.class.getName());
    }


    protected ServletContext decorate(ServletContext context) {
        return new ConfiguredServletContext(context, INITN);
    }
}
