/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.http.message.parser;

import com.debugs.messaging.MessageBundle;
import com.debugs.messaging.evaluator.EvaluatorHandler;
import com.debugs.messaging.evaluator.FieldEvaluationException;
import com.debugs.messaging.evaluator.MessageEvaluator;
import com.debugs.messaging.evaluator.NoFieldException;
import com.debugs.server.http.config.PackagerManager;
import com.debugs.server.http.config.Route;
import com.debugs.server.http.message.HTTPField;
import com.debugs.server.http.message.HTTPMessage;
import com.debugs.server.http.message.HTTPPackager;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public abstract class MessageUnpacker implements EvaluatorHandler<HTTPField, String, Object> {
    
    private final HttpServletRequest request;
    private final byte[] data;
    private final Route route;
    private HTTPMessage message;
    private int keyIndex;
    private List<String> fieldKeys;

    MessageUnpacker(HttpServletRequest request, byte[] data, Route route) {
        this.request = request;
        this.data = data;
        this.route = route;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public byte[] getData() {
        return data;
    }

    public Route getRoute() {
        return route;
    }

    @Override
    public String nextKey(MessageEvaluator evaluator) {
        return keyIndex < fieldKeys.size() ? fieldKeys.get(keyIndex++) : null;
    }

    @Override
    public Object nextValue(MessageEvaluator evaluator, HTTPField field) throws FieldEvaluationException {
        return message.get(field.getId());
    }
    
    protected abstract boolean validate();
    
    protected abstract HTTPMessage parse() throws Exception;
    
    public final HTTPMessage unpack(PackagerManager manager) throws Exception {
        if (!validate()) {
            throw new RequestValidationException(request);
        }
        message = parse();
        HTTPPackager packager = manager.loadRequest(route, message);
        
        fieldKeys = packager.keys();
        for (String key : message.keys()) {
            if (!fieldKeys.contains(key)) {
                throw new NoFieldException(key);
            }  
        }
        
        MessageEvaluator evaluator = new MessageEvaluator(packager, this);
        
        MessageBundle bundle = new MessageBundle(message);
        evaluator.evaluate(bundle);
        
        Logger log = LogManager.getLogger();
        log.info("-- Unpack Message --\n{}", message);
        return message;
    }
    
    public static MessageUnpacker newInstance(HttpServletRequest request, byte[] data, Route route) {
        switch (route.getRequestType()) {
            case JSON:
                return new JSONUnpacker(request, data, route);
            case XML:
                return new XMLUnpacker(request, data, route);
            case GET:
            case POST:
                return new HTTPUnpacker(request, data, route);
        }
        return null;
    }
}
