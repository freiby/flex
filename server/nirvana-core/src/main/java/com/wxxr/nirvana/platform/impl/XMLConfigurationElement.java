package com.wxxr.nirvana.platform.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;

import com.wxxr.nirvana.platform.CoreException;
import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.platform.IContributor;
import com.wxxr.nirvana.platform.InvalidRegistryObjectException;

/**
 * @author Neil Lin
 * @version 1.0
 * @since Auguest 4th, 2002
 */
public class XMLConfigurationElement implements IConfigurationElement,
		Comparable<XMLConfigurationElement> {

	private static final String[] EMPTY_STRING_ARRAY = new String[0];
	private static final IConfigurationElement[] NONE_CHILDREN = new IConfigurationElement[0];
	private int seqNumber;
	private String value;
	private HashMap children = new HashMap();
	private Map<String, String> attributes = new HashMap<String, String>();
	private XMLConfigurationElement parent;
	private String name;
	private static Log tr = LogFactory.getLog(XMLConfigurationElement.class);

	/**
	 * @since Auguest 4th, 2002
	 * @roseuid 3D777F5E012D
	 */
	public XMLConfigurationElement() {
	}

	/**
	 * @return java.lang.String
	 * @since Auguest 4th, 2002
	 * @roseuid 3D777EFE0233
	 */
	public String getConfigInfo() {
		return toXML();
	}

	/**
	 * Returns the attrs.
	 * 
	 * @return Attributes
	 */
	public Map<String, String> getAttrs() {
		return attributes;
	}

	/**
	 * Returns the attrs.
	 * 
	 * @return Attributes
	 */
	public String getAttributeValue(String attrName) {
		return (String) attributes.get(attrName);
	}

	/**
	 * Returns the children.
	 * 
	 * @return Vector
	 */
	public IConfigurationElement[] getChildren() {
		List<XMLConfigurationElement> list = getChildrenList();
		if (list.isEmpty()) {
			return NONE_CHILDREN;
		}
		return list.toArray(new XMLConfigurationElement[list.size()]);
	}

	/**
	 * Returns the children.
	 * 
	 * @return Vector
	 */
	public IConfigurationElement[] getChildren(String name) {
		Object obj = children.get(name);
		if (obj == null)
			return null;
		if (obj instanceof IConfigurationElement) {
			return new IConfigurationElement[] { (IConfigurationElement) obj };
		}
		List<XMLConfigurationElement> v = (List<XMLConfigurationElement>) obj;
		IConfigurationElement[] objs = new IConfigurationElement[v.size()];
		return v.toArray(objs);
	}

	public IConfigurationElement getChild(String name) {
		Object obj = children.get(name);
		if (obj == null)
			return null;
		if (obj instanceof IConfigurationElement) {
			return (IConfigurationElement) obj;
		}
		List<XMLConfigurationElement> v = (List<XMLConfigurationElement>) obj;
		if (v.isEmpty()) {
			return null;
		}
		return (IConfigurationElement) v.iterator().next();
	}

	/**
	 * Returns the name.
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the value.
	 * 
	 * @return String
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the attrs.
	 * 
	 * @param attrs
	 *            The attrs to set
	 */
	public void setAttrs(Attributes attrs) {
		initAttributes(attrs);
	}

	/**
	 * Sets the children.
	 * 
	 * @param children
	 *            The children to set
	 */
	public void addChild(IConfigurationElement conf) {
		if (conf != null) {
			Object obj = children.get(conf.getName());
			if (obj == null)
				children.put(conf.getName(), conf);
			else {
				if (obj instanceof List) {
					((List) obj).add(conf);
				} else {
					List<XMLConfigurationElement> v = new LinkedList<XMLConfigurationElement>();
					v.add((XMLConfigurationElement) obj);
					v.add((XMLConfigurationElement) conf);
					children.put(conf.getName(), v);
				}
			}
			((XMLConfigurationElement) conf).setParent(this);
		}
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value
	 *            The value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	public String toXML() {
		StringBuffer buf = new StringBuffer();
		buf.append("<").append(name);
		Collection col = attributes.keySet();
		Iterator itr = (col != null) ? col.iterator() : null;
		while ((itr != null) && itr.hasNext()) {
			String name = (String) itr.next();
			String value = (String) attributes.get(name);
			buf.append(elementHeadToXml(name, value));
		}
		int len = 0;
		if (((len = children.size()) == 0) && (value == null))
			buf.append("/>");
		else {
			buf.append(">");
			col = children.values();
			itr = (col != null) ? col.iterator() : null;
			while ((itr != null) && (itr.hasNext())) {
				Object obj = itr.next();
				if (obj instanceof List) {
					List<XMLConfigurationElement> v = (List) obj;
					for (Iterator<XMLConfigurationElement> vitr = v.iterator(); vitr
							.hasNext();) {
						try {
							buf.append('\n')
									.append(vitr.next().getConfigInfo());
						} catch (NoSuchElementException e) {
							break;
						}
					}
				} else {
					buf.append('\n').append(
							((XMLConfigurationElement) obj).getConfigInfo());
				}
			}
			if (value != null)
				buf.append('\n').append('"').append(value).append('"');
			buf.append("\n</").append(name).append('>');
		}
		return buf.toString();
	}

	private String elementHeadToXml(String name, String value) {
		StringBuffer buf = new StringBuffer();
		if (this instanceof PluginConfigurationElement && name.equals("main")
				&& value.endsWith(".swf")) {
			buf.append(' ')
					.append(name)
					.append('=')
					.append('"')
					.append("plugins/swf/")
					.append(((PluginConfigurationElement) this)
							.getNamespaceIndentifier())
					.append('/')
					.append(((PluginConfigurationElement) this)
							.getPluginVersion().toString()).append('/')
					.append(value).append('"');
		} else if (this.name.equals("library") && name.equals("name")
				&& value.endsWith(".swf")) {
			if (attributes.get("thirdParty") != null) {// 如果是第三方的swf 就把它放到root下边
				String thirdParty = (String) attributes.get("thirdParty");
				buf.append(' ').append(name).append('=')
						.append("\"plugins/thirdparty/").append(value)
						.append('"');
			} else {
				PluginConfigurationElement pelements = this
						.getPluginConfigurationElement();
				buf.append(' ').append(name).append('=').append('"')
						.append("plugins/swf/")
						.append(pelements.getNamespaceIndentifier())
						.append('/')
						.append(pelements.getPluginVersion().toString())
						.append('/').append(value).append('"');
			}
		} else if (this.name.equals("productTheme") && name.equals("url")
				&& value.endsWith(".swf")) {
			buf.append(' ').append(name).append('=').append('"')
					.append("plugins/style/").append(value).append('"');
		} else {
			buf.append(' ').append(name).append('=').append('"').append(value)
					.append('"');
		}
		return buf.toString();
	}

	/**
	 * add by wangping 2009-01-10 14:07 在xml这个树上,如果任何一个子节点上有"filter"
	 * 属性,并且它的值和此函数的传入参数匹配，则以此结点为根结点的子树和它的父结点一直上溯到根，这些结点都会被记录在返回的String 里。
	 * 但并不能保证父结点的其它子结点也包含到此String中，
	 * 
	 * 0 参数为 null 或 "" 或 "*" 不过滤，返回toXML()。 1 当前结点通过了，把当前结点的子树返回。 2 如果当前结点没有通过，
	 * 2.1 当前结点还有子结点，则先过滤子结点，如果过滤后的String 长度不为"0",则返回: 当前结点name + attribute +
	 * 过滤结果 + value + 当前结点的xml结束符。 2.2 当前结点没有子结点，则返回null 或 "" 。
	 * 
	 * @return
	 */
	public String toXMLWithFilter(String filter) {
		// 0 参数为 "*" 不过滤，返回toXML()。
		if (StringUtils.isBlank(filter) || filter.equals("*")) {
			return this.toXML();
		}
		// 1 当前结点通过了，把当前结点的子树返回。
		if (this.hasAttribute(CoreConstants.ATT_FILTER)) {
			if (this.getAttributeValue(CoreConstants.ATT_FILTER).equals(filter)) {
				return this.toXML();
			}
		}
		// 2 如果当前结点没有通过，
		// 2.1 当前结点还有子结点，则先过滤子结点，如果过滤后的String 长度不为"0",则返回: 当前结点name + attribute
		// + 过滤结果 + value + 当前结点的xml结束符。
		if (children.size() != 0) {
			// 则先过滤子结点，
			StringBuffer buf = new StringBuffer();
			Collection col = children.values();
			Iterator itr = (col != null) ? col.iterator() : null;
			while ((itr != null) && (itr.hasNext())) {
				Object obj = itr.next();
				if (obj instanceof List) {
					List<XMLConfigurationElement> v = (List) obj;
					for (Iterator<XMLConfigurationElement> vitr = v.iterator(); vitr
							.hasNext();) {
						try {
							String tempString = vitr.next().toXMLWithFilter(
									filter);
							if (!StringUtils.isBlank(tempString)) {
								buf.append('\n').append(tempString);
							}
						} catch (NoSuchElementException e) {
							break;
						}
					}
				} else {
					String tempString = ((XMLConfigurationElement) obj)
							.toXMLWithFilter(filter);
					if (!StringUtils.isBlank(tempString)) {
						buf.append('\n').append(tempString);
					}
				}
			}
			// 如果过滤后的String 长度不为"0",则返回: 当前结点name + attribute + 过滤结果 + value +
			// 当前结点的xml结束符。
			if (buf.length() > 0) {
				// 当前结点name + attribute
				StringBuffer buf2 = new StringBuffer();
				buf2.append("<").append(name);
				Collection col2 = attributes.keySet();
				Iterator itr2 = (col2 != null) ? col2.iterator() : null;
				while ((itr2 != null) && itr2.hasNext()) {
					String name = (String) itr2.next();
					String value = (String) attributes.get(name);
					// buf2.append(' ').append(name).append('=').append('"').append(
					// value).append(
					// '"');
					buf2.append(elementHeadToXml(name, value));
				}
				buf2.append(">");
				// + 过滤结果
				buf2.append(buf.toString());
				// + value + 当前结点的xml结束符
				if (value != null) {
					buf2.append('\n').append('"').append(value).append('"');
				}
				buf2.append("\n</").append(name).append('>');
				return buf2.toString();
			}
		}
		// 2.2 当前结点没有子结点，则返回null 或 "" 。
		return null;
	}

	/**
	 * 返回那些黑名单与blacklist不匹配的元素的toXML()
	 * 
	 * 在webplugin.xml里的元素可以增加 blacklist 属性，它的值被认为是此元素的黑名单列表，如果有多个黑名单以","分隔。
	 * 在使用此方法返回element的String形式时，如果参数与元素的黑名单列表匹配成功,在返回值中将不会包含此元素及其子树的String形式。
	 * 
	 * 0 如果为null或"" 则认为匹配不成功，此函数返回值与toXML()同 1 如果当前元素有blacklist属性 1.1
	 * 如果当前元素匹配不成功 如果没有子元素，则返回当前元素toString。 如果有子元素 记算子元素的String形式，
	 * 如果结果blank，则返回当前元素toString。 如果结果不是blank则 返回：当前结点name + attribute + 结果 +
	 * value + 当前结点的xml结束符。 1.2 如果当前元素匹配成功 返回null 2 如果当前元素没有blacklist属性
	 * 如果没有子元素，则返回当前元素toString。 如果有子元素 记算子元素的String形式，
	 * 如果结果blank，则返回当前元素toString。 如果结果不是blank则 返回：当前结点name + attribute + 结果 +
	 * value + 当前结点的xml结束符。
	 * 
	 * @param blacklist
	 *            blacklist 的值可以是以下形式： 如果为"WXXR"
	 *            如果元素的黑名单列表中有黑名单与"WXXR"字符串完全匹配，系统将认为黑名单匹配成功。 如果为"*WXXR*"
	 *            如果元素的黑名单列表中包含"WXXR"字符串，系统将认为黑名单匹配成功。 如果为"ABC*"
	 *            如果元素的黑名单列表中包含以"ABC"开头的字符串，系统将认为黑名单匹配成功。 如果为"*XYZ"
	 *            如果元素的黑名单列表中包含以"XYZ"结尾的字符串，系统将认为黑名单匹配成功。
	 * 
	 *            如果为"*" 则任何有blacklist属性的元素都匹配成功，即使blacklist的值为""
	 *            ，除了没有blacklist属性的元素。 如果为null或"" 则认为匹配不成功，此函数返回值与toXML()同。
	 * 
	 * 
	 * @return
	 */
	public String toXMLWithoutBlackList(String blacklist) {
		// 0 如果为null或"" 则认为匹配不成功，此函数返回值与toXML()同
		if (StringUtils.isBlank(blacklist)) {
			return this.toXML();
		}
		// 1 如果当前元素有blacklist属性
		if (this.hasAttribute(CoreConstants.ATT_BLACKLIST)) {
			String blacklists = this
					.getAttributeValue(CoreConstants.ATT_BLACKLIST);
			boolean b = blackListMatch(blacklist, blacklists);
			// 1.1 如果当前元素匹配不成功
			if (!b) {
				return doChildWithoutBlacklist(blacklist);
			}
			// 1.2 如果当前元素匹配成功
			return null;
		} else {
			// 2 如果当前元素没有blacklist属性则此函数返回值与toXML()同
			return doChildWithoutBlacklist(blacklist);
		}
	}

	private String doChildWithoutBlacklist(String blacklist) {
		// 如果没有子元素，则返回当前元素toString。
		if (children.size() == 0) {
			return this.toXML();
		}
		// 如果有子元素
		// 记算子元素的String形式，
		StringBuffer buf = new StringBuffer();
		Collection col = children.values();
		Iterator itr = (col != null) ? col.iterator() : null;
		while ((itr != null) && (itr.hasNext())) {
			Object obj = itr.next();
			if (obj instanceof List) {
				List<XMLConfigurationElement> v = (List) obj;
				for (Iterator<XMLConfigurationElement> vitr = v.iterator(); vitr
						.hasNext();) {
					try {
						String tempString = vitr.next().toXMLWithoutBlackList(
								blacklist);
						if (!StringUtils.isBlank(tempString)) {
							buf.append('\n').append(tempString);
						}
					} catch (NoSuchElementException e) {
						break;
					}
				}
			} else {
				String tempString = ((XMLConfigurationElement) obj)
						.toXMLWithoutBlackList(blacklist);
				if (!StringUtils.isBlank(tempString)) {
					buf.append('\n').append(tempString);
				}
			}
		}
		// 如果结果不是blank则 当前结点name + attribute + 结果 + value + 当前结点的xml结束符。
		if (buf.length() > 0) {
			// 当前结点name + attribute
			StringBuffer buf2 = new StringBuffer();
			buf2.append("<").append(name);
			Collection col2 = attributes.keySet();
			Iterator itr2 = (col2 != null) ? col2.iterator() : null;
			while ((itr2 != null) && itr2.hasNext()) {
				String name = (String) itr2.next();
				String value = (String) attributes.get(name);
				// buf2.append(' ').append(name).append('=').append('"').append(
				// value).append(
				// '"');

				buf2.append(elementHeadToXml(name, value));
			}
			buf2.append(">");
			// + 过滤结果
			buf2.append(buf.toString());
			// + value + 当前结点的xml结束符
			if (value != null) {
				buf2.append('\n').append('"').append(value).append('"');
			}
			buf2.append("\n</").append(name).append('>');
			return buf2.toString();
		} else {
			// 如果结果blank，则返回当前元素toString。
			return this.toXML();
		}
		// return this.toXML();
	}

	/**
	 * 
	 * @param blacklist
	 *            true 黑名单匹配成功 false 黑名单匹配不成功
	 * @param blacklists
	 * @return
	 */
	private boolean blackListMatch(String blacklist, String blacklists) {
		// 如果为null或"" 则不过滤返回值与toXML()同。 在这里就是返回false， 代表匹配不成功
		if (StringUtils.isBlank(blacklist)) {
			return false;
		}
		// 如果为"*" 则过滤任何有blacklist属性的元素，即使blacklist的值为""。，除了没有blacklist属性的元素。
		// 能进入此方法说明一定有blacklist属性
		if (blacklist.equals("*")) {
			return true;
		}
		if (StringUtils.isBlank(blacklists)) {
			return false;
		}
		blacklists = blacklists.trim();

		// 如果为"*WXXR*"
		// 如果元素的黑名单列表中包含"WXXR"字符串，系统将认为黑名单匹配成功。
		if (blacklist.endsWith("*") && blacklist.startsWith("*")) {
			String tempBL = blacklist.substring(0, blacklist.length() - 1);
			tempBL = tempBL.substring(1, tempBL.length());
			String[] blacklistStrings = blacklists.split(",");
			for (String bl : blacklistStrings) {
				if (bl.indexOf(tempBL) != -1) {
					return true;
				}
			}
		}

		// 如果为"ABC*"
		// 如果元素的黑名单列表中包含以"ABC"开头的字符串，系统将认为黑名单匹配成功。
		if (blacklist.endsWith("*")) {
			String tempBL = blacklist.substring(0, blacklist.length() - 1);
			String[] blacklistStrings = blacklists.split(",");
			for (String bl : blacklistStrings) {
				if (bl.startsWith(tempBL)) {
					return true;
				}
			}
		}
		// 如果为"*XYZ"
		// 如果元素的黑名单列表中包含以"XYZ"结尾的字符串，系统将认为黑名单匹配成功。
		if (blacklist.startsWith("*")) {
			String tempBL = blacklist.substring(1, blacklist.length());
			String[] blacklistStrings = blacklists.split(",");
			for (String bl : blacklistStrings) {
				if (bl.endsWith(tempBL)) {
					return true;
				}
			}
		}
		// 如果为"WXXR"
		// 如果元素的黑名单列表中有黑名单与"WXXR"字符串完全匹配，系统将认为黑名单匹配成功。
		String[] blacklistStrings = blacklists.split(",");
		for (String bl : blacklistStrings) {
			if (blacklist.equals(bl)) {
				return true;
			}
		}
		return false;
	}

	private synchronized void initAttributes(Attributes attrs) {
		attributes = new HashMap<String, String>();
		for (int i = 0; (attrs != null) && (i < attrs.getLength()); i++) {
			String qname = attrs.getQName(i);
			String lname = attrs.getLocalName(i);
			String name = lname;
			if ((lname == null) || (lname.trim().length() == 0))
				name = qname;

			String value = attrs.getValue(i);
			attributes.put(name, value);
		}
	}

	public List<XMLConfigurationElement> getChildrenList() {
		ArrayList<XMLConfigurationElement> list = new ArrayList<XMLConfigurationElement>();
		Collection col = children.values();
		Iterator itr = (col != null) ? col.iterator() : null;
		while (itr != null && itr.hasNext()) {
			Object obj = itr.next();
			if (obj instanceof List)
				list.addAll((List<XMLConfigurationElement>) obj);
			else
				list.add((XMLConfigurationElement) obj);
		}
		Collections.sort(list);
		return list;
	}

	/**
	 * Returns the seqNumber.
	 * 
	 * @return int
	 */
	public int getSeqNumber() {
		return seqNumber;
	}

	/**
	 * Sets the seqNumber.
	 * 
	 * @param seqNumber
	 *            The seqNumber to set
	 */
	public void setSeqNumber(int seqNumber) {
		this.seqNumber = seqNumber;
	}

	protected String getChildContent(String childName) {
		XMLConfigurationElement child = (XMLConfigurationElement) getChild(childName);
		return (child != null) ? child.getValue() : null;
	}

	/**
	 * @return the parent
	 */
	public XMLConfigurationElement getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(XMLConfigurationElement parent) {
		this.parent = parent;
	}

	/**
	 * @return the contributor
	 */
	public IContributor getContributor() {
		return getPluginConfigurationElement().getContributor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wxxr.web.platform.core.IConfigurationElement#createExecutableExtension
	 * (java.lang.String)
	 */
	public Object createExecutableExtension(String propertyName)
			throws CoreException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wxxr.web.platform.core.IConfigurationElement#getAttribute(java.lang
	 * .String)
	 */
	public String getAttribute(String name)
			throws InvalidRegistryObjectException {
		return getAttributeValue(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wxxr.web.platform.core.IConfigurationElement#getAttributeNames()
	 */
	public String[] getAttributeNames() {
		if (attributes.isEmpty()) {
			return EMPTY_STRING_ARRAY;
		}
		return (String[]) attributes.keySet().toArray(
				new String[attributes.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wxxr.web.platform.core.IConfigurationElement#getDeclaringExtensionId
	 * ()
	 */
	public String getDeclaringExtensionId() {
		ExtensionConfigurationElement extConf = getExtensionConfigurationElement();
		return extConf != null ? extConf.getExtensionId() : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wxxr.web.platform.core.IConfigurationElement#getNamespaceIdentifier()
	 */
	public String getNamespaceIdentifier()
			throws InvalidRegistryObjectException {
		return getPluginConfigurationElement().getNamespaceIndentifier();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wxxr.web.platform.core.IConfigurationElement#isValid()
	 */
	public boolean isValid() {
		return true;
	}

	protected PluginConfigurationElement getPluginConfigurationElement() {
		XMLConfigurationElement elem = this;
		for (; elem != null; elem = elem.getParent()) {
			if (elem instanceof PluginConfigurationElement) {
				return (PluginConfigurationElement) elem;
			}
		}
		return null;
	}

	protected ExtensionConfigurationElement getExtensionConfigurationElement() {
		XMLConfigurationElement elem = this;
		for (; elem != null; elem = elem.getParent()) {
			if (elem instanceof ExtensionConfigurationElement) {
				return (ExtensionConfigurationElement) elem;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(XMLConfigurationElement o) {
		return (o == null) ? 1 : seqNumber - o.seqNumber;
	}

	public boolean hasAttribute(String name) {
		return attributes.containsKey(name);
	}

	public String toString() {
		return toXML();
	}
}
