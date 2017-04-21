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
package com.erbjuder.logger.client.logmessage.writer.impl;

import com.erbjuder.logger.client.logmessage.impl.ProxyObjectMapperImpl;
import com.erbjuder.logger.client.logmessage.interfaces.LogMessageContainer;
import com.erbjuder.logger.client.logmessage.util.LoggerPropertyKeys;
import com.erbjuder.logger.client.logmessage.util.LoggerPropertyUtil;
import com.erbjuder.logger.client.logmessage.writer.interfaces.async.LogWriter;
import com.generic.global.transactionlogger.TransactionLogAsynchronousService;
import com.generic.global.transactionlogger.Transactions;
import com.sap.aii.af.service.resource.SAPAdapterResources;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.namespace.QName;

/**
 *
 * @author Stefan Andersson
 */
public class LogWriterSAPPIAsync implements LogWriter {

    /**
     *
     */
    private static final long serialVersionUID = 27805462270912537L;

    @Override
    public void write(final LogMessageContainer logMessageContainer) {

        try {

            SAPAdapterResources threadPoolExecutor = (SAPAdapterResources) new InitialContext().lookup("SAPAdapterResources");
            Runnable task = new Runnable() {

                @Override
                public void run() {

                    try {

                        // 
                        // Convert to proxy object
                        Transactions transactions = new ProxyObjectMapperImpl().logToSOAPTransactions(logMessageContainer);

                        // 
                        // fetch endPoint
                        QName QName = new QName("urn:generic.com:Global:TransactionLogger", "TransactionLogAsynchronousService");

                        //
                        // fetch Appserver environment variable iff not exist! use logger.properties value
                        URL wsdlLocation = null;
                        try {
                            wsdlLocation = new URL(InitialContext.<String>doLookup(LoggerPropertyKeys.LOGMESSAGESERVICE_WSDL_LOCATION_ASYNC));

                        } catch (NamingException e) {
                            wsdlLocation = new URL(new LoggerPropertyUtil().getProperty(LoggerPropertyKeys.LOGMESSAGESERVICE_WSDL_LOCATION_ASYNC));
                        }

                        //
                        // Send
                        TransactionLogAsynchronousService service = new TransactionLogAsynchronousService(wsdlLocation, QName);
                        service.getTransactionLogAsynchronousInPort().persist(transactions);

                    } catch (MalformedURLException ex) {

                        String msgText = getMalformedURLExceptionText(LoggerPropertyKeys.LOGMESSAGESERVICE_WSDL_LOCATION_ASYNC).toString();
                        Logger.getLogger(LogWriterSAPPIAsync.class.getName()).log(Level.SEVERE, msgText);
                        Logger.getLogger(LogWriterSAPPIAsync.class.getName()).log(Level.SEVERE, ex.getMessage());
                    }

                }
            };

            threadPoolExecutor.startRunnable(task);

        } catch (NamingException ex) {
            Logger.getLogger(LogWriterSAPPIAsync.class.getName()).log(Level.SEVERE, ex.getMessage());
        }

    }

    private StringBuilder getMalformedURLExceptionText(String wsdl_location) {

        StringBuilder builder = new StringBuilder();
        builder.append("Missing key=[ ").append(wsdl_location).append(" ]. \n");
        builder.append("No jndi loockup for [ ").append(wsdl_location).append(" ] was found. \n");
        builder.append("To set application custom resource, run command ");
        builder.append("[ \n");
        builder.append("< SAP ref page >");
        builder.append(" ] \n");
        builder.append("OR set key and valid URl in logger.propperties file! \n");
        return builder;
    }

}
