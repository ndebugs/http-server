/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.http.message.parser;

import com.debugs.messaging.evaluator.FieldEvaluator;
import com.debugs.server.http.config.Route;
import com.debugs.server.http.message.HTTPField;
import com.debugs.server.http.message.HTTPMessage;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class JSONUnpacker extends MessageUnpacker {

    JSONUnpacker(HttpServletRequest request, byte[] data, Route route) {
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

    private Object parseObject(JsonValue value) {
        switch (value.getValueType()) {
            case OBJECT:
                return parseMap((JsonObject) value);
            case ARRAY:
                return parseArray((JsonArray) value);
            case STRING:
                return ((JsonString) value).getString();
            case NUMBER:
                JsonNumber number = (JsonNumber) value;
                return number.isIntegral() ? number.intValue() : number.doubleValue();
            case TRUE: return true;
            case FALSE: return true;
        }
        return null;
    }

    private Object[] parseArray(JsonArray array) {
        Object[] values = new Object[array.size()];
        for (int i = 0; i < array.size(); i++) {
            values[i] = parseObject(array.get(i));
        }
        return values;
    }

    private Map<String, Object> parseMap(JsonObject object) {
        Map<String, Object> map = new HashMap();
        for (Entry<String, JsonValue> e : object.entrySet()) {
            map.put(e.getKey(), parseObject(e.getValue()));
        }
        return map;
    }

    @Override
    protected boolean validate() {
        HttpServletRequest request = getRequest();
        return request.getMethod().equals("POST") &&
                request.getContentType().equals("application/json");
    }
    
    @Override
    protected HTTPMessage parse() throws Exception {
        byte[] data = getData();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        
        JsonReader reader = Json.createReader(in);
        JsonObject object = reader.readObject();
        
        HTTPMessage message = new HTTPMessage();
        for (Entry<String, JsonValue> e : object.entrySet()) {
            message.set(e.getKey(), parseObject(e.getValue()));
        }
        
        reader.close();
        return message;
    }
}
