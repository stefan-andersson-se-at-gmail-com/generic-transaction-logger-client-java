/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.generic.logger.client.logmessage.interfaces;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ds38745
 */
public interface LogMessage extends LogMessageBase, Serializable {

    String getApplicationName();

    Date getExpiryDate();

    String getFlowName();

    String getFlowPointName();

    long getUTCLocalTimeStamp();

    String getUniqueId();

    boolean isIsErrorType();

    void setApplicationName(String applicationName);

    void setExpiryDate(Date expiryDate);

    void setFlow(@NotNull String flowName, @NotNull String flowPointName);

    void setFlowName(String flowName);

    void setFlowPointName(String flowPointName);

    void setIsErrorType(boolean isErrorType);

    void setUTCLocalTimeStamp(long UTCLocalTimeStamp);

    void setUniqueId(String uniqueId);

}
