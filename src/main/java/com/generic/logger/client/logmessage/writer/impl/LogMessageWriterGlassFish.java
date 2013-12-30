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
                        URL wsdlLocation = new URL(LoggerPropertyUtil.getProperty(LoggerPropertyKeys.LOGMESSAGESERVICE_WSDL_LOCATION));

                        //
                        // Send
                        LogMessageService service = new LogMessageService(wsdlLocation, QName);
                        service.getTransactionLoggerInPort().persist(transactions);


                    } catch (MalformedURLException ex) {
                        Logger.getLogger(LogMessageWriterGlassFish.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            };

            threadPoolExecutor.execute(task);

        } catch (Exception ex) {
            Logger.getLogger(LogMessageWriterGlassFish.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
