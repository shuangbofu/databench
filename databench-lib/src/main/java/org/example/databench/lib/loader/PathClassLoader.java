/*
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
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.example.databench.lib.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;

public class PathClassLoader extends URLClassLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PathClassLoader.class);
    protected ClassLoader parent;

    private boolean hasExternalRepositories = false;

    public PathClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
        this.parent = parent;
    }

    public PathClassLoader(URL[] urls) {
        super(urls);
    }

    public PathClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (getURLs() == null || getURLs().length == 0) {
            return parent.loadClass(name);
        }
        return loadClass(name, false);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("loadClass(" + name + ", " + resolve + ")");
            }
            Class<?> clazz = findLoadedClass(name);
            if (clazz != null) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Returning class from cache");
                }
                if (resolve) {
                    resolveClass(clazz);
                }
                return (clazz);
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Searching local repositories");
            }
            try {
                clazz = findClass(name);
                if (clazz != null) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Loading class from local repository");
                    }
                    if (resolve) {
                        resolveClass(clazz);
                    }
                    return (clazz);
                }
            } catch (ClassNotFoundException e) {
                // Ignore
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Delegating to parent classloader at end: " + parent);
            }

            try {
                clazz = Class.forName(name, false, parent);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Loading class from parent");
                }
                if (resolve) {
                    resolveClass(clazz);
                }
                return (clazz);
            } catch (ClassNotFoundException e) {
                // Ignore
            }
        }

        throw new ClassNotFoundException(name);
    }


    @Override
    public URL getResource(String name) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getResource(" + name + ")");
        }

        URL url = findResource(name);
        if (url != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("--> Returning '" + url + "'");
            }
            return (url);
        }

        // (3) Delegate to parent unconditionally if not already attempted
        url = parent.getResource(name);
        if (url != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("--> Returning '" + url + "'");
            }
            return (url);
        }

        // (4) Resource was not found
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("--> Resource not found, returning null");
        }
        return (null);
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
        hasExternalRepositories = true;
    }

    /**
     * FIXME 需要测试
     *
     * @param name
     * @return
     * @throws IOException
     */
    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        Enumeration<URL>[] tmp = (Enumeration<URL>[]) new Enumeration<?>[1];
        tmp[0] = findResources(name);//优先使用当前类的资源

        if (!tmp[0].hasMoreElements()) {//只有子classLoader找不到任何资源才会调用原生的方法
            return super.getResources(name);
        }
        return new CompoundEnumeration<>(tmp);
    }

    @Override
    public Enumeration<URL> findResources(String name) throws IOException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("findResources(" + name + ")");
        }

        LinkedHashSet<URL> result = new LinkedHashSet<>();

        Enumeration<URL> superResource = super.findResources(name);

        while (superResource.hasMoreElements()) {
            result.add(superResource.nextElement());
        }

        // Adding the results of a call to the superclass
        if (hasExternalRepositories) {
            Enumeration<URL> otherResourcePaths = super.findResources(name);
            while (otherResourcePaths.hasMoreElements()) {
                result.add(otherResourcePaths.nextElement());
            }
        }

        return Collections.enumeration(result);
    }
}
