/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.generic.logger.client.logmessage.facade.sync;

import com.generic.global.transactionlogger.Response;
import com.generic.logger.client.logmessage.impl.LogMessageContainerImpl;
import com.generic.logger.client.logmessage.interfaces.LogMessage;
import com.generic.logger.client.logmessage.interfaces.LogMessageContainer;
import com.generic.logger.client.logmessage.util.LoggerPropertyKeys;
import com.generic.logger.client.logmessage.util.LoggerPropertyUtil;
import com.generic.logger.client.logmessage.util.LoggerPropertyValues;
import com.generic.logger.client.logmessage.writer.impl.LogWriterGlassFishSync;
import com.generic.logger.client.logmessage.writer.impl.LogWriterJavaStandaloneSync;
import com.generic.logger.client.logmessage.writer.impl.LogWriterWebsphereSync;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stefan Andersson
 */
public class LogWriterFacade implements Serializable {

    public Response write(LogMessageContainer logMessageContainer) {
        Response response = new Response();
        response.setReturn(false);
        try {

            String environment = LoggerPropertyUtil.getProperty(LoggerPropertyKeys.LOGGING_IN_ENVIRONMENT);
            if (LoggerPropertyValues.LOGGING_IN_ENVIRONMENT_STANDALONE.equalsIgnoreCase(environment)) {
                response = new LogWriterJavaStandaloneSync().write(logMessageContainer);
            } else if (LoggerPropertyValues.LOGGING_IN_ENVIRONMENT_GLASSFISHV3.equalsIgnoreCase(environment)) {
                response = new LogWriterGlassFishSync().write(logMessageContainer);
            } else if (LoggerPropertyValues.LOGGING_IN_ENVIRONMENT_WEBSPHEREV6.equalsIgnoreCase(environment)) {
                response = new LogWriterWebsphereSync().write(logMessageContainer);
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

    public void write(LogMessage logMessage) {
        this.write(new LogMessageContainerImpl(logMessage));
    }

}
