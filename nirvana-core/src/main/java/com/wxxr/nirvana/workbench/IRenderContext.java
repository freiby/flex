package com.wxxr.nirvana.workbench;


public interface IRenderContext {
	<T> T get(Class<T> type);
}
