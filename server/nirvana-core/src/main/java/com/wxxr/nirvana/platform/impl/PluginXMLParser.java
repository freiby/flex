package com.wxxr.nirvana.platform.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;

public class PluginXMLParser implements ElementParserProvider {

	private static Map<String, Class> elementInstances = new HashMap<String, Class>();
	private static final Log tr = LogFactory.getLog(PluginXMLParser.class);

	static {
		elementInstances.put(CoreConstants.PLUGIN, PluginElementHandler.class);
		elementInstances.put(CoreConstants.EXTENSION,
				ExtensionElementHandler.class);
		elementInstances.put(CoreConstants.EXTENSION_POINT,
				ExtensionPointElementHandler.class);
	}

	public static void registerElementParser(String elementName,
			Class parserClass) {
		if ((StringUtils.isBlank(elementName)) || (parserClass == null)) {
			throw new IllegalArgumentException();
		}
		elementInstances.put(elementName, parserClass);
	}

	public static Class unregisterElementParser(String elementName) {
		return elementInstances.remove(elementName);
	}

	/**
	 * @param elementTag
	 * @return
	 * @see com.hygensoft.common.configure.Configure#getElementParser(java.lang.String)
	 */
	public IConfigureElementHandler getElementParser(String elementTag) {
		Class cls = (Class) elementInstances.get(elementTag);
		if (cls == null) {
			return null;
		}
		try {
			return (IConfigureElementHandler) cls.newInstance();
		} catch (Exception e) {
			tr.error("Failed to create Configure object from class : " + cls, e);
			throw new RuntimeException(
					"Failed to create Configure object from class : " + cls, e);
		}
	}

	/**
	 * Constructor for XMLConfigurator.
	 */
	public PluginConfigurationElement parse(InputStream in) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		XMLConfigurationElementHandler handler = new XMLConfigurationElementHandler(
				this);
		parser.parse(new InputSource(in), handler);
		return (PluginConfigurationElement) handler.getAllConfiure();
	}

}
