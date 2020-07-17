/*
 * Copyright 2017-2019 Baidu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.threedr3am.log.agent;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.net.URLDecoder;
import java.util.jar.JarFile;
import lombok.extern.slf4j.Slf4j;

public class JarFileHelper {

    /**
     * 添加jar文件到jdk的跟路径下，优先加载
     *
     * @param inst {@link Instrumentation}
     */
    public static void addJarToBootstrap(Instrumentation inst) throws IOException {
        String localJarPath = getLocalJarPath();
        inst.appendToBootstrapClassLoaderSearch(new JarFile(localJarPath));
    }

    /**
     * 获取当前所在jar包的路径
     *
     * @return jar包路径
     */
    public static String getLocalJarPath() throws UnsupportedEncodingException {
        URL localUrl = Agent.class.getProtectionDomain().getCodeSource().getLocation();
        String path = null;
        try {
            path = URLDecoder.decode(localUrl.getFile().replace("+", "%2B"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("[OpenRASP] Failed to get jarFile path.");
            throw e;
        }
        return path;
    }

    /**
     * 获取当前jar包所在的文件夹路径
     *
     * @return jar包所在文件夹路径
     */
    public static String getLocalJarParentPath() throws UnsupportedEncodingException {
        String jarPath = getLocalJarPath();
        return jarPath.substring(0, jarPath.lastIndexOf("/"));
    }

}
