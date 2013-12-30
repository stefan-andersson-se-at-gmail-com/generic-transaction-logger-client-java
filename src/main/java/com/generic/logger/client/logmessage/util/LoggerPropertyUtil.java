/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.generic.logger.client.logmessage.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author ds38745
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
