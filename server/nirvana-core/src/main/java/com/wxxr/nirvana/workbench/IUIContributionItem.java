/*
 * @(#)IUIContributionItem.java	 2007-11-1
 *
 * Copyright 2004-2007 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.nirvana.workbench;

/**
 * @author fudapeng
 *
 */
public interface IUIContributionItem extends IContributionItem {
	/**
	 * Returns a unique identifier for this item.
	 *
	 * @return the item id
	 */
	public String getId();

	/**
	 * Returns the tool tip text for this item.
	 *
	 * @return the tool tip text, or <code>null</code> if none
	 */
	public String getToolTipText();

	/**
	 * Returns the hover image for this item as an image uri.
	 *
	 * @return the image uri, or <code>null</code> if this item has no such
	 *         image
	 */
	String getSubcontextURI();

	String getUniqueIndentifier();

	String getName();

	String getDescription();

	IRender getRender();

}
