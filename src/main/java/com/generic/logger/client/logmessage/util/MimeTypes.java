/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.generic.logger.client.logmessage.util;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Stefan Andersson
 */
public class MimeTypes {
    
    public static final String PLAIN_TEXT = "text/plain";
    public static final String BASE64 = "application/base64";
    public static final Set<String> MIME_TYPES = new HashSet<String>();
    
    static {
        MIME_TYPES.add(MimeTypes.BASE64);
        MIME_TYPES.add(MimeTypes.PLAIN_TEXT);
    }
}
