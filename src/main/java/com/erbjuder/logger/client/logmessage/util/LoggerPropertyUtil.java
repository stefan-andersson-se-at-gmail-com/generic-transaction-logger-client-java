/* 
 * Copyright (C) 2014 erbjuder.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.erbjuder.logger.client.logmessage.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stefan Andersson
 */
public class LoggerPropertyUtil {

    private static Properties props;

    public static final String FILE_NAME = "../logger.properties";
    public static final String PERSISTENCE_CLASSES = "../classes/logger.properties";
    public static final String PERSISTENCE_JAR_PATH_1 = "../META-INF/logger.properties";
    public static final String PERSISTENCE_JAR_PATH_2 = "../META-INF/classes/logger.properties";
    public static final String PERSISTENCE_WAR_PATH_1 = "../WEB-INF/classes/logger.properties";
    public static final String PERSISTENCE_WAR_PATH_2 = "../WEB-INF/classes/META-INF/logger.properties";
    public static final String PERSISTENCE_WAR_PATH_3 = "../WEB-INF/classes/META-INF/classes/logger.properties";

    private LoggerPropertyUtil() {

    }

    public static String getProperty(String key) {
        if (props == null) {
            props = new LoggerPropertyUtil().getPropertiesFromClasspath();
        }

        return props.getProperty(key);
    }

    public static Set<String> getkeys() {
        if (props == null) {
            props = new LoggerPropertyUtil().getPropertiesFromClasspath();
        }
        return (Set) props.keySet();
    }

    private Properties getPropertiesFromClasspath() {

        try {

            Properties properties = new Properties();

            InputStream in = this.findPropertieStream(LoggerPropertyUtil.PERSISTENCE_WAR_PATH_3);
            if (in == null) {
                in = this.findPropertieStream(LoggerPropertyUtil.PERSISTENCE_WAR_PATH_2);
            }
            if (in == null) {
                in = this.findPropertieStream(LoggerPropertyUtil.PERSISTENCE_WAR_PATH_1);
            }
            if (in == null) {
                in = this.findPropertieStream(LoggerPropertyUtil.PERSISTENCE_JAR_PATH_2);
            }
            if (in == null) {
                in = this.findPropertieStream(LoggerPropertyUtil.PERSISTENCE_JAR_PATH_1);
            }
            if (in == null) {
                in = this.findPropertieStream(LoggerPropertyUtil.PERSISTENCE_CLASSES);
            }

            if (in == null) {
                in = this.findPropertieStream(LoggerPropertyUtil.FILE_NAME);
            }

            if (in != null) {
                properties.load(in);
                in.close();
                return properties;
            } else {
                Logger.getLogger(LoggerPropertyUtil.class.getName()).log(Level.SEVERE, "logger.properties NOT in valid path");
                return null;
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(LoggerPropertyUtil.class.getName()).log(Level.SEVERE, ex.getMessage());
            return null;
        } catch (IOException ex) {
            Logger.getLogger(LoggerPropertyUtil.class.getName()).log(Level.SEVERE, ex.getMessage());
            return null;
        }
    }

    private InputStream findPropertieStream(String path) {

        InputStream in = this.getClass().getClassLoader().getResourceAsStream(path);
        if (in != null) {
            return in;
        }

        in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        if (in != null) {
            return in;
        }

        return in;

    }
}
