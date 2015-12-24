package com.wxxr.nirvana.workbench;

public interface IViewManager {
	/**
	 * Return a view descriptor with the given extension id. If no view exists
	 * with the id return <code>null</code>.
	 * 
	 * @param id
	 *            the id to search for
	 * @return the descriptor or <code>null</code>
	 */
	public IView find(String id);
	
	public IView createViewIfPrimaryIdView(String id)  throws Exception;

	/**
	 * Return a list of views defined in the registry.
	 * 
	 * @return the views. Never <code>null</code>.
	 */
	public IView[] getViews();

	public String[] getViewIds();

	void destroy();

	void start();

}
