/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.generic.logger.client.logmessage.interfaces;

import java.io.Serializable;

/**
 *
 * @author Stefan Andersson
 */
public interface LogMessageData extends Serializable {

    public String getLabel();

    public void setLabel(String label);

    public String getContent();

    public void setContent(String content);

    public String getMimeType();

    public void setMimeType(String mimeType);
}
