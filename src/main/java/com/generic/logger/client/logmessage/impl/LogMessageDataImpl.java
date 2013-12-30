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

    private String contentDescription = "";
    private String contentMimeType = "";
    private String content = "";
    public LogMessageDataImpl(
            @NotNull String contentDescription,
            @NotNull String content,
            @NotNull String contentMimeType) {

        this.contentDescription = contentDescription;
        this.content = content;
        this.contentMimeType = contentMimeType;
    }

    @Override
    public String getContentDescription() {
        return contentDescription;
    }

    @Override
    public void setContentDescription(String contentDescription) {
        this.contentDescription = contentDescription;
    }

    @Override
    public String getContentMimeType() {
        return contentMimeType;
    }

    @Override
    public void setContentMimeType(String contentMimeType) {
        this.contentMimeType = contentMimeType;
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
