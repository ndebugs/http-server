/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.http.message;

import com.debugs.messaging.Field;
import com.debugs.messaging.attribute.Filter;
import com.debugs.messaging.attribute.Length;
import com.debugs.messaging.attribute.Required;
import com.debugs.messaging.lang.LObject;
import com.debugs.messaging.type.TObject;
import com.debugs.server.http.message.attribute.HTTPFilter;
import com.debugs.server.http.message.attribute.HTTPLength;
import com.debugs.server.http.message.attribute.HTTPRequired;
import com.debugs.server.http.xml.adapter.TypeAdapter;
import com.debugs.server.http.xml.adapter.ValueAdapter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@XmlType(propOrder={"type", "required", "length", "filter", "value"})
@XmlAccessorType(XmlAccessType.FIELD)
public class HTTPField extends Field<Object> {
    
    @XmlElement
    private HTTPRequired required;
    
    @XmlElement
    private HTTPLength length;
    
    @XmlElement
    private HTTPFilter filter;
    
    @XmlAttribute
    @Override
    public Object getId() {
        return super.getId();
    }

    @Override
    public void setId(Object id) {
        super.setId(id);
    }

    @XmlElement
    @XmlJavaTypeAdapter(TypeAdapter.class)
    @Override
    public TObject getType() {
        return super.getType();
    }

    @Override
    public HTTPRequired getRequired() {
        return required;
    }

    @Override
    public void setRequired(Required required) {
        this.required = (HTTPRequired) required;
    }

    @Override
    public HTTPLength getLength() {
        return length;
    }

    @Override
    public void setLength(Length length) {
        this.length = (HTTPLength) length;
    }
    
    @Override
    public HTTPFilter getFilter() {
        return filter;
    }

    @Override
    public void setFilter(Filter filter) {
        this.filter = (HTTPFilter) filter;
    }
    
    @XmlElement
    @XmlJavaTypeAdapter(ValueAdapter.class)
    @Override
    public LObject getValue() {
        return super.getValue();
    }
}
