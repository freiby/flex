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
package org.apache.tiles;


/**
 * Root class for all Tiles-exceptions.
 *
 * @version $Rev$ $Date$
 */
public class ModelException extends Exception {

    /**
     * Constructor.
     */
    public ModelException() {
        super();
    }

    /**
     * Constructor.
     *
     * @param message The error or warning message.
     */
    public ModelException(String message) {
        super(message);
    }


    /**
     * Create a new <code>TilesException</code> wrapping an existing exception.
     * <p/>
     * <p>The existing exception will be embedded in the new
     * one, and its message will become the default message for
     * the TilesException.</p>
     *
     * @param e The exception to be wrapped.
     */
    public ModelException(Exception e) {
        super(e);
    }


    /**
     * Create a new <code>TilesException</code> from an existing exception.
     * <p/>
     * <p>The existing exception will be embedded in the new
     * one, but the new exception will have its own message.</p>
     *
     * @param message The detail message.
     * @param e       The exception to be wrapped.
     */
    public ModelException(String message, Exception e) {
        super(message, e);
    }
}