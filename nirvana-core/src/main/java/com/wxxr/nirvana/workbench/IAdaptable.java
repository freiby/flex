package com.wxxr.nirvana.workbench;

public interface IAdaptable {
	<T> T get(Class<T> type,Object adaptableObject);
}
