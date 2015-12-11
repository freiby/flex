package com.wxxr.nirvana.platform.impl;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.wxxr.nirvana.platform.IConfigurationElement;

public class XMLConfigurationElementHandler extends DefaultHandler {

	protected Object xmlObject;
	protected HandlerWrapper currentState = null;
	protected String xmlString = null;
	protected IConfigureElementHandler topParser = new PluginElementHandler();
	protected ElementParserProvider parserProvider;
	protected StringBuffer buf = new StringBuffer();

	public XMLConfigurationElementHandler(ElementParserProvider provider) {
		super();
		parserProvider = provider;
	}

	class HandlerWrapper extends TreeNode implements IConfigureElementHandler {

		private IConfigureElementHandler parser;

		// private StringBuffer buf = new StringBuffer();
		/**
		 * Constructor for HandlerWrapper.
		 */
		public HandlerWrapper(IConfigureElementHandler parser) {
			super();
			this.parser = parser;
		}

		/**
		 * Gets the parent.
		 * 
		 * @return Returns a XmlModel
		 */
		public HandlerWrapper getParent() {
			return (HandlerWrapper) super.getParentNode();
		}

		/**
		 * Sets the parent.
		 * 
		 * @param parent
		 *            The parent to set
		 */
		public void setParent(HandlerWrapper parent) {
			setParentNode(parent);
		}

		/**
		 * Gets the child.
		 * 
		 * @return Returns a XmlModel
		 */
		public Vector getChildren() {
			return getChildNodes();
		}

		/**
		 * Sets the child.
		 * 
		 * @param child
		 *            The child to set
		 */
		public void addChildParser(HandlerWrapper child) {
			addChildNode(child);
		}

		public HandlerWrapper push(HandlerWrapper state) {
			addChildParser(state);
			state.setParent(this);
			return state;
		}

		public HandlerWrapper pop() {
			return getParent();
		}

		/**
		 * @see com.bmo.dol.frontend.configuration.xmlparser.IConfigureElementHandler#getConfigure()
		 */
		public IConfigurationElement getConfigure() {
			return parser.getConfigure();
		}

		/**
		 * @see com.bmo.dol.frontend.configuration.xmlparser.IConfigureElementHandler#setAttributes(Attributes)
		 */
		public void setAttributes(Attributes attrs) {
			parser.setAttributes(attrs);
		}

		/**
		 * @see com.bmo.dol.frontend.configuration.xmlparser.IConfigureElementHandler#setContents(String)
		 */
		public void addContents(String context) {
			buf.append(context);
		}

		/**
		 * @see com.bmo.dol.frontend.configuration.xmlparser.IConfigureElementHandler#setContents(String)
		 */
		public void addContents(char context) {
			buf.append(context);
		}

		public void setContents() {
			String cont = buf.toString().trim();
			if (cont.length() > 0)
				parser.setContents(cont);
			buf.setLength(0);
		}

		/**
		 * @see com.bmo.dol.frontend.configuration.xmlparser.IConfigureElementHandler#setElementName(String)
		 */
		public void setElementName(String name) {
			parser.setElementName(name);
		}

		/**
		 * @see com.bmo.dol.frontend.configuration.xmlparser.IConfigureElementHandler#addChild(ConfigureObject)
		 */
		public void addChild(IConfigurationElement conf) {
			parser.addChild(conf);
		}

		/**
		 * @see com.bmo.dol.frontend.appfw.configuration.xmlparser.IConfigureElementHandler#getElementName()
		 */
		public String getElementName() {
			return parser.getElementName();
		}

		public String getFullPath() {
			Vector v = new Vector();
			HandlerWrapper wrapper = null;
			for (wrapper = this; wrapper != null; wrapper = (HandlerWrapper) wrapper
					.getParentNode()) {
				v.addElement(wrapper);
			}
			StringBuffer buf = new StringBuffer();
			for (int i = v.size() - 1; i >= 0; i--) {
				wrapper = (HandlerWrapper) v.elementAt(i);
				if (wrapper.getElementName() != null)
					buf.append(wrapper.getElementName()).append('.');
			}
			return buf.toString();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.hygensoft.framework.common.configuration.xmlparser.
		 * IConfigureElementParser#setContents(java.lang.String)
		 */
		public void setContents(String context) {
			setContents();
		}

	}

	/**
	 * @see ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		// super.characters(arg0, arg1, arg2);
		// StringBuffer buf = new StringBuffer();
		for (int i = 0; i < arg2; i++) {
			currentState.addContents(arg0[i + arg1]);
		}
		// String cont = buf.toString().trim();
		// if (cont.length() > 0)
		// currentState.addContents(cont);
		// System.out.println(buf.toString());
	}

	/**
	 * @see ContentHandler#endDocument()
	 */
	public void endDocument() throws SAXException {
		super.endDocument();
	}

	/**
	 * @see ContentHandler#endElement(String, String, String)
	 */
	public void endElement(String arg0, String arg1, String arg2)
			throws SAXException {
		// super.endElement(arg0, arg1, arg2);
		HandlerWrapper state = currentState.pop();
		if (state != null) {
			try {
				currentState.setContents();
				state.addChild(currentState.getConfigure());
			} catch (Exception e) {
				throw new SAXException(e);
			}
		} else {
		}
		currentState = state;
	}

	/**
	 * @see ContentHandler#startDocument()
	 */
	public void startDocument() throws SAXException {
		super.startDocument();
	}

	/**
	 * @see ContentHandler#startElement(String, String, String, Attributes)
	 */
	public void startElement(String uri, String elementName, String qName,
			Attributes attributes) throws SAXException {
		// super.startElement(uri, elementName, qName, attributes);
		if (currentState == null)
			currentState = new HandlerWrapper(topParser);
		else {
			HandlerWrapper p = new HandlerWrapper(getElementParser(qName));
			currentState = currentState.push(p);
		}
		currentState.setElementName(qName);
		currentState.setAttributes(attributes);
	}

	protected IConfigureElementHandler getElementParser(String elementName) {

		IConfigureElementHandler p = null;
		String key = currentState.getFullPath() + elementName;
		if (parserProvider != null) {
			p = parserProvider.getElementParser(key);
			if (p == null) {
				key = elementName;
				p = parserProvider.getElementParser(key);
			}
		}
		if (p != null)
			return p;
		return new DefaultElementHandler();
	}

	public PluginConfigurationElement getAllConfiure() {
		return (PluginConfigurationElement) topParser.getConfigure();
	}
}
