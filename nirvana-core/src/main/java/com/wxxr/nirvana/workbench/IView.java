package com.wxxr.nirvana.workbench;

import com.wxxr.nirvana.platform.IConfigurationElement;

public interface IView extends IUIContributionItem, Cloneable {



    void init(IViewManager manager, IConfigurationElement config);
    
    void destroy();
    
    String getViewURI();

}
