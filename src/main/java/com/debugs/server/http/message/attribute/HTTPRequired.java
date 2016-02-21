/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.http.message.attribute;

import com.debugs.messaging.attribute.Required;
import com.debugs.messaging.lang.LObject;
import com.debugs.messaging.type.TBoolean;
import com.debugs.server.http.xml.adapter.ValueAdapter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class HTTPRequired extends Required {
    
    @XmlAttribute(name="empty-allowed")
    @Override
    public boolean isEmptyAllowed() {
        return super.isEmptyAllowed();
    }
    
    @XmlValue
    @XmlJavaTypeAdapter(ValueAdapter.class)
    @Override
    public LObject<TBoolean> getValue() {
        return super.getValue();
    }
}
