/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.generic.logger.client.logmessage.writer.impl;


import com.generic.global.transactionlogger.Response;
import com.generic.global.transactionlogger.ServiceFault;
import com.generic.global.transactionlogger.TransactionLogSynchronousService;
import com.generic.global.transactionlogger.Transactions;
import com.generic.logger.client.logmessage.impl.ProxyObjectMapperImpl;
import com.generic.logger.client.logmessage.interfaces.LogMessageContainer;
import com.generic.logger.client.logmessage.util.LoggerPropertyKeys;
import com.generic.logger.client.logmessage.util.LoggerPropertyUtil;
import com.generic.logger.client.logmessage.writer.interfaces.sync.LogWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;

/**
 *
 * @author ds38745
 */
public class LogWriterJavaStandaloneSync implements LogWriter {

    @Override
    public Response write(LogMessageContainer logMessageContainer) {
        Response response = new Response();
        response.setReturn(false);

        try {

            // 
            // Convert to proxy object
            Transactions transactions = new ProxyObjectMapperImpl().logToSOAPTransactions(logMessageContainer);

            // 
            // fetch endPoint
            QName QName = new QName("urn:generic.com:Global:TransactionLogger", "TransactionLogSynchronousService");
            URL wsdlLocation = new URL(LoggerPropertyUtil.getProperty(LoggerPropertyKeys.LOGMESSAGESERVICE_WSDL_LOCATION_SYNC));

            //
            // Send
            TransactionLogSynchronousService service = new TransactionLogSynchronousService(wsdlLocation, QName);
            response = service.getTransactionLogSynchronousInPort().persist(transactions);

        } catch (MalformedURLException ex) {
            String msgText = getMalformedURLExceptionText(LoggerPropertyKeys.LOGMESSAGESERVICE_WSDL_LOCATION_SYNC).toString();
            Logger.getLogger(LogWriterGlassFishAsync.class.getName()).log(Level.SEVERE, msgText);
            Logger.getLogger(LogWriterJavaStandaloneSync.class.getName()).log(Level.SEVERE, ex.getMessage());
        } catch (ServiceFault ex) {
            Logger.getLogger(LogWriterJavaStandaloneSync.class.getName()).log(Level.SEVERE, null, ex);
        }

        //
        // Result
        return response;
    }

   
    private StringBuilder getMalformedURLExceptionText(String wsdl_location) {
        StringBuilder builder = new StringBuilder();
        builder.append("Missing [ ").append(wsdl_location).append(" ] \n");
        builder.append("No valid URl was found in logger.propperties! \n");
        return builder;
    }
}
