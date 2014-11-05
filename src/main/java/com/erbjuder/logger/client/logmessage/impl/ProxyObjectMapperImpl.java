/* 
 * Copyright (C) 2014 erbjuder.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.erbjuder.logger.client.logmessage.impl;

import com.erbjuder.logger.client.logmessage.interfaces.LogMessage;
import com.erbjuder.logger.client.logmessage.interfaces.LogMessageContainer;
import com.erbjuder.logger.client.logmessage.interfaces.ProxyObjectMapper;
import com.erbjuder.logger.client.logmessage.util.LoggerToProxyObjects;
import com.generic.global.transactionlogger.ObjectFactory;
import com.generic.global.transactionlogger.Transactions;

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
