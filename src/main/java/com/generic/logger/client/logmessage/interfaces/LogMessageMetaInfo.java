/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.generic.logger.client.logmessage.interfaces;

import java.io.Serializable;

/**
 *
 * @author ds38745
 */
public interface LogMessageMetaInfo extends Serializable{

    public String getMetaInfoLabel();
    public void setMetaInfoLabel(String metaInfoLabel);
    public String getMetaInfoValue();
    public void setMetaInfoValue(String metaInfoValue);
}
