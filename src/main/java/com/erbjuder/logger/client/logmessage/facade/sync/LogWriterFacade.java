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
package com.erbjuder.logger.client.logmessage.facade.sync;

import com.erbjuder.logger.client.logmessage.impl.LogMessageContainerImpl;
import com.erbjuder.logger.client.logmessage.interfaces.LogMessage;
import com.erbjuder.logger.client.logmessage.interfaces.LogMessageContainer;
import com.erbjuder.logger.client.logmessage.util.LoggerPropertyKeys;
import com.erbjuder.logger.client.logmessage.util.LoggerPropertyUtil;
import com.erbjuder.logger.client.logmessage.util.LoggerPropertyValues;
import com.erbjuder.logger.client.logmessage.writer.impl.LogWriterGlassFishSync;
import com.erbjuder.logger.client.logmessage.writer.impl.LogWriterJavaStandaloneSync;
import com.erbjuder.logger.client.logmessage.writer.impl.LogWriterSAPPISync;
import com.erbjuder.logger.client.logmessage.writer.impl.LogWriterWebsphereSync;
import com.generic.global.transactionlogger.Response;
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

            String environment = new LoggerPropertyUtil().getProperty(LoggerPropertyKeys.LOGGING_IN_ENVIRONMENT);
            if (LoggerPropertyValues.LOGGING_IN_ENVIRONMENT_STANDALONE.equalsIgnoreCase(environment)) {
                response = new LogWriterJavaStandaloneSync().write(logMessageContainer);
            } else if (LoggerPropertyValues.LOGGING_IN_ENVIRONMENT_WEBSPHEREV6.equalsIgnoreCase(environment)) {
                response = new LogWriterWebsphereSync().write(logMessageContainer);
            } else if (LoggerPropertyValues.LOGGING_IN_ENVIRONMENT_GLASSFISHV3.equalsIgnoreCase(environment)
                    || LoggerPropertyValues.LOGGING_IN_ENVIRONMENT_GLASSFISHV4.equalsIgnoreCase(environment)) {
                response = new LogWriterGlassFishSync().write(logMessageContainer);
            } else if (LoggerPropertyValues.LOGGING_IN_ENVIRONMENT_SAP_PI_V73.equalsIgnoreCase(environment)
                    || LoggerPropertyValues.LOGGING_IN_ENVIRONMENT_SAP_PI_V74.equalsIgnoreCase(environment)) {
                response = new LogWriterSAPPISync().write(logMessageContainer);
            } else {
                Logger.getLogger(LogWriterFacade.class.getName()).log(Level.SEVERE, "Invalid logger.properies! No prop value=[ LOGGING_IN_ENVIRONMENT ]");

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
