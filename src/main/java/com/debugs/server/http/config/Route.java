/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.http.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Route {
    
    @XmlAttribute
    private String path;
    
    @XmlElement(name="request-type")
    private RequestType requestType;
    
    @XmlElement(name="response-type")
    private ResponseType responseType;

    @XmlElement(name="field-key")
    @XmlElementWrapper(name="field-keys")
    private String[] fieldKeys;
    
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }
    
    public String[] getFieldKeys() {
        return fieldKeys;
    }

    public void setFieldKeys(String[] fieldKeys) {
        this.fieldKeys = fieldKeys;
    }
}
