/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.http.xml.adapter;

import com.debugs.messaging.Field;
import com.debugs.messaging.type.TObject;
import com.debugs.messaging.type.TList;
import com.debugs.server.http.message.HTTPField;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class FieldsAdapter extends XmlAdapter<FieldsWrapper, Map<String, HTTPField>> {

    @Override
    public Map<String, HTTPField> unmarshal(FieldsWrapper v) throws Exception {
        Map<String, HTTPField> map = new LinkedHashMap(v.getFields().length);
        for (HTTPField field : v.getFields()) {
            unpackChild(map, field);
        }
        return map;
    }

    @Override
    public FieldsWrapper marshal(Map<String, HTTPField> v) throws Exception {
        FieldsWrapper list = new FieldsWrapper();
        List<HTTPField> fieldList = new ArrayList();
        for (Entry<String, HTTPField> e : v.entrySet()) {
            packChild(fieldList, e.getValue());
        }
        HTTPField[] fields = new HTTPField[fieldList.size()];
        fieldList.toArray(fields);
        list.setFields(fields);
        return list;
    }
    
    private void unpackChild(Map<String, HTTPField> fieldMap, HTTPField field) {
        String key = (String) field.getId();
        List<String> keys = new ArrayList();
        int index = 0;
        boolean readChild = false;
        for (int i = index; i < key.length(); i++) {
            char c = key.charAt(i);
            if (c == '[') {
                if (index == 0) {
                    keys.add(key.substring(index, i));
                }
                index = i + 1;
                readChild = true;
            } else if (c == ']' && readChild) {
                keys.add(index != i ? key.substring(index, i) : null);
                readChild = false;
            }
        }
        if (!keys.isEmpty())  {
            HTTPField parent = fieldMap.get(keys.get(0));
            for (int i = 1; i < keys.size(); i++) {
                TObject type = parent.getType();
                String fieldId = keys.get(i);
                Object fieldKey = fieldId != null && type instanceof TList ?
                        Integer.parseInt(keys.get(i)) : fieldId;
                Map<Object, Field> childs = parent.getChilds();
                if (i == keys.size() - 1) {
                    if (childs == null) {
                        childs = new LinkedHashMap();
                        parent.setChilds(childs);
                    }
                    field.setId(fieldId);
                    childs.put(fieldKey, field);
                } else {
                    parent = (HTTPField) childs.get(fieldKey);
                }
            }
        } else {
            fieldMap.put(key, field);
        }
    }
    
    private void packChild(List<HTTPField> fieldList, HTTPField field) {
        fieldList.add(field);
        Map<Object, Field> childs = field.getChilds();
        if (childs != null) {
            for (Entry<Object, Field> e : childs.entrySet()) {
                HTTPField f = (HTTPField) e.getValue();
                String k = f.getId() != null ?
                        field.getId() + "[" + e.getKey() + "]" :
                        field.getId() + "[]";
                f.setId(k);
                packChild(fieldList, f);
            }
        }
    }
}
