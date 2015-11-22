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

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.apache.tiles.access.TilesAccess;

/**
 * Exposes am attribute as a scripting variable within the page.
 *
 * @since Tiles 1.0
 * @version $Rev$ $Date$
 */
public class UseAttributeExtTag extends UseAttributeTag {
	public int doStartTag() throws JspException {
		container = TilesAccess.getContainer(pageContext.getServletContext());
		attributeContext = container.getAttributeContext(pageContext);
		scope = getScopeId();

		// Some tags allow for unspecified attribues. This
		// implies that the tag should use all of the attributes.
		if (name != null) {
			attribute = attributeContext.getAttribute(name);
			if ((attribute == null || attribute.getValue() == null) && ignore) {
				return SKIP_BODY;
			}

			if (attribute == null) {
				throw new JspException("Attribute with name '" + name
						+ "' not found");
			}

			if (attribute.getValue() == null) {
				throw new JspException("Attribute with name '" + name
						+ "' has a null value.");
			}
		}

		try {
			execute();
		} catch (IOException e) {
			throw new JspException("io error while executing tag '"
					+ getClass().getName() + "'.", e);
		}

		return SKIP_BODY;
	}
}
