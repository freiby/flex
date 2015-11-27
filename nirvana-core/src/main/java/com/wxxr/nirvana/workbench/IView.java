package com.wxxr.nirvana.workbench;

import com.wxxr.nirvana.platform.IConfigurationElement;

public interface IView extends IUIContributionItem, Cloneable {


    /**
     * Returns whether this view allows multiple instances.
     * 
     * @return whether this view allows multiple instances
     */
    public boolean getAllowMultiple();

    void init(IViewManager manager, IConfigurationElement config);
    
    void applyConfigure(IConfigurationElement data);
    
    void destroy();
    
    String getViewURI();
    
    String getViewStylesURI();
    
    Object clone();
    
    String getInitJs();
    

}
