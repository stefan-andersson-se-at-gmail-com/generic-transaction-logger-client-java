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
package com.erbjuder.logger.client.logmessage.interfaces;

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
