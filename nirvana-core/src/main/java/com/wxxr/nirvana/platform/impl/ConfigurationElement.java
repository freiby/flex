package com.wxxr.nirvana.platform.impl;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.wxxr.nirvana.platform.CoreException;
import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.platform.IContributor;
import com.wxxr.nirvana.platform.IExecutableExtension;
import com.wxxr.nirvana.platform.IExecutableExtensionFactory;
import com.wxxr.nirvana.platform.InvalidRegistryObjectException;

public class ConfigurationElement implements IConfigurationElement {
	private static final IConfigurationElement[] NONE_CHILDREN = new IConfigurationElement[0];
	private static final String[] EMPTY_STRING = new String[0];
	private Element elem;
	private IConfigurationElement[] children;
	private IConfigurationElement parent;
	private String[] attributeNames;
	private String namespace;
	private String extensionUniqueId;

	public ConfigurationElement(Element elem, IConfigurationElement parent) {
		this.elem = elem;
		this.parent = parent;
	}

	void throwException(String message, Throwable exception)
			throws CoreException {
		throw new CoreException();
	}

	public Object createExecutableExtension(String attributeName)
			throws CoreException {

		String prop = null;
		String executable;
		String contributorName = null;
		String className = null;
		Object initData = null;
		int i;

		if (attributeName != null)
			prop = getAttribute(attributeName);
		else {
			// property not specified, try as element value
			prop = getValue();
			if (prop != null) {
				prop = prop.trim();
				if (prop.equals("")) //$NON-NLS-1$
					prop = null;
			}
		}

		if (prop == null) {
			// property not defined, try as a child element
			IConfigurationElement[] exec;
			IConfigurationElement[] parms;
			IConfigurationElement element;
			Hashtable initParms;
			String pname;

			exec = getChildren(attributeName);
			if (exec.length != 0) {
				element = exec[0]; // assumes single definition
				contributorName = element.getAttribute("plugin"); //$NON-NLS-1$
				className = element.getAttribute("class"); //$NON-NLS-1$
				parms = element.getChildren("parameter"); //$NON-NLS-1$
				if (parms.length != 0) {
					initParms = new Hashtable(parms.length + 1);
					for (i = 0; i < parms.length; i++) {
						pname = parms[i].getAttribute("name"); //$NON-NLS-1$
						if (pname != null)
							initParms
									.put(pname, parms[i].getAttribute("value")); //$NON-NLS-1$
					}
					if (!initParms.isEmpty())
						initData = initParms;
				}
			} else {
				// specified name is not a simple attribute nor child element
				throwException("", null);
			}
		} else {
			// simple property or element value, parse it into its components
			i = prop.indexOf(':');
			if (i != -1) {
				executable = prop.substring(0, i).trim();
				initData = prop.substring(i + 1).trim();
			} else
				executable = prop;

			i = executable.indexOf('/');
			if (i != -1) {
				contributorName = executable.substring(0, i).trim();
				className = executable.substring(i + 1).trim();
			} else
				className = executable;
		}

		// create a new instance
		IContributor defaultContributor = null; // Platform.getPlatformContext().getObjectFactory().getContributor(contributorId);
		Object result = null;// Platform.getPlatformContext().getObjectFactory().createExecutableExtension(defaultContributor,
								// className, contributorName);

		// Check if we have extension adapter and initialize;
		// Make the call even if the initialization string is null
		try {
			// We need to take into account both "old" and "new" style
			// executable extensions
			if (result instanceof IExecutableExtension)
				((IExecutableExtension) result).setInitializationData(this,
						attributeName, initData);
		} catch (CoreException ce) {
			// user code threw exception
			throw ce;
		} catch (Exception te) {
			// user code caused exception
			throwException("", te);
		}

		// Deal with executable extension factories.
		if (result instanceof IExecutableExtensionFactory)
			result = ((IExecutableExtensionFactory) result).create();

		return result;
	}

	public String getAttribute(String name)
			throws InvalidRegistryObjectException {
		return elem.getAttribute(name);
	}

