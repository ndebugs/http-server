/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.http.message.parser;

import com.debugs.messaging.evaluator.FieldEvaluator;
import com.debugs.server.http.config.Route;
import com.debugs.server.http.message.HTTPField;
import java.io.ByteArrayOutputStream;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class JSONPacker extends MessagePacker {

    private ArrayDeque tempResult;
    private JsonObjectBuilder tempBuilder;
    
    JSONPacker(HttpServletResponse response, Route route) {
        super(response, route);
    }

    @Override
    public void onEvaluateObject(FieldEvaluator evaluator, HTTPField field, Object value, int depth) throws Exception {
        Object objectBuilder = tempResult.getFirst();
        if (objectBuilder instanceof JsonObjectBuilder) {
            JsonObjectBuilder builder = (JsonObjectBuilder) objectBuilder;
            String id = (String) field.getId();
            if (value == null) {
                builder.add(id, JsonValue.NULL);
            } else if (value instanceof Boolean) {
                builder.add(id, (Boolean) value ?
                        JsonValue.TRUE : JsonValue.FALSE);
            } else if (value instanceof Double) {
                builder.add(id, (Double) value);
            } else if (value instanceof Integer) {
                builder.add(id, (Integer) value);
            } else {
                builder.add(id, value.toString());
            }
        } else if (objectBuilder instanceof JsonArrayBuilder) {
            JsonArrayBuilder builder = (JsonArrayBuilder) objectBuilder;
            if (value == null) {
                builder.add(JsonValue.NULL);
            } else if (value instanceof Boolean) {
                builder.add((Boolean) value ?
                        JsonValue.TRUE : JsonValue.FALSE);
            } else if (value instanceof Double) {
                builder.add((Double) value);
            } else if (value instanceof Integer) {
                builder.add((Integer) value);
            } else {
                builder.add(value.toString());
            }
        }
    }

    @Override
    public void onPreEvaluateArray(FieldEvaluator evaluator, HTTPField field, Object[] value, int depth) throws Exception {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        tempResult.push(builder);
    }

    @Override
    public void onPostEvaluateArray(FieldEvaluator evaluator, HTTPField field, Object[] value, int depth) throws Exception {
        JsonArrayBuilder builder = (JsonArrayBuilder) tempResult.pop();
        
        Object objectBuilder = tempResult.getFirst();
        if (objectBuilder instanceof JsonObjectBuilder) {
            String id = (String) field.getId();
            ((JsonObjectBuilder) objectBuilder).add(id, builder.build());
        } else if (objectBuilder instanceof JsonArrayBuilder) {
            ((JsonArrayBuilder) objectBuilder).add(builder.build());
        }
    }

    @Override
    public void onPreEvaluateMap(FieldEvaluator evaluator, HTTPField field, Map value, int depth) throws Exception {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        tempResult.push(builder);
    }

    @Override
    public void onPostEvaluateMap(FieldEvaluator evaluator, HTTPField field, Map value, int depth) throws Exception {
        JsonObjectBuilder builder = (JsonObjectBuilder) tempResult.pop();
        
        Object objectBuilder = tempResult.getFirst();
        if (objectBuilder instanceof JsonObjectBuilder) {
            String id = (String) field.getId();
            ((JsonObjectBuilder) objectBuilder).add(id, builder.build());
        } else if (objectBuilder instanceof JsonArrayBuilder) {
            ((JsonArrayBuilder) objectBuilder).add(builder.build());
        }
    }

    @Override
    protected void init() throws Exception {
        tempResult = new ArrayDeque();
        tempBuilder = Json.createObjectBuilder();
        tempResult.push(tempBuilder);
    }

    @Override
    protected byte[] build() throws Exception {
        JsonObject object = tempBuilder.build();
        byte[] result;
        
        if (isPrettyPrint()) {
            Map<String, Boolean> config = new HashMap(1);
            config.put(JsonGenerator.PRETTY_PRINTING, true);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JsonWriterFactory jwf = Json.createWriterFactory(config);
            JsonWriter jsonWriter = jwf.createWriter(out);
            jsonWriter.writeObject(object);
            jsonWriter.close();
            
            result = out.toByteArray();
        } else {
            String data = object.toString();
            result = data.getBytes();
        }
        
        HttpServletResponse response = getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.setContentLength(result.length);
        return result;
    }
}
