/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.generic.logger.client.logmessage.interfaces;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author ds38745
 */
public interface LogMessageBase extends Serializable {

    void addContent(String label, String content, String mimeType);

    List<LogMessageData> getLogData();
}