	public Map<String, String> getAttrs() {
		String[] aNames = getAttributeNames();
		Map<String, String> attrsMap = new HashMap<String, String>();
		for (String string : aNames) {
			attrsMap.put(string, getAttribute(string));
		}
		return attrsMap;
	}

	public String[] getAttributeNames() throws InvalidRegistryObjectException {
		if (attributeNames == null) {
			initAttributeNames();
		}
		return attributeNames;
	}

	/**
	 * 
	 */
	private void initAttributeNames() {
		NamedNodeMap attrs = elem.getAttributes();
		int size = attrs.getLength();
		if ((attrs != null) && (size > 0)) {
			LinkedList<String> list = new LinkedList<String>();
			for (int i = 0; i < size; i++) {
				Attr attr = (Attr) attrs.item(i);
				if (attr != null) {
					list.add(attr.getName());
				}
			}
			if (list.isEmpty()) {
				attributeNames = EMPTY_STRING;
			} else {
				attributeNames = list.toArray(new String[list.size()]);
			}
		}
	}

	public IConfigurationElement[] getChildren()
			throws InvalidRegistryObjectException {
		if (!elem.hasChildNodes()) {
			return NONE_CHILDREN;
		}
		if (children == null) {
			initChildren();
		}
		return children;
	}

	private void initChildren() {
		LinkedList<ConfigurationElement> list = new LinkedList<ConfigurationElement>();
		for (Node el = elem.getFirstChild(); el != null; el = el
				.getNextSibling()) {
			if (el instanceof Element) {
				list.add(new ConfigurationElement((Element) el, this));
			}
		}
		if (list.isEmpty()) {
			children = NONE_CHILDREN;
		} else {
			children = list.toArray(new ConfigurationElement[list.size()]);
		}
	}

	public IConfigurationElement[] getChildren(String name)
			throws InvalidRegistryObjectException {
		IConfigurationElement[] all = getChildren();
		int len = all.length;
		if (len == 0) {
			return NONE_CHILDREN;
		}
		LinkedList<IConfigurationElement> list = new LinkedList<IConfigurationElement>();
		for (int i = 0; i < len; i++) {
			if (name.equalsIgnoreCase(all[i].getName())) {
				list.add(all[i]);
			}
		}
		if (!list.isEmpty()) {
			return list.toArray(new IConfigurationElement[list.size()]);
		}
		return NONE_CHILDREN;
	}

	public IContributor getContributor() throws InvalidRegistryObjectException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDeclaringExtensionId() {
		return null;
	}

	public String getName() throws InvalidRegistryObjectException {
		return elem.getTagName();
	}

	public String getNamespaceIdentifier()
			throws InvalidRegistryObjectException {
		if (namespace == null) {
			initNamespace();
		}
		return namespace;
	}

	private void initNamespace() {
		IConfigurationElement plugin = null;
		for (IConfigurationElement p = this; p != null; p = (IConfigurationElement) p
				.getParent()) {
			plugin = p;
		}
		namespace = plugin.getAttribute(CoreConstants.PLUGIN_ID);
	}

	private IConfigurationElement getExtensionElement() {
		for (IConfigurationElement p = this; p != null; p = (IConfigurationElement) p
				.getParent()) {
			if (CoreConstants.EXTENSION.equalsIgnoreCase(p.getName())) {
				return p;
			}
		}
		return null;
	}

	public Object getParent() throws InvalidRegistryObjectException {
		return parent;
	}

	public String getValue() throws InvalidRegistryObjectException {
		return elem.getNodeValue();
	}

	public boolean isValid() {
		return true;
	}

	/**
	 * @return the extensionUniqueId
	 */
	public String getExtensionUniqueId() {
		return extensionUniqueId;
	}

	/**
	 * @param extensionUniqueId
	 *            the extensionUniqueId to set
	 */
	public void setExtensionUniqueId(String extensionUniqueId) {
		this.extensionUniqueId = extensionUniqueId;
	}

	public boolean hasAttribute(String name) {
		// TODO Auto-generated method stub
		return false;
	}

}
