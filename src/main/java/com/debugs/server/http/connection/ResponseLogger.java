/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.http.connection;

import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class ResponseLogger {
    
    private final HttpServletResponse response;
    private final byte[] data;

    public ResponseLogger(HttpServletResponse response, byte[] data) {
        this.response = response;
        this.data = data;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder()
                .append("Status: ").append(response.getStatus()).append('\n')
                .append("Locale: ").append(response.getLocale()).append('\n');
        
        sb.append("\nHEADER\n");
        for (String header : response.getHeaderNames()) {
            sb.append(header)
                    .append(": ")
                    .append(response.getHeader(header))
                    .append('\n');
        }
        
        sb.append("\nBODY\n");
        sb.append(data != null ? new String(data) : null);
        return sb.toString();
    }
}
