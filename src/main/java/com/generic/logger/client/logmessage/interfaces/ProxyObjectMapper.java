/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.generic.logger.client.logmessage.interfaces;

import com.generic.global.transactionlogger.Transactions;
import java.io.Serializable;

/**
 *
 * @author Stefan Andersson
 */
public interface ProxyObjectMapper extends Serializable {

    public Transactions logToSOAPTransactions(LogMessage logMessage);

    public Transactions logToSOAPTransactions(LogMessageContainer logContainer);
}
