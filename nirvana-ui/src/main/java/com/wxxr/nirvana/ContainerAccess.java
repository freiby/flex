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
package com.wxxr.nirvana;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wxxr.nirvana.context.NirvanaServletContext;
import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.workbench.IWorkbench;


/**
 * Provides static access to the workbench container.
 * 
 *
 * @version $Rev$ $Date$
 */
public final class ContainerAccess {

    /**
     * Constructor, private to avoid instantiation.
     */
    private ContainerAccess() {
    }

    /**
     * The logging object.
     */
    private static final Log LOG =
        LogFactory.getLog(ContainerAccess.class);

    /**
     * The name of the attribute to use when getting and setting the container
     * object in a context.
     */
    public static final String CONTAINER_ATTRIBUTE =
        "com.wxxr.nirvana.CONTAINER";
    
    public static final String WORKBENCH_ATTRIBUTE =
            "com.wxxr.nirvana.WORKBENCH";
    
    public static final String WORKBENCH_SESSION_ATTRIBUTE =
            "com.wxxr.nirvana.SESSIONWORKBENCH";

    public static IWorkbench getWorkbench(){
    	return (IWorkbench) NirvanaServletContext.getContext().getServletContext().getAttribute(ContainerAccess.WORKBENCH_ATTRIBUTE);
    }
    
    public static ISessionWorkbench getSessionWorkbench(){
    	return (ISessionWorkbench) NirvanaServletContext.getContext().getSession().getAttribute(ContainerAccess.WORKBENCH_SESSION_ATTRIBUTE);
    }
    
    public static void setSessionWorkbench(ISessionWorkbench value){
    	 NirvanaServletContext.getContext().getSession().setAttribute(ContainerAccess.WORKBENCH_SESSION_ATTRIBUTE,value);
    }
    
    /**
     * 放入session中
     * @param context
     * @param workbench
     * @throws NirvanaException
     */
    public static void setWorkbench(Object context, IWorkbench workbench) throws NirvanaException{
    	if (workbench == null) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Removing nirvana for context: " + context.getClass().getName());
            }
            removeAttribute(context, WORKBENCH_ATTRIBUTE);
        }
        if (workbench != null && LOG.isInfoEnabled()) {
            LOG.info("Publishing nirvana for context: " + context.getClass().getName());
        }
        setAttribute(context, WORKBENCH_ATTRIBUTE, workbench);
    }
    
    /**
     * Finds and returns a Tiles container object, if it was previously initialized.
     *
     * @param context The (application) context object to use.
     * @return The container if it has been configured previously, otherwise
     * <code>null</code>.
     * @see #setContainer(Object, IIWorkbenchContainer)
     */
    public static IWorkbenchContainer getContainer(Object context) {
        return (IWorkbenchContainer) getAttribute(context, CONTAINER_ATTRIBUTE);
    }
    
    /**
     * Configures the container to be used in the application.
     *
     * @param context The (application) context object to use.
     * @param container The container object to set.
     * @throws NirvanaException If something goes wrong during manipulation of the
     * context.
     */
    public static void setContainer(IWorkbenchContainer container)
        throws NirvanaException {
    	 NirvanaServletContext.getContext().getSession().setAttribute(ContainerAccess.CONTAINER_ATTRIBUTE,container);
    }


    /**
     * Returns an attribute from a context.
     *
     * @param context The context object to use.
     * @param attributeName The name of the attribute to search for.
     * @return The object, that is the value of the specified attribute.
     */
    private static Object getAttribute(Object context, String attributeName) {
        try {
            Class<?> contextClass = context.getClass();
            Method attrMethod = contextClass.getMethod("getAttribute", String.class);
            return attrMethod.invoke(context, attributeName);
        } catch (Exception e) {
            LOG.warn("Unable to retrieve container from specified context: '" + context + "'", e);
            return null;
        }
    }

    /**
     * Sets an attribute in a context.
     *
     * @param context The context object to use.
     * @param name The name of the attribute to set.
     * @param value The value of the attribute to set.
     * @throws NirvanaException If something goes wrong during setting the
     * attribute.
     */
    private static void setAttribute(Object context, String name, Object value)
        throws NirvanaException {
        try {
            Class<?> contextClass = context.getClass();
            Method attrMethod = contextClass.getMethod("setAttribute", String.class, Object.class);
            attrMethod.invoke(context, name, value);
        } catch (Exception e) {
            throw new NirvanaException("Unable to set attribute for specified context: '" + context + "'");
        }
    }

    /**
     * Removes an attribute from a context.
     *
     * @param context The context object to use.
     * @param name The name of the attribute to remove.
     * @throws NirvanaException If something goes wrong during removal.
     */
    private static void removeAttribute(Object context, String name)
        throws NirvanaException {
        try {
            Class<?> contextClass = context.getClass();
            Method attrMethod = contextClass.getMethod("removeAttribute", String.class);
            attrMethod.invoke(context, name);
        } catch (Exception e) {
            throw new NirvanaException("Unable to remove attribute for specified context: '" + context + "'");
        }
    }
}
