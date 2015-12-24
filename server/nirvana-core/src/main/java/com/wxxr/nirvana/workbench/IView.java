package com.wxxr.nirvana.workbench;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.workbench.impl.ResourceRef;

public interface IView extends IDispatchUI, Cloneable {
	void init(IViewManager manager, IConfigurationElement config, IRender render);

	void destroy();

	ResourceRef[] getResourcesRef();

	String get(String attri);

}
