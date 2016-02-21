/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.http.xml.adapter;

import com.debugs.messaging.lang.LObject;
import com.debugs.messaging.parser.LangParser;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class ValueAdapter extends XmlAdapter<String, LObject> {

    @Override
    public LObject unmarshal(String v) throws Exception {
        LangParser parser = new LangParser(v);
        return parser.parse();
    }

    @Override
    public String marshal(LObject v) throws Exception {
        return v.toString();
    }
}
