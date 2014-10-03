/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.generic.logger.client.logmessage.facade.async;

import com.generic.logger.client.logmessage.impl.LogMessageContainerImpl;
import com.generic.logger.client.logmessage.interfaces.LogMessage;
import com.generic.logger.client.logmessage.interfaces.LogMessageContainer;
import com.generic.logger.client.logmessage.util.LoggerPropertyKeys;
import com.generic.logger.client.logmessage.util.LoggerPropertyUtil;
import com.generic.logger.client.logmessage.util.LoggerPropertyValues;
import com.generic.logger.client.logmessage.writer.impl.LogWriterGlassFishAsync;
import com.generic.logger.client.logmessage.writer.impl.LogWriterJavaStandaloneAsync;
import com.generic.logger.client.logmessage.writer.impl.LogWriterWebsphereAsync;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stefan Andersson
 */
public class LogWriterFacade implements Serializable {

    public void write(LogMessageContainer logMessageContainer) {

        try {
            String environment = LoggerPropertyUtil.getProperty(LoggerPropertyKeys.LOGGING_IN_ENVIRONMENT);
            if (LoggerPropertyValues.LOGGING_IN_ENVIRONMENT_STANDALONE.equalsIgnoreCase(environment)) {
                new LogWriterJavaStandaloneAsync().write(logMessageContainer);
            } else if (LoggerPropertyValues.LOGGING_IN_ENVIRONMENT_GLASSFISHV3.equalsIgnoreCase(environment)) {
                new LogWriterGlassFishAsync().write(logMessageContainer);
            } else if (LoggerPropertyValues.LOGGING_IN_ENVIRONMENT_WEBSPHEREV6.equalsIgnoreCase(environment)) {
                new LogWriterWebsphereAsync().write(logMessageContainer);
            } else {
                Logger.getLogger(LogWriterFacade.class.getName()).log(Level.SEVERE, "Invalid logger.propperies! No prop value=[ LOGGING_IN_ENVIRONMENT ]");
            }

        } catch (Exception ex) {
            Logger.getLogger(LogWriterFacade.class.getName()).log(Level.SEVERE, ex.getMessage());

        }

    }

    public void write(LogMessage logMessage) {
        this.write(new LogMessageContainerImpl(logMessage));
    }
}