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

import com.erbjuder.logger.client.logmessage.interfaces.LogMessageData;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Stefan Andersson
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
