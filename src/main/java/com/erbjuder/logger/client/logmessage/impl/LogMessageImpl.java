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
import com.erbjuder.logger.client.logmessage.interfaces.LogMessageData;
import com.erbjuder.logger.client.logmessage.util.MimeTypes;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Stefan Andersson
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
    private String description;
    private String abstractDescription;

    //
    // 
    private List<LogMessageData> logData = new ArrayList<LogMessageData>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setFlow(
            @NotNull String flowName,
            @NotNull String flowPointName) {
        this.flowName = flowName;
        this.flowPointName = flowPointName;
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
        if (this.description != null && !this.description.isEmpty()) {
            proxyLogData.add(new LogMessageDataImpl(
                    "Description",
                    this.description,
                    MimeTypes.PLAIN_TEXT));

        }

        //
        // Add already existed
        proxyLogData.addAll(this.logData);

        //
        // Result
        return proxyLogData;
    }

    @Override
    public void addContent(String label, String content, String contentMimeType) {
        if (MimeTypes.MIME_TYPES.contains(contentMimeType)) {
            logData.add(new LogMessageDataImpl(label, content, contentMimeType));
        }
    }
}