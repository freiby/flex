package com.wxxr.nirvana.setup;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.media.jfxmediaimpl.platform.Platform;
import com.wxxr.nirvana.ContainerAccess;
import com.wxxr.nirvana.context.NirvanaServletContext;
import com.wxxr.nirvana.deploy.PluginDeployer;
import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.platform.PlatformLocator;
import com.wxxr.nirvana.workbench.IWorkbench;
import com.wxxr.nirvana.workbench.impl.WorkbenchFactory;

/**
 * Listener for the initialization of the Tiles container.
 *
 * @version $Rev$ $Date$
 */
public class NirvanaListener
    implements ServletContextListener {

    /**
     * Log instance.
     */
    protected static final Log log =
        LogFactory.getLog(NirvanaListener.class);

    /**
     * Initialize the TilesContainer and place it
     * into service.
     *
     * @param event The intercepted event.
     */
    public void contextInitialized(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();
        NirvanaServletContext.setContext(new NirvanaServletContext(new HashMap<String, Object>()));
        NirvanaServletContext.setServletContext(servletContext);
        String webRoot = servletContext.getRealPath("/");
        //1 boostrap deployer listener platform
        PluginDeployer deployer = PluginDeployer.getPluginDeployer();
        deployer.init(webRoot);
        deployer.start();
        
        //2 boostrap platform
        try {
        	PlatformLocator.getPlatform().start();
		} catch (Exception e) {
			log.error("platform setup error " + e);
		}
        
        //3 boostrap workbench
        IWorkbench workbench = createWorkbench(servletContext);
        try {
			ContainerAccess.setWorkbench(servletContext, workbench);
		} catch (NirvanaException e) {
			 throw new IllegalStateException("Unable to instantiate workbench.", e);
		}
    }

    private IWorkbench createWorkbench(ServletContext servletContext) {
		return WorkbenchFactory.createWorkbenchFactory();
	}

	/**
     * Remove the tiles container from service.
     *
     * @param event The intercepted event.
     */
    public void contextDestroyed(ServletContextEvent event) {
    	try {
			ContainerAccess.setContainer(null);
			PluginDeployer.getPluginDeployer().stop();
		} catch (NirvanaException e) {
			log.warn("Unable to remove tiles container from service.");
		}
    }

}
