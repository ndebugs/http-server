/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.http.message.parser;

import com.debugs.messaging.MessageBundle;
import com.debugs.messaging.evaluator.EvaluatorHandler;
import com.debugs.messaging.evaluator.FieldEvaluationException;
import com.debugs.messaging.evaluator.MessageEvaluator;
import com.debugs.server.http.config.PackagerManager;
import com.debugs.server.http.config.Route;
import com.debugs.server.http.message.HTTPField;
import com.debugs.server.http.message.HTTPMessage;
import com.debugs.server.http.message.HTTPPackager;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public abstract class MessagePacker implements EvaluatorHandler<HTTPField, String, Object> {
    
    private final HttpServletResponse response;
    private final Route route;
    private int keyIndex;
    private List<String> fieldKeys;
    private boolean prettyPrint;

    MessagePacker(HttpServletResponse response, Route route) {
        this.response = response;
        this.route = route;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public Route getRoute() {
        return route;
    }

    public boolean isPrettyPrint() {
        return prettyPrint;
    }

    public void setPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }
    
    @Override
    public String nextKey(MessageEvaluator evaluator) {
        return keyIndex < fieldKeys.size() ? fieldKeys.get(keyIndex++) : null;
    }

    @Override
    public Object nextValue(MessageEvaluator evaluator, HTTPField field) throws FieldEvaluationException {
        return null;
    }

    protected abstract void init() throws Exception;
    
    protected abstract byte[] build() throws Exception;
    
    public byte[] pack(PackagerManager manager, HTTPMessage request) throws Exception {
        init();
        
        HTTPPackager packager = manager.loadResponse(route, request);
        HTTPMessage message = new HTTPMessage();
        
        fieldKeys = packager.keys();
        
        MessageEvaluator evaluator = new MessageEvaluator(packager, this);
        
        MessageBundle bundle = new MessageBundle(request, message);
        evaluator.evaluate(bundle);
        
        Logger log = LogManager.getLogger();
        log.info("-- Pack Message --\n{}", message);
        return build();
    }
    
    public static MessagePacker newInstance(HttpServletResponse response, Route route) {
        switch (route.getResponseType()) {
            case JSON:
                return new JSONPacker(response, route);
            case XML:
                return new XMLPacker(response, route);
        }
        return null;
    }
}
