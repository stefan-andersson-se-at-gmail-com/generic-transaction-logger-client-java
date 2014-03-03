/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.generic.logger.client.logmessage.writer.impl;

import com.generic.global.transactionlogger.LogMessageService;
import com.generic.global.transactionlogger.Transactions;
import com.generic.logger.client.logmessage.impl.ProxyObjectMapperImpl;
import com.generic.logger.client.logmessage.interfaces.LogMessageContainer;
import com.generic.logger.client.logmessage.util.LoggerPropertyKeys;
import com.generic.logger.client.logmessage.util.LoggerPropertyUtil;
import com.generic.logger.client.logmessage.writer.interfaces.LogMessageWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;

/**
 *
 * @author ds38745
 */
public class LogMessageWriterJavaStandalone implements LogMessageWriter {

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
                        QName QName = new QName("urn:generic.com:Global:TransactionLogger", "LogMessageService");
                        URL wsdlLocation = new URL(LoggerPropertyUtil.getProperty(LoggerPropertyKeys.LOGMESSAGESERVICE_WSDL_LOCATION));

                        //
                        // Send
                        LogMessageService service = new LogMessageService(wsdlLocation, QName);
                        service.getTransactionLoggerInPort().persist(transactions);

                    } catch (MalformedURLException ex) {              
                        
                        StringBuilder builder = new StringBuilder();
                        builder.append("Missing [ ").append(LoggerPropertyKeys.LOGMESSAGESERVICE_WSDL_LOCATION).append(" ] \n");
                        builder.append("No valid URl was found in logger.propperties! \n");
                        Logger.getLogger(LogMessageWriterJavaStandalone.class.getName()).log(Level.SEVERE, null, builder.toString());
                        Logger.getLogger(LogMessageWriterJavaStandalone.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };

            logWriterJavaStandaloneExecutor.execute(task);

        } catch (Exception ex) {
            Logger.getLogger(LogMessageWriterJavaStandalone.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private class LogWriterJavaStandaloneExecutor implements Executor {

        @Override
        public void execute(Runnable runnable) {
            new Thread(runnable).start();
        }
    }
}