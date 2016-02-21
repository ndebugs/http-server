/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.http.message.parser;

import com.debugs.messaging.evaluator.FieldEvaluator;
import com.debugs.server.http.config.RequestType;
import com.debugs.server.http.config.Route;
import com.debugs.server.http.message.HTTPField;
import com.debugs.server.http.message.HTTPMessage;
import java.net.URLDecoder;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class HTTPUnpacker extends MessageUnpacker {

    HTTPUnpacker(HttpServletRequest request, byte[] data, Route route) {
        super(request, data, route);
    }

    @Override
    public void onEvaluateObject(FieldEvaluator evaluator, HTTPField field, Object value, int depth) throws Exception {}

    @Override
    public void onPreEvaluateArray(FieldEvaluator evaluator, HTTPField field, Object[] value, int depth) throws Exception {}

    @Override
    public void onPostEvaluateArray(FieldEvaluator evaluator, HTTPField field, Object[] value, int depth) throws Exception {}
    
    @Override
    public void onPreEvaluateMap(FieldEvaluator evaluator, HTTPField field, Map value, int depth) throws Exception {}

    @Override
    public void onPostEvaluateMap(FieldEvaluator evaluator, HTTPField field, Map value, int depth) throws Exception {}

    @Override
    protected boolean validate() {
        HttpServletRequest request = getRequest();
        RequestType type = getRoute().getRequestType();
        return RequestType.valueOf(request.getMethod()).equals(type);
    }
    
    @Override
    protected HTTPMessage parse() throws Exception {
        HttpServletRequest request = getRequest();
        
        RequestType type = getRoute().getRequestType();
        String data = type == RequestType.GET ?
                request.getQueryString() : new String(getData());
        
        HTTPMessage message = new HTTPMessage();
        if (data != null) {
            String[] params = data.split("&");
            for (String param : params) {
                int index  = param.indexOf('=');
                String key;
                String value = null;
                if (index > 0) {
                    key = URLDecoder.decode(param.substring(0, index), "UTF-8");
                    value = URLDecoder.decode(param.substring(index + 1), "UTF-8");
                } else {
                    key = param;
                }
                message.set(key, value);
            }
        }
        
        return message;
    }
}
