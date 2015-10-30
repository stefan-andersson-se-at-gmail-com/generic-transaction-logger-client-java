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
package com.erbjuder.logger.client.logmessage.util;

import com.erbjuder.logger.client.logmessage.interfaces.LogMessage;
import com.erbjuder.logger.client.logmessage.interfaces.LogMessageData;
import com.generic.global.transactionlogger.ObjectFactory;
import com.generic.global.transactionlogger.Transactions;
import com.sun.xml.messaging.saaj.util.Base64;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 *
 * @author Stefan Andersson
 */
public class LoggerToProxyObjects {

    public static Transactions.Transaction logMessageToTransaction(LogMessage logMessage, ObjectFactory objectFactory) {

        // 
        // Mandatory
        Transactions.Transaction transaction = objectFactory.createTransactionsTransaction();
        transaction.setApplicationName(StringEscapeUtils.escapeXml(logMessage.getApplicationName()));
        transaction.setTransactionReferenceID(StringEscapeUtils.escapeXml(logMessage.getUniqueId()));
        transaction.setUTCLocalTimeStamp(LoggerToProxyObjects.long2Gregorian(logMessage.getUTCLocalTimeStamp()));
        transaction.setUTCLocalTimeStampNanoSeconds(logMessage.getUTCLocalTimeStamp().getNanos());
        transaction.setIsError(logMessage.isIsErrorType());

        if (logMessage.getExpiryDate() != null) {
            transaction.setExpiryDate(LoggerToProxyObjects.date2Gregorian(logMessage.getExpiryDate()));
        }

        // 
        // Flow
        String flowName = logMessage.getFlowName() != null ? logMessage.getFlowName().trim() : "";
        String flowPointName = logMessage.getFlowPointName() != null ? logMessage.getFlowPointName().trim() : "";
        if (!flowName.isEmpty() && !flowPointName.isEmpty()) {
            Transactions.Transaction.TransactionLogPointInfo transactionLogPointInfo = new Transactions.Transaction.TransactionLogPointInfo();
            transactionLogPointInfo.setFlowName(StringEscapeUtils.escapeXml(flowName));
            transactionLogPointInfo.setFlowPointName(StringEscapeUtils.escapeXml(flowPointName));
            transaction.setTransactionLogPointInfo(transactionLogPointInfo);
        }

        // 
        // LogMessageData
        for (LogMessageData logData : logMessage.getLogData()) {

            String label = logData.getLabel() != null ? logData.getLabel().trim() : "";
            String content = logData.getContent() != null ? logData.getContent().trim() : "";
            String mimeType = logData.getMimeType() != null ? logData.getMimeType().trim() : "";

            if (!label.isEmpty() && !mimeType.isEmpty() && !content.isEmpty()) {
                Transactions.Transaction.TransactionLogData transactionLogData = new Transactions.Transaction.TransactionLogData();
                transactionLogData.setContentLabel(StringEscapeUtils.escapeXml(label));
                transactionLogData.setContent(Arrays.toString(Base64.encode(content.getBytes())));
                transactionLogData.setContentMimeType(mimeType);
                transaction.getTransactionLogData().add(transactionLogData);
            }
        }

        return transaction;

    }

    public static XMLGregorianCalendar date2Gregorian(Date date) {
        DatatypeFactory dataTypeFactory;
        try {
            dataTypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
        GregorianCalendar gc = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        gc.setTime(date);
        return dataTypeFactory.newXMLGregorianCalendar(gc);
    }

    public static XMLGregorianCalendar long2Gregorian(Timestamp timeStamp) {
        return date2Gregorian(new Date(timeStamp.getTime()));
    }
}
