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

import com.sun.management.jmx.Trace;
import com.wxxr.nirvana.platform.CoreException;
import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.platform.IContributor;
import com.wxxr.nirvana.platform.InvalidRegistryObjectException;


/**
 * @author Neil Lin
 * @version 1.0
 * @since Auguest 4th, 2002
 */
public class XMLConfigurationElement implements IConfigurationElement,Comparable<XMLConfigurationElement> {

	private static final String[] EMPTY_STRING_ARRAY = new String[0];
	private static final IConfigurationElement[] NONE_CHILDREN = new IConfigurationElement[0];
	private int seqNumber;
	private String value;
	private HashMap children = new HashMap();
	private Map<String,String> attributes = new HashMap<String,String>();
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
	 * @return Attributes
	 */
	public Map<String,String> getAttrs() {
		return attributes;
	}

	/**
	 * Returns the attrs.
	 * @return Attributes
	 */
	public String getAttributeValue(String attrName) {
		return (String)attributes.get(attrName);
	}

	/**
	 * Returns the children.
	 * @return Vector
	 */
	public IConfigurationElement[] getChildren() {
		List<XMLConfigurationElement> list = getChildrenList();
		if(list.isEmpty()){
			return NONE_CHILDREN;
		}
		return list.toArray(new XMLConfigurationElement[list.size()]);
	}

