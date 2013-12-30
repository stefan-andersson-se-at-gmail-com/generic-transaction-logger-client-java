/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.generic.logger.client.logmessage.impl;

import com.generic.logger.client.logmessage.interfaces.LogMessage;
import com.generic.logger.client.logmessage.interfaces.LogMessageData;
import com.generic.logger.client.logmessage.interfaces.LogMessageMetaInfo;
import com.generic.logger.client.logmessage.util.MimeTypes;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ds38745
 */
public class LogMessageImpl implements LogMessage {

    // 
    // Mandatory
    private String applicationName;
    private String uniqueId;
    private long UTCLocalTimeStamp;
    private boolean isErrorType = false;
    private Date expiryDate;
    //
    // Flow
    private String flowName;
    private String flowPointName;
    //
    // Extra : Vill endup as proxyLogData
    private String abstractDescription;
    private String content;
    private String contentMimeType;
    private String executionTime;
    //
    // 
    private List<LogMessageData> logData = new ArrayList<LogMessageData>();
    private List<LogMessageMetaInfo> metaInfo = new ArrayList<LogMessageMetaInfo>();

    public LogMessageImpl() {
        this.uniqueId = UUID.randomUUID().toString();
        this.applicationName = this.uniqueId;
        this.UTCLocalTimeStamp = new Date().getTime();
    }

    public LogMessageImpl(String uniqueId) {

        if (uniqueId == null || uniqueId.isEmpty()) {
            this.uniqueId = UUID.randomUUID().toString();
        } else {
            this.uniqueId = uniqueId;
        }

        this.applicationName = this.uniqueId;
        this.UTCLocalTimeStamp = new Date().getTime();

    }

    public LogMessageImpl(
            String applicationName,
            String uniqueId,
            boolean isErrorType,
            Date expiryDate) {

        if (uniqueId == null || uniqueId.isEmpty()) {
            this.uniqueId = UUID.randomUUID().toString();
        } else {
            this.uniqueId = uniqueId;
        }
        if (applicationName == null || applicationName.isEmpty()) {
            this.applicationName = this.uniqueId;
        } else {
            this.applicationName = applicationName;
        }

        this.UTCLocalTimeStamp = new Date().getTime();
        this.isErrorType = isErrorType;

        if (expiryDate != null) {
            this.expiryDate = expiryDate;
        }
    }

    public String getAbstractDescription() {
        return abstractDescription;
    }

    public void setAbstractDescription(String abstractDescription) {
        this.abstractDescription = abstractDescription;
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

    public void setContent(String content, String contentMimeType) {
        this.content = content;
        this.contentMimeType = contentMimeType;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setContentMimeType(String contentMimeType) {
        if (MimeTypes.MIME_TYPES.contains(contentMimeType)) {
            this.contentMimeType = contentMimeType;
        }
    }

    public String getContent() {
        return this.content;
    }

    public String getContentMimeType() {
        return this.contentMimeType;
    }

    @Override
    public void setFlow(
            @NotNull String flowName,
            @NotNull String flowPointName) {
        this.flowName = flowName;
        this.flowPointName = flowPointName;
    }

    @Override
    public void addMetaInfo(
            @NotNull String metaInfoLabel,
            @NotNull String metaInfoValue) {
        metaInfo.add(new LogMessageMetaInfoImpl(metaInfoLabel, metaInfoValue));
    }

    @Override
    public String getApplicationName() {
        return applicationName;
    }

    @Override
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    @Override
    public String getUniqueId() {
        return uniqueId;
    }

    @Override
    public void setUniqueId(String uniqueId) {
        if (uniqueId != null && !uniqueId.isEmpty()) {
            this.uniqueId = uniqueId;
        }
    }

    @Override
    public long getUTCLocalTimeStamp() {
        return UTCLocalTimeStamp;
    }

    @Override
    public void setUTCLocalTimeStamp(long UTCLocalTimeStamp) {
        this.UTCLocalTimeStamp = UTCLocalTimeStamp;
    }

    @Override
    public boolean isIsErrorType() {
        return isErrorType;
    }

    @Override
    public void setIsErrorType(boolean isErrorType) {
        this.isErrorType = isErrorType;
    }

    @Override
    public Date getExpiryDate() {
        return expiryDate;
    }

    @Override
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String getFlowName() {
        return flowName;
    }

    @Override
    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    @Override
    public String getFlowPointName() {
        return flowPointName;
    }

    @Override
    public void setFlowPointName(String flowPointName) {
        this.flowPointName = flowPointName;
    }

    @Override
    public List<LogMessageMetaInfo> getMetaInfo() {

        List<LogMessageMetaInfo> outMetaInfo = new ArrayList<LogMessageMetaInfo>();

        //
        // Execution time
        if(this.executionTime != null && !this.executionTime.isEmpty())
        outMetaInfo.add(new LogMessageMetaInfoImpl(
                "Execution Time",
                this.executionTime));

        outMetaInfo.addAll(this.metaInfo);

        return outMetaInfo;
    }

    @Override
    public void setMetaInfo(List<LogMessageMetaInfo> metaInfo) {
        this.metaInfo = metaInfo;
    }

    @Override
    public List<LogMessageData> getLogData() {

        List<LogMessageData> proxyLogData = new ArrayList<LogMessageData>();

        // 
        // Abstract 
        if (this.abstractDescription != null && !this.abstractDescription.isEmpty()) {
            proxyLogData.add(new LogMessageDataImpl(
                    "Abstract Description",
                    this.abstractDescription,
                    MimeTypes.PLAIN_TEXT));
        }

        //
        // Description
        if (this.content != null && !this.content.isEmpty()) {
            if (this.contentMimeType == null || this.contentMimeType.isEmpty()) {

                proxyLogData.add(new LogMessageDataImpl(
                        "Content Description",
                        this.content,
                        MimeTypes.PLAIN_TEXT));

            } else {
                proxyLogData.add(new LogMessageDataImpl(
                        "Content Description",
                        this.content,
                        MimeTypes.PLAIN_TEXT));
            }
        }

        //
        // Add already existed
        proxyLogData.addAll(this.logData);

        //
        // Result
        return proxyLogData;
    }

    @Override
    public void addContent(String contentDescription, String content, String contentMimeType) {
        if (MimeTypes.MIME_TYPES.contains(contentMimeType)) {
            logData.add(new LogMessageDataImpl(contentDescription, content, contentMimeType));
        }
    }
}
