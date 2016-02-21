/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.http.connection;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class RequestLogger {
    
    private final HttpServletRequest request;
    private final byte[] data;

    public RequestLogger(HttpServletRequest request, byte[] data) {
        this.request = request;
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder()
                .append("LocalAddr: ").append(request.getLocalAddr()).append(':').append(request.getLocalPort()).append('\n')
                .append("LocalName: ").append(request.getLocalName()).append('\n')
                .append("RemoteAddr: ").append(request.getRemoteAddr()).append(':').append(request.getRemotePort()).append('\n')
                .append("RemoteHost: ").append(request.getRemoteHost()).append('\n')
                .append("RequestURL: ").append(request.getRequestURL()).append('\n')
                .append("Protocol: ").append(request.getProtocol()).append('\n')
                .append("Method: ").append(request.getMethod()).append('\n')
                .append("Scheme: ").append(request.getScheme()).append('\n')
                .append("Locale: ").append(request.getLocale()).append('\n')
                .append("QueryString: ").append(request.getQueryString()).append('\n');
        
        sb.append("\nHEADER\n");
        Enumeration<String> headers = request.getHeaderNames();
        boolean first = true;
        while (headers.hasMoreElements()) {
            if (!first) {
                sb.append('\n');
            } else {
                first = false;
            }
            String header = headers.nextElement();
            sb.append(header)
                    .append(": ")
                    .append(request.getHeader(header));
        }
        
        sb.append("\n\nBODY\n");
        sb.append(data != null ? new String(data) : null);
        
        return sb.toString();
    }
}
