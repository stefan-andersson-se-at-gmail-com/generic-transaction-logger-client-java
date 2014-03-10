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
import com.ibm.websphere.asynchbeans.Work;
import com.ibm.websphere.asynchbeans.WorkManager;
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
public class LogWriterWebsphereAsync implements LogWriter {



    @Override
    public void write(final LogMessageContainer logMessageContainer) {

        try {

            WorkManager workManager = (WorkManager) InitialContext.doLookup("wm/ard");
            workManager.doWork(new SoapWork(logMessageContainer));

        } catch (Exception ex) {
            Logger.getLogger(LogWriterWebsphereAsync.class.getName()).log(Level.SEVERE, ex.getMessage());
        }

    }

    // 
    // Private inner static class
    private class SoapWork implements Work {

        private final LogMessageContainer logMessageContainer;

        public SoapWork(final LogMessageContainer logMessageContainer) {
            this.logMessageContainer = logMessageContainer;
        }

        @Override
        public void release() {
            Logger.getLogger(LogWriterWebsphereAsync.class.getName()).log(Level.INFO, "[ SoapWorkLogMessage. release() was called ]");
        }

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
                    wsdlLocation = new URL(LoggerPropertyUtil.getProperty(LoggerPropertyKeys.LOGMESSAGESERVICE_WSDL_LOCATION_ASYNC));
                }

                // 
                // Send
                TransactionLogAsynchronousService service = new TransactionLogAsynchronousService(wsdlLocation, QName);
                service.getTransactionLogAsynchronousInPort().persist(transactions);

            } catch (MalformedURLException ex) {
                String msgText = getMalformedURLExceptionText(LoggerPropertyKeys.LOGMESSAGESERVICE_WSDL_LOCATION_ASYNC).toString();
                Logger.getLogger(LogWriterGlassFishAsync.class.getName()).log(Level.SEVERE, msgText);
                Logger.getLogger(LogWriterWebsphereAsync.class.getName()).log(Level.SEVERE, ex.getMessage());
            }
        }
    }

    private StringBuilder getMalformedURLExceptionText(String wsdl_location) {
        StringBuilder builder = new StringBuilder();
        builder.append("Missing key=[ ").append(wsdl_location).append(" ]. \n");
        builder.append("No jndi loockup for [ ").append(wsdl_location).append(" ] was found. \n");
        builder.append("To set application custom resource, run command ");
        builder.append("[ \n");
        builder.append("< IBM ref page >");
        builder.append(" ] \n");
        builder.append("OR set key and valid URl in logger.propperties file! \n");
        return builder;
    }

}
