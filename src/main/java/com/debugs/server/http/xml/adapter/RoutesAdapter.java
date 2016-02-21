/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.http.xml.adapter;

import com.debugs.server.http.config.Route;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class RoutesAdapter extends XmlAdapter<RoutesWrapper, Map<String, Route>> {

    @Override
    public Map<String, Route> unmarshal(RoutesWrapper v) throws Exception {
        Map<String, Route> map = new HashMap(v.getRoutes().length);
        for (Route route : v.getRoutes()) {
            map.put(route.getPath(), route);
        }
        return map;
    }

    @Override
    public RoutesWrapper marshal(Map<String, Route> v) throws Exception {
        RoutesWrapper list = new RoutesWrapper();
        Route[] routes = new Route[v.size()];
        int i = 0;
        for (Entry<String, Route> e : v.entrySet()) {
            routes[i++] = e.getValue();
        }
        list.setRoutes(routes);
        return list;
    }
}
