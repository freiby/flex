package com.wxxr.nirvana.platform.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.wxxr.nirvana.util.ResProperties;


public class ResourceBundleImpl extends ResourceBundle {

	private ResProperties resources;
	/**
	 * Constructor for MessageResourceBundle.
	 */
	public ResourceBundleImpl(InputStream ins, String characterEncoding) throws IOException {
		super();
		resources = new ResProperties();
		resources.load(ins,characterEncoding);
	}

	/**
	 * @see java.util.ResourceBundle#handleGetObject(java.lang.String)
	 */
	protected Object handleGetObject(String arg0)
		throws MissingResourceException {
		return resources.get(arg0);
	}

	/**
	 * @see java.util.ResourceBundle#getKeys()
	 */
	public Enumeration getKeys() {

		Enumeration enumeration = null;
		final Iterator thisKeys = resources.keySet().iterator();
		final Enumeration pKeys =
			(super.parent != null) ? super.parent.getKeys() : null;
		enumeration = new Enumeration() {

			Object temp;

			public boolean hasMoreElements() {
				if (temp == null)
					nextElement();
				return temp != null;
			}

			public Object nextElement() {
				Object obj = temp;
				if (thisKeys.hasNext())
					temp = thisKeys.next();
				else
					for (temp = null;
						temp == null
							&& (pKeys != null)
							&& pKeys.hasMoreElements();
						) {
						temp = pKeys.nextElement();
						if (resources.containsKey(temp))
							temp = null;
					}

				return obj;
			}
		};
		return enumeration;
	}

}
