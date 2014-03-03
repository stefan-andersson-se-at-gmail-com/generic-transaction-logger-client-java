/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.generic.logger.client.logmessage.util;

import com.generic.global.transactionlogger.ObjectFactory;
import com.generic.global.transactionlogger.Transactions;
import com.generic.logger.client.logmessage.interfaces.LogMessage;
import com.generic.logger.client.logmessage.interfaces.LogMessageData;
import com.generic.logger.client.logmessage.interfaces.LogMessageMetaInfo;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 *
 * @author ds38745
 */
public class LoggerToProxyObjects {

    public static Transactions.Transaction logMessageToTransaction(LogMessage logMessage, ObjectFactory objectFactory) {

        // 
        // Mandatory
        Transactions.Transaction transaction = objectFactory.createTransactionsTransaction();
        transaction.setApplicationName(StringEscapeUtils.escapeXml(logMessage.getApplicationName()));
        transaction.setTransactionReferenceID(StringEscapeUtils.escapeXml(logMessage.getUniqueId()));
        transaction.setUTCLocalTimeStamp(LoggerToProxyObjects.long2Gregorian(logMessage.getUTCLocalTimeStamp()));
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

            String contentLabel = logData.getContentDescription() != null ? logData.getContentDescription().trim() : "";
            String content = logData.getContent() != null ? logData.getContent().trim() : "";
            String mimeType = logData.getContentMimeType() != null ? logData.getContentMimeType().trim() : "";

            if (!contentLabel.isEmpty() && !mimeType.isEmpty()) {
                Transactions.Transaction.TransactionLogData transactionLogData = new Transactions.Transaction.TransactionLogData();
                transactionLogData.setContentDescription(StringEscapeUtils.escapeXml(contentLabel));
                transactionLogData.setContent(StringEscapeUtils.escapeXml(content));
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

    public static XMLGregorianCalendar long2Gregorian(long time) {
        return date2Gregorian(new Date(time));
    }
}
