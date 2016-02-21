/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.http.message;

import com.debugs.messaging.Message;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class HTTPMessage implements Message {

    private final Map<String, Object> values;

    public HTTPMessage() {
        this(new LinkedHashMap());
    }

    public HTTPMessage(Map<String, Object> values) {
        this.values = values;
    }
    
    @Override
    public Object get(Object key) {
        return values.get(key.toString());
    }

    @Override
    public void set(Object key, Object value) {
        values.put(key.toString(), value);
    }

    @Override
    public List<String> keys() {
        return new ArrayList<String>(values.keySet());
    }

    public Map<String, Object> getValues() {
        return values;
    }

    private char[] createIndent(int length) {
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = ' ';
        }
        return chars;
    }
    
    private void toObjectString(StringBuilder sb, Object target, int indent) {
        if (target instanceof Object[]) {
            toArrayString(sb, (Object[]) target, indent);
        } else if (target instanceof Map) {
            toMapString(sb, (Map) target, indent);
        } else {
            sb.append(target);
        }
    }
    
    private void toArrayString(StringBuilder sb, Object[] target, int indent) {
        sb.append('[');
        if (target.length > 0) {
            for (int i = 0; i < target.length; i++) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append('\n')
                        .append(createIndent(indent + 2));
                toObjectString(sb, target[i], indent + 2);
            }
            sb.append('\n').append(createIndent(indent));
        }
        sb.append(']');
    }
    
    private void toMapString(StringBuilder sb, Map<String, Object> target, int indent) {
        sb.append('{');
        if (target.size() > 0) {
            boolean first = true;
            for (Entry e : target.entrySet()) {
                if (!first) {
                    sb.append(',');
                } else {
                    first = false;
                }
                sb.append('\n')
                        .append(createIndent(indent + 2))
                        .append(e.getKey())
                        .append(": ");
                toObjectString(sb, e.getValue(), indent + 2);
            }
            sb.append('\n').append(createIndent(indent));
        }
        sb.append('}');
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString())
                .append(": ");
        toMapString(sb, values, 0);
        return sb.toString();
    }
}
