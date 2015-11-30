package com.wxxr.nirvana.workbench;

import java.util.List;

public interface IWebResourceManager {
	public IWebResource getResource(String id);
	public List<IWebResource> getResources();
}
