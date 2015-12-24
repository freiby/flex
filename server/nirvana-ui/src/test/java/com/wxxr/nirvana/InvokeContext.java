package com.wxxr.nirvana;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wxxr.nirvana.exception.NirvanaException;

public class InvokeContext {
	private JspTagMock currentNode;
	private Log log = LogFactory.getLog(InvokeContext.class);

	public JspTagMock getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(JspTagMock currentNode) throws NirvanaException {
		this.currentNode = currentNode;
		log.info(" === before show " + currentNode.component + " ===");
		currentNode.show();
		log.info(" === end show " + currentNode.component + " ===");
	}

}
