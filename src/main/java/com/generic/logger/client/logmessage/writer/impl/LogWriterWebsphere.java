/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.generic.logger.client.logmessage.writer.impl;

import com.generic.global.transactionlogger.Response;
import com.generic.global.transactionlogger.TransactionLogAsynchronousService;
import com.ibm.websphere.asynchbeans.Work;
import com.ibm.websphere.asynchbeans.WorkManager;
import com.generic.global.transactionlogger.Transactions;
import com.generic.logger.client.logmessage.impl.ProxyObjectMapperImpl;
import com.generic.logger.client.logmessage.interfaces.LogMessageContainer;
import com.generic.logger.client.logmessage.util.LoggerPropertyKeys;
import com.generic.logger.client.logmessage.util.LoggerPropertyUtil;
import com.generic.logger.client.logmessage.writer.interfaces.LogWriter;
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
public class LogWriterWebsphere implements LogWriter {

    @Override
    public Response writeSynchronous(LogMessageContainer logMessageContainer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void writeAsynchronous(final LogMessageContainer logMessageContainer) {

        try {

            WorkManager workManager = (WorkManager) InitialContext.doLookup("wm/ard");
            workManager.doWork(new SoapWork(logMessageContainer));

        } catch (Exception ex) {
            Logger.getLogger(LogWriterWebsphere.class.getName()).log(Level.SEVERE, ex.getMessage());
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
            Logger.getLogger(LogWriterWebsphere.class.getName()).log(Level.INFO, "[ SoapWorkLogMessage. release() was called ]");
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
                    wsdlLocation = new URL(InitialContext.<String>doLookup(LoggerPropertyKeys.LOGMESSAGESERVICE_WSDL_LOCATION));

                } catch (NamingException e) {
                    wsdlLocation = new URL(LoggerPropertyUtil.getProperty(LoggerPropertyKeys.LOGMESSAGESERVICE_WSDL_LOCATION));
                }

                // 
                // Send
                TransactionLogAsynchronousService service = new TransactionLogAsynchronousService(wsdlLocation, QName);
                service.getTransactionLogAsynchronousInPort().persist(transactions);

            } catch (MalformedURLException ex) {
                StringBuilder builder = new StringBuilder();
                builder.append("Missing key=[ ").append(LoggerPropertyKeys.LOGMESSAGESERVICE_WSDL_LOCATION).append(" ]. \n");
                builder.append("No jndi loockup for [ ").append(LoggerPropertyKeys.LOGMESSAGESERVICE_WSDL_LOCATION).append(" ] was found. \n");
                builder.append("To set application custom resource, run command ");
                builder.append("[ \n");
                builder.append("< IBM ref page >");
                builder.append(" ] \n");
                builder.append("OR set key and valid URl in logger.propperties file! \n");

                Logger.getLogger(LogWriterWebsphere.class.getName()).log(Level.SEVERE, builder.toString());
                Logger.getLogger(LogWriterWebsphere.class.getName()).log(Level.SEVERE, ex.getMessage());
            }
        }
    }
}