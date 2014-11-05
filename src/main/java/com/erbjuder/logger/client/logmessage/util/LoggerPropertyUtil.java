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

/**
 *
 * @author Stefan Andersson
 */
public class LoggerPropertyUtil {

    private static Properties props;

    static {

        props = new Properties();

        try {

            //load properties file
            LoggerPropertyUtil util = new LoggerPropertyUtil();
            props = util.getPropertiesFromClasspath("loggger.properties");

        } catch (FileNotFoundException ex) {
            System.err.println("[ Exception ] " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("[ Exception ] " + ex.getMessage());
        }

    }

    private LoggerPropertyUtil() {
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }

    public static Set<String> getkeys() {
        return (Set) props.keySet();
    }

    private Properties getPropertiesFromClasspath(String propFileName)
            throws IOException {
        Properties props = new Properties();
        InputStream inputStream =
                this.getClass().getClassLoader().getResourceAsStream(propFileName);

        if (inputStream == null) {
            throw new FileNotFoundException("property file '" + propFileName
                    + "' not found in the classpath");
        }

        props.load(inputStream);
        return props;
    }
}
