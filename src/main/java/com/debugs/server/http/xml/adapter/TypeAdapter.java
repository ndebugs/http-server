/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.http.xml.adapter;

import com.debugs.messaging.parser.TypeParser;
import com.debugs.messaging.type.TObject;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class TypeAdapter extends XmlAdapter<String, TObject> {

    @Override
    public TObject unmarshal(String v) throws Exception {
        TypeParser parser = new TypeParser(v);
        return parser.parse();
    }

    @Override
    public String marshal(TObject v) throws Exception {
        return v.alias();
    }
}
