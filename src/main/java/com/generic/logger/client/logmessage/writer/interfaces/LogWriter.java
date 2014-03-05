/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.generic.logger.client.logmessage.writer.interfaces;

import com.generic.global.transactionlogger.Response;
import com.generic.logger.client.logmessage.interfaces.LogMessageContainer;
import java.io.Serializable;

/**
 *
 * @author ds38745
 */
public interface LogWriter extends Serializable {

    public Response writeSynchronous(final LogMessageContainer logMessageContainer);
    public void writeAsynchronous(final LogMessageContainer logMessageContainer);
}
