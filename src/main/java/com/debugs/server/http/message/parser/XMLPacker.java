/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.http.message.parser;

import com.debugs.messaging.evaluator.FieldEvaluator;
import com.debugs.server.http.config.Route;
import com.debugs.server.http.message.HTTPField;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class XMLPacker extends MessagePacker {

    private XMLStreamWriter writer;
    private ByteArrayOutputStream tempOutput;
    
    XMLPacker(HttpServletResponse response, Route route) {
        super(response, route);
    }

    @Override
    public void onEvaluateObject(FieldEvaluator evaluator, HTTPField field, Object value, int depth) throws Exception {
        writeIndent(depth);
        String id = (String) field.getId();
        writer.writeStartElement(id);
        if (value != null) {
            writer.writeCharacters(value.toString());
        }
        writer.writeEndElement();
    }

    @Override
    public void onPreEvaluateArray(FieldEvaluator evaluator, HTTPField field, Object[] value, int depth) throws Exception {}

    @Override
    public void onPostEvaluateArray(FieldEvaluator evaluator, HTTPField field, Object[] value, int depth) throws Exception {}

    @Override
    public void onPreEvaluateMap(FieldEvaluator evaluator, HTTPField field, Map value, int depth) throws Exception {
        writeIndent(depth);
        String id = (String) field.getId();
        writer.writeStartElement(id);
    }

    @Override
    public void onPostEvaluateMap(FieldEvaluator evaluator, HTTPField field, Map value, int depth) throws Exception {
        writeIndent(depth);
        writer.writeEndElement();
    }
    
    private void writeIndent(int length) throws XMLStreamException {
        if (isPrettyPrint()) {
            char[] chars = new char[length + 1];
            chars[0] = '\n';
            for (int i = 1; i <= length; i++) {
                chars[i] = '\t';
            }
            String indent = new String(chars);
            writer.writeCharacters(indent);
        }
    }

    @Override
    protected void init() throws Exception {
        tempOutput = new ByteArrayOutputStream();
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        
        writer = factory.createXMLStreamWriter(tempOutput);
        writer.writeStartDocument();
    }

    @Override
    protected byte[] build() throws Exception {
        writer.writeEndDocument();
        writer.flush();
        
        HttpServletResponse response = getResponse();
        response.setContentType("application/xml;charset=UTF-8");
        response.setContentLength(tempOutput.size());
        return tempOutput.toByteArray();
    }
}
