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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class XMLUnpacker extends MessageUnpacker {

    XMLUnpacker(HttpServletRequest request, byte[] data, Route route) {
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

    private Map<String, Object> parseMap(XMLStreamReader reader, String rootKey) throws XMLStreamException {
        Map<String, Object> map = new HashMap();
        String key = null;
        while (reader.hasNext()) {
            int state = reader.next();
            if (state == XMLStreamConstants.START_ELEMENT) {
                String tempKey = reader.getLocalName();
                if (key == null) {
                    key = tempKey;
                } else {
                    map.put(key, parseMap(reader, tempKey));
                    key = null;
                }
            } else if (state == XMLStreamConstants.END_ELEMENT) {
                String tempKey = reader.getLocalName();
                if (tempKey.equals(rootKey)) {
                    break;
                } else if (tempKey.equals(key)) {
                    key = null;
                }
            } else if (state == XMLStreamConstants.CHARACTERS && key != null) {
                Object prevValue = map.get(key);
                if (prevValue != null) {
                    if (!(prevValue instanceof List)) {
                        List list = new ArrayList();
                        list.add(prevValue);
                        prevValue = list;
                    }
                    ((List) prevValue).add(reader.getText());
                } else {
                    map.put(key, reader.getText());
                }
            }
        }
        for (Entry<String, Object> e : map.entrySet()) {
            Object value = e.getValue();
            if (value instanceof List) {
                List list = (List) value;
                Object[] values = new Object[list.size()];
                list.toArray(values);
                e.setValue(values);
            }
        }
        return map;
    }

    @Override
    protected boolean validate() {
        HttpServletRequest request = getRequest();
        return request.getMethod().equals("POST") &&
                request.getContentType().equals("application/xml");
    }
    
    @Override
    protected HTTPMessage parse() throws Exception {
        byte[] data = getData();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(in);
        Map map = parseMap(reader, null);
        
        HTTPMessage message = new HTTPMessage(map);
        return message;
    }
}
