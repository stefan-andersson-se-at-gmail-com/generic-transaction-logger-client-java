/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.generic.logger.client.logmessage.writer.impl;

import com.generic.global.transactionlogger.Response;
import com.generic.global.transactionlogger.ServiceFault;
import com.generic.global.transactionlogger.TransactionLogAsynchronousService;
import com.generic.global.transactionlogger.TransactionLogSynchronousService;
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
public class WriterJavaStandalone implements LogMessageWriter {

    @Override
    public Response writeSynchronous(LogMessageContainer logMessageContainer) {
        Response response = new Response();
        response.setReturn(false);

        try {

            // 
            // Convert to proxy object
            Transactions transactions = new ProxyObjectMapperImpl().logToSOAPTransactions(logMessageContainer);

            // 
            // fetch endPoint
            QName QName = new QName("urn:generic.com:Global:TransactionLogger", "TransactionLogSynchronousService");
            URL wsdlLocation = new URL(LoggerPropertyUtil.getProperty(LoggerPropertyKeys.LOGMESSAGESERVICE_WSDL_LOCATION));

            //
            // Send
            TransactionLogSynchronousService service = new TransactionLogSynchronousService(wsdlLocation, QName);
            response = service.getTransactionLogSynchronousInPort().persist(transactions);

        } catch (MalformedURLException ex) {
            Logger.getLogger(WriterJavaStandalone.class.getName()).log(Level.SEVERE, getMalformedURLExceptionText().toString());
            Logger.getLogger(WriterJavaStandalone.class.getName()).log(Level.SEVERE, ex.getMessage());
        } catch (ServiceFault ex) {
            Logger.getLogger(WriterJavaStandalone.class.getName()).log(Level.SEVERE, null, ex);
        }

        //
        // Result
        return response;
    }

    @Override
    public void writeAsynchronous(final LogMessageContainer logMessageContainer) {

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
                        URL wsdlLocation = new URL(LoggerPropertyUtil.getProperty(LoggerPropertyKeys.LOGMESSAGESERVICE_WSDL_LOCATION));

                        //
                        // Send
                        TransactionLogAsynchronousService service = new TransactionLogAsynchronousService(wsdlLocation, QName);
                        service.getTransactionLogAsynchronousInPort().persist(transactions);

                    } catch (MalformedURLException ex) {
                        Logger.getLogger(WriterJavaStandalone.class.getName()).log(Level.SEVERE, getMalformedURLExceptionText().toString());
                        Logger.getLogger(WriterJavaStandalone.class.getName()).log(Level.SEVERE, ex.getMessage());
                    }
                }
            };

            logWriterJavaStandaloneExecutor.execute(task);

        } catch (Exception ex) {
            Logger.getLogger(WriterJavaStandalone.class
                    .getName()).log(Level.SEVERE, ex.getMessage());
        }

    }

    private class LogWriterJavaStandaloneExecutor implements Executor {

        @Override
        public void execute(Runnable runnable) {
            new Thread(runnable).start();
        }
    }

    private StringBuilder getMalformedURLExceptionText() {
        StringBuilder builder = new StringBuilder();
        builder.append("Missing [ ").append(LoggerPropertyKeys.LOGMESSAGESERVICE_WSDL_LOCATION).append(" ] \n");
        builder.append("No valid URl was found in logger.propperties! \n");
        return builder;
    }
}
