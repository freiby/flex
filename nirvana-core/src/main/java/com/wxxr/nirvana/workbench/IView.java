package com.wxxr.nirvana.workbench;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.workbench.impl.View;

public interface IView extends IDispatchUI, Cloneable {
    void init(IViewManager manager, IConfigurationElement config);
    
    void destroy();
    
    View.ResourceRef[] getResourcesRef();
    
    String get(String attri);

}
