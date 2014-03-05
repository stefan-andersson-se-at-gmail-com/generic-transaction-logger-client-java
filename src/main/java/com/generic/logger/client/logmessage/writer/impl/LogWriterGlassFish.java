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
import com.generic.logger.client.logmessage.writer.interfaces.LogWriter;
import com.generic.thread.pool.executor.ThreadPoolExecutor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.namespace.QName;

/**
 *
 * @author ds38745
 */
public class LogWriterGlassFish implements LogWriter {

    @Override
    public Response writeSynchronous(final LogMessageContainer logMessageContainer) {

        Response response = new Response();
        response.setReturn(false);

        try {

            // 
            // Convert to proxy object
            Transactions transactions = new ProxyObjectMapperImpl().logToSOAPTransactions(logMessageContainer);

            // 
            // fetch endPoint
            QName QName = new QName("urn:generic.com:Global:TransactionLogger", "TransactionLogSynchronousService");

            //
            // fetch Appserver environment variable iff not exist! use logger.properties value
            URL wsdlLocation = null;
            try {
                wsdlLocation = new URL(InitialContext.<String>doLookup(LoggerPropertyKeys.LOGMESSAGESERVICE_WSDL_LOCATION));

            } catch (NamingException e) {
                wsdlLocation = new URL(LoggerPropertyUtil.getProperty(LoggerPropertyKeys.LOGMESSAGESERVICE_WSDL_LOCATION));
            }

            //
            // Send
            TransactionLogSynchronousService service = new TransactionLogSynchronousService(wsdlLocation, QName);
            response = service.getTransactionLogSynchronousInPort().persist(transactions);

        } catch (MalformedURLException ex) {
            Logger.getLogger(LogWriterGlassFish.class.getName()).log(Level.SEVERE, getMalformedURLExceptionText().toString());
            Logger.getLogger(LogWriterGlassFish.class.getName()).log(Level.SEVERE, ex.getMessage());
        } catch (ServiceFault ex) {
            Logger.getLogger(LogWriterGlassFish.class.getName()).log(Level.SEVERE, ex.getMessage());
        }

        //
        // Return 
        return response;
    }

    @Override
    public void writeAsynchronous(final LogMessageContainer logMessageContainer) {

        try {

            ThreadPoolExecutor threadPoolExecutor = InitialContext.<ThreadPoolExecutor>doLookup("concurrency/TP");
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
                            wsdlLocation = new URL(InitialContext.<String>doLookup(LoggerPropertyKeys.LOGMESSAGESERVICE_WSDL_LOCATION));

                        } catch (NamingException e) {
                            wsdlLocation = new URL(LoggerPropertyUtil.getProperty(LoggerPropertyKeys.LOGMESSAGESERVICE_WSDL_LOCATION));
                        }

                        //
                        // Send
                        TransactionLogAsynchronousService service = new TransactionLogAsynchronousService(wsdlLocation, QName);
                        service.getTransactionLogAsynchronousInPort().persist(transactions);

                    } catch (MalformedURLException ex) {
                        Logger.getLogger(LogWriterGlassFish.class.getName()).log(Level.SEVERE, getMalformedURLExceptionText().toString());
                        Logger.getLogger(LogWriterGlassFish.class.getName()).log(Level.SEVERE, ex.getMessage());
                    }

                }
            };

            threadPoolExecutor.execute(task);

        } catch (NamingException ex) {
            Logger.getLogger(LogWriterGlassFish.class.getName()).log(Level.SEVERE, ex.getMessage());
        }

    }

    private StringBuilder getMalformedURLExceptionText() {
        StringBuilder builder = new StringBuilder();
        builder.append("Missing key=[ ").append(LoggerPropertyKeys.LOGMESSAGESERVICE_WSDL_LOCATION).append(" ]. \n");
        builder.append("No jndi loockup for [ ").append(LoggerPropertyKeys.LOGMESSAGESERVICE_WSDL_LOCATION).append(" ] was found. \n");
        builder.append("To set application custom resource, run command ");
        builder.append("[ \n");
        builder.append("./asadmin create-custom-resource --restype=java.lang.String --factoryclass=org.glassfish.resources.custom.factory.PrimitivesAndStringFactory --property value=\"http\\://<the value>\" ").append(LoggerPropertyKeys.LOGMESSAGESERVICE_WSDL_LOCATION);
        builder.append(" ] \n");
        builder.append("OR set key and valid URl in logger.propperties file! \n");
        return builder;
    }

}
