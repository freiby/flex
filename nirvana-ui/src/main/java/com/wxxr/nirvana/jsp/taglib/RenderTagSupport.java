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
package com.wxxr.nirvana.jsp.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import com.wxxr.nirvana.exception.NirvanaException;

/**
 * <p>
 * Support for all tags which render (a template, or definition).
 * </p>
 * <p>
 * Properly invokes the defined preparer and invokes the abstract render method
 * upon completion.
 * </p>
 * This tag takes special care to ensure that the attribute context is reset to
 * it's original state after the execution of the tag is complete. This ensures
 * that all all included attributes in subsequent tiles are scoped properly and
 * do not bleed outside their intended scope.
 *
 * @since Tiles 2.0
 * @version $Rev$ $Date$
 */
public abstract class RenderTagSupport extends ContainerTagSupport{

    /**
     * The view preparer to use before the rendering.
     */
    protected String preparer;

    /**
     * This flag, if <code>true</code>, flushes the content before rendering.
     */
    protected boolean flush;

    /**
     * This flag, if <code>true</code>, ignores exception thrown by preparers
     * and those caused by problems with definitions.
     */
    protected boolean ignore;

    /**
     * Returns the preparer name.
     *
     * @return The preparer name.
     */
    public String getPreparer() {
        return preparer;
    }

    /**
     * Sets the preparer name.
     *
     * @param preparer The preparer name.
     */
    public void setPreparer(String preparer) {
        this.preparer = preparer;
    }

    /**
     * Returns the flush flag. If <code>true</code>, current page out stream
     * is flushed before insertion.
     *
     * @return The flush flag.
     */
    public boolean isFlush() {
        return flush;
    }

    /**
     * Sets the flush flag. If <code>true</code>, current page out stream
     * is flushed before insertion.
     *
     * @param flush The flush flag.
     */
    public void setFlush(boolean flush) {
        this.flush = flush;
    }

    /**
     * Returns the ignore flag. If it is set to true, and the attribute
     * specified by the name does not exist, simply return without writing
     * anything. The default value is false, which will cause a runtime
     * exception to be thrown.
     *
     * @return The ignore flag.
     */
    public boolean isIgnore() {
        return ignore;
    }

    /**
     * Sets the ignore flag. If this attribute is set to true, and the attribute
     * specified by the name does not exist, simply return without writing
     * anything. The default value is false, which will cause a runtime
     * exception to be thrown.
     *
     * @param ignore The ignore flag.
     */
    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }


    /** {@inheritDoc} */
    public void release() {
        preparer = null;
        flush = false;
        ignore = false;
        super.release();
    }

    /** {@inheritDoc} */
    public int doStartTag() throws JspException {
        super.doStartTag();
        return isAccessAllowed() ? EVAL_BODY_BUFFERED : SKIP_BODY;
    }

    protected boolean isAccessAllowed() {
		return true;
	}

	/**
     * Execute the tag by invoking the preparer, if defined, and then
     * rendering.
     *
     * @throws NirvanaException if a prepare or render exception occurs.
     * @throws JspException if a jsp exception occurs.
     * @throws IOException if an io exception occurs.
     */
    protected void execute() throws NirvanaException, JspException, IOException {
        if (preparer != null) {
            container.prepare(preparer, pageContext);
        }
        render();
        if (flush) {
            pageContext.getOut().flush();
        }
    }

    /**
     * Render the specified content.
     *
     * @throws NirvanaException if a prepare or render exception occurs.
     * @throws JspException if a jsp exception occurs.
     * @throws IOException if an io exception occurs.
     */
    protected abstract void render() throws JspException, NirvanaException, IOException;

}
