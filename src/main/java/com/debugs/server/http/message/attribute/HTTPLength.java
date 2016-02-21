/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.http.message.attribute;

import com.debugs.messaging.attribute.Length;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class HTTPLength extends Length {
    
    @XmlAttribute
    @Override
    public boolean isFixed() {
        return super.isFixed();
    }

    @XmlAttribute(name="min")
    @Override
    public int getMinValue() {
        return super.getMinValue();
    }

    @XmlValue
    @Override
    public int getValue() {
        return super.getValue();
    }
}
