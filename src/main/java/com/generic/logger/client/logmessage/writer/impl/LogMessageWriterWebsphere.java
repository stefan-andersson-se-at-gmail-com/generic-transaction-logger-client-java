/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.generic.logger.client.logmessage.writer.impl;

import com.ibm.websphere.asynchbeans.Work;
import com.ibm.websphere.asynchbeans.WorkManager;
import com.generic.global.transactionlogger.LogMessageService;
import com.generic.global.transactionlogger.Transactions;
import com.generic.logger.client.logmessage.impl.ProxyObjectMapperImpl;
import com.generic.logger.client.logmessage.interfaces.LogMessageContainer;
import com.generic.logger.client.logmessage.util.LoggerPropertyKeys;
import com.generic.logger.client.logmessage.util.LoggerPropertyUtil;
import com.generic.logger.client.logmessage.writer.interfaces.LogMessageWriter;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.xml.namespace.QName;

/**
 *
 * @author ds38745
 */
public class LogMessageWriterWebsphere implements LogMessageWriter {

    public void write(final LogMessageContainer logMessageContainer) {

        try {

            WorkManager workManager = (WorkManager) InitialContext.doLookup("wm/ard");
            workManager.doWork(new SoapWork(logMessageContainer));

        } catch (Exception ex) {
            Logger.getLogger(LogMessageWriterWebsphere.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(LogMessageWriterWebsphere.class.getName()).log(Level.INFO, "[ SoapWorkLogMessage. release() was called ]");
        }

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

            } catch (Exception ex) {
                Logger.getLogger(LogMessageWriterWebsphere.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
