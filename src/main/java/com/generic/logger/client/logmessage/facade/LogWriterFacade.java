/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.generic.logger.client.logmessage.facade;

import com.generic.global.transactionlogger.Response;
import com.generic.logger.client.logmessage.impl.LogMessageContainerImpl;
import com.generic.logger.client.logmessage.interfaces.LogMessage;
import com.generic.logger.client.logmessage.interfaces.LogMessageContainer;
import com.generic.logger.client.logmessage.util.LoggerPropertyKeys;
import com.generic.logger.client.logmessage.util.LoggerPropertyUtil;
import com.generic.logger.client.logmessage.util.LoggerPropertyValues;
import com.generic.logger.client.logmessage.writer.impl.WriterGlassFish;
import com.generic.logger.client.logmessage.writer.impl.WriterJavaStandalone;
import com.generic.logger.client.logmessage.writer.impl.WriterWebsphere;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ds38745
 */
public class LogWriterFacade implements Serializable {

    public Response writeSynchronous(LogMessageContainer logMessageContainer) {
        Response response = new Response();
        response.setReturn(false);
        try {
            
            String environment = LoggerPropertyUtil.getProperty(LoggerPropertyKeys.LOGGING_IN_ENVIRONMENT);
            if (LoggerPropertyValues.LOGGING_IN_ENVIRONMENT_STANDALONE.equalsIgnoreCase(environment)) {
                response = new WriterJavaStandalone().writeSynchronous(logMessageContainer);
            } else if (LoggerPropertyValues.LOGGING_IN_ENVIRONMENT_GLASSFISHV3.equalsIgnoreCase(environment)) {
                response = new WriterGlassFish().writeSynchronous(logMessageContainer);
            } else if (LoggerPropertyValues.LOGGING_IN_ENVIRONMENT_WEBSPHEREV6.equalsIgnoreCase(environment)) {
                response = new WriterWebsphere().writeSynchronous(logMessageContainer);
            } else {
                Logger.getLogger(LogWriterFacade.class.getName()).log(Level.SEVERE, "Invalid logger.propperies! No prop value=[ LOGGING_IN_ENVIRONMENT ]");

            }

        } catch (Exception ex) {
            Logger.getLogger(LogWriterFacade.class.getName()).log(Level.SEVERE, ex.getMessage());

        }

        //
        // Result
        return response;

    }

    public void writeSynchronous(LogMessage logMessage) {
        this.writeSynchronous(new LogMessageContainerImpl(logMessage));
    }

    public void writeAsynchronous(LogMessageContainer logMessageContainer) {

        try {
            String environment = LoggerPropertyUtil.getProperty(LoggerPropertyKeys.LOGGING_IN_ENVIRONMENT);
            if (LoggerPropertyValues.LOGGING_IN_ENVIRONMENT_STANDALONE.equalsIgnoreCase(environment)) {
                new WriterJavaStandalone().writeAsynchronous(logMessageContainer);
            } else if (LoggerPropertyValues.LOGGING_IN_ENVIRONMENT_GLASSFISHV3.equalsIgnoreCase(environment)) {
                new WriterGlassFish().writeAsynchronous(logMessageContainer);
            } else if (LoggerPropertyValues.LOGGING_IN_ENVIRONMENT_WEBSPHEREV6.equalsIgnoreCase(environment)) {
                new WriterWebsphere().writeAsynchronous(logMessageContainer);
            } else {
                Logger.getLogger(LogWriterFacade.class.getName()).log(Level.SEVERE, "Invalid logger.propperies! No prop value=[ LOGGING_IN_ENVIRONMENT ]");
            }

        } catch (Exception ex) {
            Logger.getLogger(LogWriterFacade.class.getName()).log(Level.SEVERE, ex.getMessage());

        }

    }

    public void writeAsynchronous(LogMessage logMessage) {
        this.writeAsynchronous(new LogMessageContainerImpl(logMessage));
    }
}
