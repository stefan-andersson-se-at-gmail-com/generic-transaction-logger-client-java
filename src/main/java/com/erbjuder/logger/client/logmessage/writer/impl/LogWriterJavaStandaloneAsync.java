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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;

/**
 *
 * @author Stefan Andersson
 */
public class LogWriterJavaStandaloneAsync implements LogWriter {


    @Override
    public void write(final LogMessageContainer logMessageContainer) {

        try {

            LogWriterJavaStandaloneExecutor logWriterJavaStandaloneExecutor = new LogWriterJavaStandaloneExecutor();
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
                        URL wsdlLocation = new URL(LoggerPropertyUtil.getProperty(LoggerPropertyKeys.LOGMESSAGESERVICE_WSDL_LOCATION_ASYNC));

                        //
                        // Send
                        TransactionLogAsynchronousService service = new TransactionLogAsynchronousService(wsdlLocation, QName);
                        service.getTransactionLogAsynchronousInPort().persist(transactions);

                    } catch (MalformedURLException ex) {
                        String msgText = getMalformedURLExceptionText(LoggerPropertyKeys.LOGMESSAGESERVICE_WSDL_LOCATION_ASYNC).toString();
                        Logger.getLogger(LogWriterJavaStandaloneAsync.class.getName()).log(Level.SEVERE, msgText);
                        Logger.getLogger(LogWriterJavaStandaloneAsync.class.getName()).log(Level.SEVERE, ex.getMessage());
                    } catch (Throwable ex) {
                        Logger.getLogger(LogWriterJavaStandaloneAsync.class.getName()).log(Level.SEVERE, "=========== [ JAVA Client Exception ] =========");
                        Logger.getLogger(LogWriterJavaStandaloneAsync.class.getName()).log(Level.SEVERE, ex.getMessage());
                    }
                }
            };

            logWriterJavaStandaloneExecutor.execute(task);

        } catch (Exception ex) {
            Logger.getLogger(LogWriterJavaStandaloneAsync.class.getName()).log(Level.SEVERE, "=========== [ JAVA Client Exception ] =========");
            Logger.getLogger(LogWriterJavaStandaloneAsync.class.getName()).log(Level.SEVERE, ex.getMessage());
        }

    }

    private class LogWriterJavaStandaloneExecutor implements Executor {

        @Override
        public void execute(Runnable runnable) {
            new Thread(runnable).start();
        }
    }

    private StringBuilder getMalformedURLExceptionText(String wsdl_location) {
        StringBuilder builder = new StringBuilder();
        builder.append("Missing [ ").append(wsdl_location).append(" ] \n");
        builder.append("No valid URl was found in logger.propperties! \n");
        return builder;
    }
}
