/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.generic.logger.client.logmessage.writer.impl;

import com.generic.global.transactionlogger.TransactionLogAsynchronousService;
import com.generic.global.transactionlogger.Transactions;
import com.generic.logger.client.logmessage.impl.ProxyObjectMapperImpl;
import com.generic.logger.client.logmessage.interfaces.LogMessageContainer;
import com.generic.logger.client.logmessage.util.LoggerPropertyKeys;
import com.generic.logger.client.logmessage.util.LoggerPropertyUtil;
import com.generic.logger.client.logmessage.writer.interfaces.async.LogWriter;
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
                        Logger.getLogger(LogWriterGlassFishAsync.class.getName()).log(Level.SEVERE, msgText);
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
