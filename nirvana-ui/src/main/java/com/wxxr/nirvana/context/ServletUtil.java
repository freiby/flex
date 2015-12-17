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

package com.wxxr.nirvana.context;

import javax.servlet.http.HttpServletRequest;


/**
 * Utilities for Tiles servlet support.
 *
 * @version $Rev$ $Date$
 * @since 2.0.6
 */
public final class ServletUtil {

    /**
     * Name of the attribute used to store the force-include option.
     * @since 2.0.6
     */
    public static final String FORCE_INCLUDE_ATTRIBUTE_NAME =
        "org.apache.tiles.servlet.context.ServletTilesRequestContext.FORCE_INCLUDE";

    /**
     * Private constructor to avoid instantiation.
     */
    private ServletUtil() {
    }

    /**
     * Returns true if forced include of the result is needed.
     *
     * @param request The HTTP request.
     * @return If <code>true</code> the include operation must be forced.
     * @since 2.0.6
     */
    public static boolean isForceInclude(HttpServletRequest request) {
        Boolean retValue = (Boolean) request
                .getAttribute(ServletUtil.FORCE_INCLUDE_ATTRIBUTE_NAME);
        return retValue != null && retValue.booleanValue();
    }

    /**
     * Sets the option that enables the forced include of the response.
     *
     * @param request The HTTP request.
     * @param forceInclude If <code>true</code> the include operation must be
     * forced.
     * @since 2.0.6
     */
    public static void setForceInclude(HttpServletRequest request,
            boolean forceInclude) {
        Boolean retValue = Boolean.valueOf(forceInclude);
        request.setAttribute(
                ServletUtil.FORCE_INCLUDE_ATTRIBUTE_NAME,
                retValue);
    }
}
