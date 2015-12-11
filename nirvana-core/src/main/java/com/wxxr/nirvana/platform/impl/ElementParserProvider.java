/*
 * Created on 2004-8-4
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.wxxr.nirvana.platform.impl;

/**
 * @author Neil
 *
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public interface ElementParserProvider {
	IConfigureElementHandler getElementParser(String name);
}
