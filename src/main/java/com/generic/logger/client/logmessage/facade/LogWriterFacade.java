/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.generic.logger.client.logmessage.facade;

import com.generic.logger.client.logmessage.impl.LogMessageContainerImpl;
import com.generic.logger.client.logmessage.interfaces.LogMessage;
import com.generic.logger.client.logmessage.interfaces.LogMessageContainer;
import com.generic.logger.client.logmessage.util.LoggerPropertyKeys;
import com.generic.logger.client.logmessage.util.LoggerPropertyUtil;
import com.generic.logger.client.logmessage.util.LoggerPropertyValues;
import com.generic.logger.client.logmessage.writer.impl.LogMessageWriterGlassFish;
import com.generic.logger.client.logmessage.writer.impl.LogMessageWriterJavaStandalone;
import com.generic.logger.client.logmessage.writer.impl.LogMessageWriterWebsphere;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ds38745
 */
public class LogWriterFacade implements Serializable {

    public void write(LogMessageContainer logMessageContainer) {

        try {
            String environment = LoggerPropertyUtil.getProperty(LoggerPropertyKeys.LOGGING_IN_ENVIRONMENT);
            if (LoggerPropertyValues.LOGGING_IN_ENVIRONMENT_STANDALONE.equalsIgnoreCase(environment)) {
                new LogMessageWriterJavaStandalone().write(logMessageContainer);
            } else if (LoggerPropertyValues.LOGGING_IN_ENVIRONMENT_GLASSFISHV3.equalsIgnoreCase(environment)) {
                new LogMessageWriterGlassFish().write(logMessageContainer);
            } else if (LoggerPropertyValues.LOGGING_IN_ENVIRONMENT_WEBSPHEREV6.equalsIgnoreCase(environment)) {
                new LogMessageWriterWebsphere().write(logMessageContainer);
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
