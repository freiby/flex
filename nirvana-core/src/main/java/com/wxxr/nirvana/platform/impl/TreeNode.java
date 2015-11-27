package com.wxxr.nirvana.platform.impl;

import java.io.Serializable;
import java.util.Vector;

/**
 * @author nelin
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class TreeNode implements Serializable {

	private TreeNode parent;
	private Vector children = new Vector();
	private String nodeName;

	/**
	 * Constructor for TreeNode.
	 */
	public TreeNode() {
		super();
	}

	/**
	 * Gets the parent.
	 * @return Returns a XmlModel
	 */
	public TreeNode getParentNode() {
		return parent;
	}

	/**
	 * Sets the parent.
	 * @param parent The parent to set
	 */
	public void setParentNode(TreeNode parent) {
		this.parent = parent;
	}

	/**
	 * Gets the child.
	 * @return Returns a XmlModel
	 */
	public Vector getChildNodes() {
		return children;
	}

	/**
	 * Sets the child.
	 * @param child The child to set
	 */
	public void addChildNode(TreeNode child) {
		this.children.add(child);
	}

	public TreeNode pushNode(TreeNode state) {
		addChildNode(state);
		state.setParentNode(this);
		return state;
	}

	public TreeNode popNode() {
		return getParentNode();
	}

	public String getFullPath() {
		Vector v = new Vector();
		TreeNode node = null;
		for (node = this; node != null; node = node.parent) {
			v.addElement(node);
		}
		StringBuffer buf = new StringBuffer();
		for (int i = v.size() - 1; i >= 0; i--) {
			node = (TreeNode) v.elementAt(i);
			if (node.getNodeName() != null)
				buf.append(node.getNodeName()).append('.');
		}
		return buf.toString();
	}

	/**
	 * Returns the nodeName.
	 * @return String
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * Sets the nodeName.
	 * @param nodeName The nodeName to set
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

}