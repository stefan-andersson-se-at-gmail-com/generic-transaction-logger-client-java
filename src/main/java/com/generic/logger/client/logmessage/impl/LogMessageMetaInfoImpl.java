/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.generic.logger.client.logmessage.impl;

import com.generic.logger.client.logmessage.interfaces.LogMessageMetaInfo;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ds38745
 */
public class LogMessageMetaInfoImpl implements LogMessageMetaInfo, Serializable{
    private String metaInfoLabel;
    private String metaInfoValue;
    
    public LogMessageMetaInfoImpl(
            @NotNull String metaInfoLabel,
            @NotNull String metaInfoValue) {
        this.metaInfoLabel = metaInfoLabel;
        this.metaInfoValue = metaInfoValue;
    }

    @Override
    public String getMetaInfoLabel() {
        return metaInfoLabel;
    }

    @Override
    public void setMetaInfoLabel(String metaInfoLabel) {
        this.metaInfoLabel = metaInfoLabel;
    }

    @Override
    public String getMetaInfoValue() {
        return metaInfoValue;
    }

    @Override
    public void setMetaInfoValue(String metaInfoValue) {
        this.metaInfoValue = metaInfoValue;
    }
    
}
