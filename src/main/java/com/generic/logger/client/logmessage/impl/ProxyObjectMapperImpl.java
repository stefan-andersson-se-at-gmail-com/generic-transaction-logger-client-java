/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.generic.logger.client.logmessage.impl;

import com.generic.global.transactionlogger.ObjectFactory;
import com.generic.global.transactionlogger.Transactions;
import com.generic.logger.client.logmessage.interfaces.LogMessage;
import com.generic.logger.client.logmessage.interfaces.LogMessageContainer;
import com.generic.logger.client.logmessage.interfaces.ProxyObjectMapper;
import com.generic.logger.client.logmessage.util.LoggerToProxyObjects;

/**
 *
 * @author Stefan Andersson
 */
public class ProxyObjectMapperImpl implements ProxyObjectMapper {

    @Override
    public Transactions logToSOAPTransactions(LogMessage logMessage) {
        ObjectFactory objectFactory = new ObjectFactory();
        Transactions transactions = objectFactory.createTransactions();
        transactions.getTransaction().add(LoggerToProxyObjects.logMessageToTransaction(logMessage, objectFactory));
        return transactions;
    }

    @Override
    public Transactions logToSOAPTransactions(LogMessageContainer logContainer) {

        ObjectFactory objectFactory = new ObjectFactory();
        Transactions transactions = objectFactory.createTransactions();
        for (LogMessage logMessage : logContainer.getLogMessages()) {
            //  
            // Due performance, DO NOT CALL "logToSOAPTransactions(LogMessage logMessage)" 
            transactions.getTransaction().add(LoggerToProxyObjects.logMessageToTransaction(logMessage, objectFactory));
        }

        return transactions;
    }
}
