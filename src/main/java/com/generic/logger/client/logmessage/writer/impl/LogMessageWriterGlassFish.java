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
public class LogMessageWriterGlassFish implements LogMessageWriter {

    @Override
    public void write(final LogMessageContainer logMessageContainer) {

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
                        QName QName = new QName("urn:generic.com:Global:TransactionLogger", "LogMessageService");

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
                        LogMessageService service = new LogMessageService(wsdlLocation, QName);
                        service.getTransactionLoggerInPort().persist(transactions);

                    } catch (MalformedURLException ex) {

                        StringBuilder builder = new StringBuilder();
                        builder.append("Missing key=[ ").append(LoggerPropertyKeys.LOGMESSAGESERVICE_WSDL_LOCATION).append(" ]. \n");
                        builder.append("No jndi loockup for [ ").append(LoggerPropertyKeys.LOGMESSAGESERVICE_WSDL_LOCATION).append(" ] was found. \n");
                        builder.append("To set application custom resource, run command ");
                        builder.append("[ \n");
                        builder.append("./asadmin create-custom-resource --restype=java.lang.String --factoryclass=org.glassfish.resources.custom.factory.PrimitivesAndStringFactory --property value=\"http\\://<the value>\" ").append(LoggerPropertyKeys.LOGMESSAGESERVICE_WSDL_LOCATION);
                        builder.append(" ] \n");
                        builder.append("OR set key and valid URl in logger.propperties file! \n");

                        Logger.getLogger(LogMessageWriterGlassFish.class.getName()).log(Level.SEVERE, builder.toString());
                        Logger.getLogger(LogMessageWriterGlassFish.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            };

            threadPoolExecutor.execute(task);

        } catch (NamingException ex) {
            Logger.getLogger(LogMessageWriterGlassFish.class.getName()).log(Level.SEVERE, ex.getMessage());
        }

    }
}
