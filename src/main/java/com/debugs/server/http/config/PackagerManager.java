/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.http.config;

import com.debugs.messaging.Message;
import com.debugs.server.http.message.HTTPPackager;
import com.debugs.server.http.xml.XMLParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class PackagerManager {
    
    private final Map<String, HTTPPackager> packagers;
    private final Logger log;

    public PackagerManager() {
        packagers = new HashMap();
        log = LogManager.getLogger();
    }

    public HTTPPackager loadRequest(Route route, Message message) throws FileNotFoundException, JAXBException {
        return loadPackager(route, message, "req");
    }
    
    public HTTPPackager loadResponse(Route route, Message message) throws FileNotFoundException, JAXBException {
        return loadPackager(route, message, "res");
    }
    
    private String mergeValues(String[] keys, Message message, int length) {
        if (keys != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                Object fieldValue = message.get(keys[i]);
                sb.append('-');
                if (fieldValue != null) {
                    sb.append(fieldValue.toString());
                }
            }
            return sb.toString();
        } else {
            return null;
        }
    }
    
    private HTTPPackager loadPackager(Route route, Message message, String id) throws FileNotFoundException, JAXBException {
        String[] keys = route.getFieldKeys();
        int keysCount = keys != null ? keys.length : 0;
        HTTPPackager packager = null;
        String defaultFilename = "config" + route.getPath() + "/" + id;
        List<String> keyList = new ArrayList();
        do {
            String filename = (keysCount > 0 ?
                    defaultFilename + mergeValues(keys, message, keysCount) :
                    defaultFilename) +
                    ".xml";
            packager = packagers.get(filename);
            if (packager == null) {
                File file = new File(filename);
                log.info("Finding packager:\n{}", filename);
                
                if (file.exists()) {
                    log.info("Loading packager:\n{}", filename);
                    InputStream is = new FileInputStream(file);
                    XMLParser<HTTPPackager> parser = new XMLParser(HTTPPackager.class);
                    packager = parser.unmarshall(is);
                    
                    log.info("Packager loaded:\n{}", filename);
                }
                keyList.add(filename);
            } else {
                log.info("Using packager:\n{}", filename);
            }
        } while(packager == null && --keysCount > -1);
        
        if (packager != null) {
            for (String key : keyList) {
                packagers.put(key, packager);
            }
        } else {
            log.info("Packager not found.");
        }
        return packager;
    }
}
