/*
 * $Id$
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.tiles.jsp.taglib;

import java.util.List;

import javax.servlet.jsp.JspException;

import org.apache.tiles.ModelException;
import org.apache.tiles.Page;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.impl.ext.BasicSiteContainer;

/**
 * Exposes am attribute as a scripting variable within the page.
 *
 * @since Tiles 1.0
 * @version $Rev$ $Date$
 */
public class ImportResourcesTag extends UseAttributeTag {
	
	private Page currentPage;
	private String type;
	private String position;
	private List<String> res;
	
	/** {@inheritDoc} */
    public int doStartTag() throws JspException {
        container = TilesAccess.getContainer(pageContext.getServletContext());
        attributeContext = container.getAttributeContext(pageContext);
        scope = getScopeId();
        execute();
        return SKIP_BODY;
    }
	
	public void execute() throws JspException {
		container = TilesAccess.getContainer(pageContext.getServletContext());
		attributeContext = container.getAttributeContext(pageContext);
		scope = getScopeId();
		if(container instanceof BasicSiteContainer){
			BasicSiteContainer bsc = (BasicSiteContainer)container;
			currentPage = bsc.getPageNavigation().getCurrentPage();
			try {
				currentPage.getView().getResource().getAttributes();
				if(position == null) position = "footer";
				res = currentPage.getView().getResourcesByTypeAndPostion(type,position);
			} catch (ModelException e) {
				throw new JspException("tag error, tile.xml pug-attribute rtype is null");
			}
		}
		pageContext.setAttribute(getScriptingVariable(), res, scope);
	}
	
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
	
}
