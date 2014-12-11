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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Stefan Andersson
 */
public class LogMessageContainerImpl implements LogMessageContainer {

    private String uniqueId;
    private String applicationName;
    private List<LogMessage> logMessages = new ArrayList<LogMessage>();

    public LogMessageContainerImpl() {
        this.uniqueId = UUID.randomUUID().toString();
        this.applicationName = this.uniqueId;
    }

    public LogMessageContainerImpl(String applicationName, String uniqueId) {

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
    }

    public LogMessageContainerImpl(@NotNull LogMessage logMessage) {
        this(logMessage.getApplicationName(), logMessage.getUniqueId());
        this.logMessages.add(this.syncronizeLogMessage(logMessage));
    }

    @Override
    public String getUniqueId() {
        return uniqueId;
    }

    @Override
    public void setUniqueId(String uniqueId) {

        if (uniqueId != null && !uniqueId.isEmpty()) {
            if (!uniqueId.equalsIgnoreCase(this.getUniqueId())) {
                this.uniqueId = uniqueId;
                this.syncronizeLogMessages(this.getLogMessages());
            }
        }
    }

    @Override
    public String getApplicationName() {
        return applicationName;
    }

    @Override
    public void setApplicationName(String applicationName) {
        if (applicationName != null && !applicationName.isEmpty()) {

            if (!applicationName.equalsIgnoreCase(this.getApplicationName())) {
                this.applicationName = applicationName;
                this.syncronizeLogMessages(this.getLogMessages());
            }
        }
    }

    @Override
    public void addLogMessage(@NotNull LogMessage logMessage) {
        this.logMessages.add(this.syncronizeLogMessage(logMessage));
    }

    @Override
    public List<LogMessage> getLogMessages() {
        return logMessages;
    }

    @Override
    public void addLogMessages(List<LogMessage> logMessages) {
        this.logMessages.addAll(this.syncronizeLogMessages(logMessages));
    }

    @Override
    public void setLogMessages(List<LogMessage> logMessages) {
        this.logMessages = logMessages;
        this.syncronizeLogMessages(this.getLogMessages());
    }

    private List<LogMessage> syncronizeLogMessages(List<LogMessage> logMessages) {

        for (LogMessage logMessage : logMessages) {
            logMessage.setUniqueId(this.getUniqueId());
            logMessage.setApplicationName(this.getApplicationName());
        }

        return logMessages;
    }

    private LogMessage syncronizeLogMessage(LogMessage logMessage) {
        logMessage.setUniqueId(this.getUniqueId());
        logMessage.setApplicationName(this.getApplicationName());
        return logMessage;
    }
}
