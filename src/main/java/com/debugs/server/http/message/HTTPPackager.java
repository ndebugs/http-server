/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.http.message;

import com.debugs.messaging.Packager;
import com.debugs.server.http.xml.adapter.FieldsAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@XmlRootElement(name="packager")
@XmlAccessorType(XmlAccessType.FIELD)
public class HTTPPackager implements Packager<String, HTTPField> {
    
    @XmlElement
    @XmlJavaTypeAdapter(FieldsAdapter.class)
    private Map<String, HTTPField> fields;

    @Override
    public HTTPField get(String key) {
        return fields.get(key);
    }

    @Override
    public void set(String key, HTTPField field) {
        fields.put(key, field);
    }

    @Override
    public List<String> keys() {
        return new ArrayList<String>(fields.keySet());
    }

    public Map<String, HTTPField> getFields() {
        return fields;
    }

    public void setFields(Map<String, HTTPField> fields) {
        this.fields = fields;
    }
}
