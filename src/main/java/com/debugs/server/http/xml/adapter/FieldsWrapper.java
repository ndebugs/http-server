/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.http.xml.adapter;

import com.debugs.server.http.message.HTTPField;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class FieldsWrapper {
    
    @XmlElement(name="field")
    private HTTPField[] fields;

    public HTTPField[] getFields() {
        return fields;
    }

    public void setFields(HTTPField[] fields) {
        this.fields = fields;
    }
}
