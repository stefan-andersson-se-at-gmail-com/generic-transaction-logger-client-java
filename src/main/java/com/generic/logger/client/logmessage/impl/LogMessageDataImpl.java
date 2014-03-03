/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.generic.logger.client.logmessage.impl;

import com.generic.logger.client.logmessage.interfaces.LogMessageData;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ds38745
 */
public class LogMessageDataImpl implements LogMessageData, Serializable {

    private String label = "";
    private String mimeType = "";
    private String content = "";
    public LogMessageDataImpl(
            @NotNull String label,
            @NotNull String content,
            @NotNull String mimeType) {

        this.label = label;
        this.content = content;
        this.mimeType = mimeType;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String getMimeType() {
        return mimeType;
    }

    @Override
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    
}