	/**
	 * Returns the children.
	 * @return Vector
	 */
	public IConfigurationElement[] getChildren(String name) {
		Object obj = children.get(name);
		if (obj == null)
			return null;
		if (obj instanceof IConfigurationElement) {
			return new IConfigurationElement[] {(IConfigurationElement) obj };
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
		if(v.isEmpty()){
			return null;
		}
		return (IConfigurationElement) v.iterator().next();
	}
	/**
	 * Returns the name.
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the value.
	 * @return String
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the attrs.
	 * @param attrs The attrs to set
	 */
	public void setAttrs(Attributes attrs) {
		initAttributes(attrs);
	}

	/**
	 * Sets the children.
	 * @param children The children to set
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
					v.add((XMLConfigurationElement)obj);
					v.add((XMLConfigurationElement)conf);
					children.put(conf.getName(), v);
				}
			}
			((XMLConfigurationElement)conf).setParent(this);
		}
	}

	/**
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the value.
	 * @param value The value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	public String toXML() {
		StringBuffer buf = new StringBuffer();
		buf.append("<").append(name);
		Collection col = attributes.keySet();
		Iterator itr = (col != null) ? col.iterator() : null;
		while((itr != null)&&itr.hasNext()){
			String name = (String)itr.next();
			String value = (String)attributes.get(name);
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
					for (Iterator<XMLConfigurationElement> vitr = v.iterator();vitr.hasNext();) {
						try {
							buf.append('\n').append(
									vitr.next().getConfigInfo());
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
		if(this instanceof PluginConfigurationElement && name.equals("main") && value.endsWith(".swf")){
			buf.append(' ').append(name).append('=').append('"')
			.append("plugins/swf/").append(((PluginConfigurationElement)this).getNamespaceIndentifier()).append('/').append(((PluginConfigurationElement)this).getPluginVersion().toString()).append('/')
			.append(value).append('"');
		}else if(this.name.equals("library") && name.equals("name") && value.endsWith(".swf")){
			if(attributes.get("thirdParty") != null){//����ǵ�������swf �Ͱ����ŵ�root�±�
				String thirdParty = (String)attributes.get("thirdParty");
				buf.append(' ').append(name).append('=').append("\"plugins/thirdparty/").append(value).append('"');
			}else{
				PluginConfigurationElement pelements = this.getPluginConfigurationElement();
				buf.append(' ').append(name).append('=').append('"')
				.append("plugins/swf/").append(pelements.getNamespaceIndentifier()).append('/').append(pelements.getPluginVersion().toString()).append('/')
				.append(value).append('"');
			}
		}else if(this.name.equals("productTheme") && name.equals("url") && value.endsWith(".swf")){
			buf.append(' ').append(name).append('=').append('"')
			.append("plugins/style/").append(value).append('"');
		}else{
			buf.append(' ').append(name).append('=').append('"').append(
					value).append(
					'"');
		}
		return buf.toString();
	}
	/**
	 * add by wangping 2009-01-10 14:07
	 * ��xml�������,����κ�һ���ӽڵ�����"filter" ����,��������ֵ�ʹ˺����Ĵ������ƥ�䣬���Դ˽��Ϊ���������������ĸ����һֱ���ݵ�������Щ��㶼�ᱻ��¼�ڷ��ص�String � 
	 * �������ܱ�֤�����������ӽ��Ҳ��������String�У�
	 * 
	 * 0 ����Ϊ null �� "" �� "*" �����ˣ�����toXML()��
	 * 1 ��ǰ���ͨ���ˣ��ѵ�ǰ�����������ء�
	 * 2 �����ǰ���û��ͨ����
	 * 		2.1 ��ǰ��㻹���ӽ�㣬���ȹ����ӽ�㣬������˺��String ���Ȳ�Ϊ"0",�򷵻�: ��ǰ���name + attribute + ���˽�� + value + ��ǰ����xml��������
	 * 		2.2 ��ǰ���û���ӽ�㣬�򷵻�null �� "" ��
	 * @return
	 */
	public String toXMLWithFilter(String filter) {
		//0 ����Ϊ "*" �����ˣ�����toXML()��
		if(StringUtils.isBlank(filter)||filter.equals("*")){
			return this.toXML();
		}
		//1 ��ǰ���ͨ���ˣ��ѵ�ǰ�����������ء�
		if(this.hasAttribute(CoreConstants.ATT_FILTER)){
			if(this.getAttributeValue(CoreConstants.ATT_FILTER).equals(filter)){
				return this.toXML();
			}
		}
		//2 �����ǰ���û��ͨ����
		//2.1 ��ǰ��㻹���ӽ�㣬���ȹ����ӽ�㣬������˺��String ���Ȳ�Ϊ"0",�򷵻�: ��ǰ���name + attribute + ���˽�� + value + ��ǰ����xml��������
		if(children.size() != 0){
			//���ȹ����ӽ�㣬
			StringBuffer buf = new StringBuffer();
			Collection col = children.values();
			Iterator itr = (col != null) ? col.iterator() : null;
			while ((itr != null) && (itr.hasNext())) {
				Object obj = itr.next();
				if (obj instanceof List) {
					List<XMLConfigurationElement> v = (List) obj;
					for (Iterator<XMLConfigurationElement> vitr = v.iterator();vitr.hasNext();) {
						try {
							String tempString = vitr.next().toXMLWithFilter(filter);
							if(!StringUtils.isBlank(tempString)){
								buf.append('\n').append(tempString);
							}
						} catch (NoSuchElementException e) {
							break;
						}
					}
				} else {
					String tempString = ((XMLConfigurationElement) obj).toXMLWithFilter(filter);
					if(!StringUtils.isBlank(tempString)){
						buf.append('\n').append(tempString);
					}
				}
			}
			//������˺��String ���Ȳ�Ϊ"0",�򷵻�: ��ǰ���name + attribute + ���˽�� + value + ��ǰ����xml��������
			if(buf.length()>0){
				// ��ǰ���name + attribute
				StringBuffer buf2 = new StringBuffer();
				buf2.append("<").append(name);
				Collection col2 = attributes.keySet();
				Iterator itr2 = (col2 != null) ? col2.iterator() : null;
				while((itr2 != null)&&itr2.hasNext()){
					String name = (String)itr2.next();
					String value = (String)attributes.get(name);
//					buf2.append(' ').append(name).append('=').append('"').append(
//							value).append(
//							'"');
					buf2.append(elementHeadToXml(name, value));
				}
				buf2.append(">");
				//  + ���˽�� 
				buf2.append(buf.toString());
				// + value  + ��ǰ����xml������
				if (value != null){
					buf2.append('\n').append('"').append(value).append('"');
				}
				buf2.append("\n</").append(name).append('>');
				return buf2.toString();
			}
		}
		//2.2 ��ǰ���û���ӽ�㣬�򷵻�null �� "" ��
		return null;
	}
	/**
	 * ������Щ��������blacklist��ƥ���Ԫ�ص�toXML()
	 * 
	 * ��webplugin.xml���Ԫ�ؿ������� blacklist ���ԣ�����ֵ����Ϊ�Ǵ�Ԫ�صĺ������б�����ж����������","�ָ���
	 * ��ʹ�ô˷�������element��String��ʽʱ�����������Ԫ�صĺ������б�ƥ��ɹ�,�ڷ���ֵ�н����������Ԫ�ؼ���������String��ʽ��
	 * 
	 * 0 ���Ϊnull��""  ����Ϊƥ�䲻�ɹ����˺�������ֵ��toXML()ͬ
	 * 1 �����ǰԪ����blacklist����
	 * 		1.1 �����ǰԪ��ƥ�䲻�ɹ�
	 * 			���û����Ԫ�أ��򷵻ص�ǰԪ��toString��
	 * 			�������Ԫ��
	 * 				������Ԫ�ص�String��ʽ��
	 * 					������blank���򷵻ص�ǰԪ��toString��
	 * 					����������blank��  ���أ���ǰ���name + attribute + ��� + value + ��ǰ����xml��������
	 * 		1.2 �����ǰԪ��ƥ��ɹ�
	 * 			����null
	 * 2 �����ǰԪ��û��blacklist����
	 * 			���û����Ԫ�أ��򷵻ص�ǰԪ��toString��
	 * 			�������Ԫ��
	 * 				������Ԫ�ص�String��ʽ��
	 * 					������blank���򷵻ص�ǰԪ��toString��
	 * 					����������blank��  ���أ���ǰ���name + attribute + ��� + value + ��ǰ����xml��������
	 * 
	 * @param blacklist
	 * blacklist ��ֵ������������ʽ��
	 *  ���Ϊ"WXXR"
	 *  		���Ԫ�صĺ������б����к�������"WXXR"�ַ�����ȫƥ�䣬ϵͳ����Ϊ������ƥ��ɹ���
	 *  ���Ϊ"*WXXR*"
	 *  		���Ԫ�صĺ������б��а���"WXXR"�ַ�����ϵͳ����Ϊ������ƥ��ɹ���
	 * 	���Ϊ"ABC*"
	 * 			���Ԫ�صĺ������б��а�����"ABC"��ͷ���ַ�����ϵͳ����Ϊ������ƥ��ɹ���
	 *  ���Ϊ"*XYZ"  
	 *  		���Ԫ�صĺ������б��а�����"XYZ"��β���ַ�����ϵͳ����Ϊ������ƥ��ɹ���
	 *  
	 *  ���Ϊ"*"       ���κ���blacklist���Ե�Ԫ�ض�ƥ��ɹ�����ʹblacklist��ֵΪ"" ������û��blacklist���Ե�Ԫ�ء�
	 *  ���Ϊnull��""  ����Ϊƥ�䲻�ɹ����˺�������ֵ��toXML()ͬ��
	 *  
	 *  
	 * @return
	 */
	public String toXMLWithoutBlackList(String blacklist) {
		//0 ���Ϊnull��""  ����Ϊƥ�䲻�ɹ����˺�������ֵ��toXML()ͬ
		if(StringUtils.isBlank(blacklist)){
			return this.toXML();
		}
		//1 �����ǰԪ����blacklist����
		if(this.hasAttribute(CoreConstants.ATT_BLACKLIST)){
			String blacklists = this.getAttributeValue(CoreConstants.ATT_BLACKLIST);
			boolean b = blackListMatch(blacklist,blacklists);
			//1.1 �����ǰԪ��ƥ�䲻�ɹ�
			if(!b){
				return doChildWithoutBlacklist(blacklist);
			}
			//1.2 �����ǰԪ��ƥ��ɹ�
			return null;
		}else{
			//2 �����ǰԪ��û��blacklist������˺�������ֵ��toXML()ͬ
			return doChildWithoutBlacklist(blacklist);
		}
	}

	private String doChildWithoutBlacklist(String blacklist) {
		//���û����Ԫ�أ��򷵻ص�ǰԪ��toString��
		if(children.size() == 0){
			return this.toXML();
		}
		//�������Ԫ��
		//������Ԫ�ص�String��ʽ��
		StringBuffer buf = new StringBuffer();
		Collection col = children.values();
		Iterator itr = (col != null) ? col.iterator() : null;
		while ((itr != null) && (itr.hasNext())) {
			Object obj = itr.next();
			if (obj instanceof List) {
				List<XMLConfigurationElement> v = (List) obj;
				for (Iterator<XMLConfigurationElement> vitr = v.iterator();vitr.hasNext();) {
					try {
						String tempString = vitr.next().toXMLWithoutBlackList(blacklist);
						if(!StringUtils.isBlank(tempString)){
							buf.append('\n').append(tempString);
						}
					} catch (NoSuchElementException e) {
						break;
					}
				}
			} else {
				String tempString = ((XMLConfigurationElement) obj).toXMLWithoutBlackList(blacklist);
				if(!StringUtils.isBlank(tempString)){
					buf.append('\n').append(tempString);
				}
			}
		}
		//����������blank��  ��ǰ���name + attribute + ��� + value + ��ǰ����xml��������
		if(buf.length()>0){
			// ��ǰ���name + attribute
			StringBuffer buf2 = new StringBuffer();
			buf2.append("<").append(name);
			Collection col2 = attributes.keySet();
			Iterator itr2 = (col2 != null) ? col2.iterator() : null;
			while((itr2 != null)&&itr2.hasNext()){
				String name = (String)itr2.next();
				String value = (String)attributes.get(name);
//				buf2.append(' ').append(name).append('=').append('"').append(
//						value).append(
//						'"');
				
				buf2.append(elementHeadToXml(name, value));
			}
			buf2.append(">");
			//  + ���˽�� 
			buf2.append(buf.toString());
			// + value  + ��ǰ����xml������
			if (value != null){
				buf2.append('\n').append('"').append(value).append('"');
			}
			buf2.append("\n</").append(name).append('>');
			return buf2.toString();
		}else{
			//������blank���򷵻ص�ǰԪ��toString��
			return this.toXML();
		}
		//return this.toXML();
	}
	/**
	 * 
	 * @param blacklist
	 * 	true ������ƥ��ɹ�
	 *  false ������ƥ�䲻�ɹ�
	 * @param blacklists
	 * @return
	 */
	private boolean blackListMatch(String blacklist,String blacklists){
		//���Ϊnull��""  �򲻹��˷���ֵ��toXML()ͬ�� ��������Ƿ���false�� ����ƥ�䲻�ɹ�
		if(StringUtils.isBlank(blacklist)){
			return false;
		}
		//���Ϊ"*"       ������κ���blacklist���Ե�Ԫ�أ���ʹblacklist��ֵΪ""��������û��blacklist���Ե�Ԫ�ء� �ܽ���˷���˵��һ����blacklist����
		if(blacklist.equals("*")){
			return true;
		}
		if(StringUtils.isBlank(blacklists)){
			return false;
		}
		blacklists = blacklists.trim();
		
		//���Ϊ"*WXXR*"
		//  		���Ԫ�صĺ������б��а���"WXXR"�ַ�����ϵͳ����Ϊ������ƥ��ɹ���
		if(blacklist.endsWith("*") && blacklist.startsWith("*")){
			String tempBL = blacklist.substring(0, blacklist.length() -1);
			tempBL = tempBL.substring(1, tempBL.length());
			String[] blacklistStrings = blacklists.split(",");
			for (String bl : blacklistStrings) {
				if(bl.indexOf(tempBL) != -1){
					return true;
				}
			}
		}
		
		//���Ϊ"ABC*"
		// 			���Ԫ�صĺ������б��а�����"ABC"��ͷ���ַ�����ϵͳ����Ϊ������ƥ��ɹ���
		if(blacklist.endsWith("*")){
			String tempBL = blacklist.substring(0, blacklist.length() -1);
			String[] blacklistStrings = blacklists.split(",");
			for (String bl : blacklistStrings) {
				if(bl.startsWith(tempBL)){
					return true;
				}
			}
		}
		//���Ϊ"*XYZ"  
		//  		���Ԫ�صĺ������б��а�����"XYZ"��β���ַ�����ϵͳ����Ϊ������ƥ��ɹ���	
		if(blacklist.startsWith("*")){
			String tempBL = blacklist.substring(1, blacklist.length());
			String[] blacklistStrings = blacklists.split(",");
			for (String bl : blacklistStrings) {
				if(bl.endsWith(tempBL)){
					return true;
				}
			}
		}
		//���Ϊ"WXXR"
		//  		���Ԫ�صĺ������б����к�������"WXXR"�ַ�����ȫƥ�䣬ϵͳ����Ϊ������ƥ��ɹ���
		String[] blacklistStrings = blacklists.split(",");
		for (String bl : blacklistStrings) {
			if(blacklist.equals(bl)){
				return true;
			}
		}
		return false;
	}

	private synchronized void initAttributes(Attributes attrs) {
		attributes = new HashMap<String,String>();
		for (int i = 0;(attrs != null) && (i < attrs.getLength()); i++) {
			String qname = attrs.getQName(i);
			String lname = attrs.getLocalName(i);
			String name = lname;
			if((lname == null)||(lname.trim().length() == 0))
				name = qname;

			String value = attrs.getValue(i);
			attributes.put(name,value);
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
				list.add((XMLConfigurationElement)obj);
		}
		Collections.sort(list);
		return list;
	}


	/**
	 * Returns the seqNumber.
	 * @return int
	 */
	public int getSeqNumber() {
		return seqNumber;
	}

	/**
	 * Sets the seqNumber.
	 * @param seqNumber The seqNumber to set
	 */
	public void setSeqNumber(int seqNumber) {
		this.seqNumber = seqNumber;
	}

	protected String getChildContent(String childName) {
		XMLConfigurationElement child = (XMLConfigurationElement)getChild(childName);
		return (child != null) ? child.getValue() : null;
	}

	/**
	 * @return the parent
	 */
	public XMLConfigurationElement getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
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

	/* (non-Javadoc)
	 * @see com.wxxr.web.platform.core.IConfigurationElement#createExecutableExtension(java.lang.String)
	 */
	public Object createExecutableExtension(String propertyName)
	throws CoreException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.web.platform.core.IConfigurationElement#getAttribute(java.lang.String)
	 */
	public String getAttribute(String name) throws InvalidRegistryObjectException {
		return getAttributeValue(name);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.web.platform.core.IConfigurationElement#getAttributeNames()
	 */
	public String[] getAttributeNames() {
		if(attributes.isEmpty()){
			return EMPTY_STRING_ARRAY;
		}
		return (String[])attributes.keySet().toArray(new String[attributes.size()]);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.web.platform.core.IConfigurationElement#getDeclaringExtensionId()
	 */
	public String getDeclaringExtensionId()
	 {
		ExtensionConfigurationElement extConf = getExtensionConfigurationElement();
		return extConf != null ? extConf.getExtensionId() : null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.web.platform.core.IConfigurationElement#getNamespaceIdentifier()
	 */
	public String getNamespaceIdentifier() throws InvalidRegistryObjectException {
		return getPluginConfigurationElement().getNamespaceIndentifier();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.web.platform.core.IConfigurationElement#isValid()
	 */
	public boolean isValid() {
		return true;
	}

	protected PluginConfigurationElement getPluginConfigurationElement(){
		XMLConfigurationElement elem = this;
		for(; elem  != null;  elem = elem.getParent()){
			if(elem instanceof PluginConfigurationElement){
				return (PluginConfigurationElement)elem;
			}
		}
		return null;
	}

	protected ExtensionConfigurationElement getExtensionConfigurationElement(){
		XMLConfigurationElement elem = this;
		for(; elem  != null;  elem = elem.getParent()){
			if(elem instanceof ExtensionConfigurationElement){
				return (ExtensionConfigurationElement)elem;
			}
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(XMLConfigurationElement o) {
		return (o == null) ? 1 : seqNumber-o.seqNumber;
	}

	public boolean hasAttribute(String name) {
		return attributes.containsKey(name);
	}

	public String toString(){
		return toXML();
	}
}
