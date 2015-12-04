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

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.jsp.PageContext;

/**
 * Encapsulation of request information.
 *
 * @since 2.0
 * @version $Rev$ $Date$
 */
public interface IRequestContext {

    /**
     * Dispatches the request to a specified path.
     *
     * @param path The path to dispatch to.
     * @throws IOException If something goes wrong during dispatching.
     */
    void dispatch(String path) throws IOException;

    /**
     * Includes the response from the specified URL in the current response output.
     *
     * @param path The path to include.
     * @throws IOException If something goes wrong during inclusion.
     */
    void include(String path) throws IOException;
    
    PrintWriter getWriter() throws IOException;
    
    PageContext getPageContext();
}
