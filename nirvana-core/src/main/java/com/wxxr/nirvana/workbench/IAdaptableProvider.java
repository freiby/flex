package com.wxxr.nirvana.workbench;

import java.util.Map;

/**
 * @author Neil Lin
 *
 */
public interface IAdaptableProvider {
	Class<?> getAdaptableClass();
	Object getAdaptable();
	Map<String, Object> getParams();
}