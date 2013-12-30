/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.generic.logger.client.logmessage.interfaces;

import java.util.List;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ds38745
 */
public interface LogMessageContainer {
    
    void addLogMessage(@NotNull LogMessage logMessage);

    void addLogMessages(List<LogMessage> logMessages);

    String getApplicationName();

    List<LogMessage> getLogMessages();
    
    void setLogMessages(List<LogMessage> logMessages);
    
    String getUniqueId();

    void setApplicationName(String applicationName);

    void setUniqueId(String uniqueId);
   
}
