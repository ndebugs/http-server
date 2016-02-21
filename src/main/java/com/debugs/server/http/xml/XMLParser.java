/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.http.xml;

import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import org.eclipse.persistence.jaxb.JAXBContextFactory;

/**
 *
 * @author van de Bugs
 */
public class XMLParser<T> {
    
    private final JAXBContext context;
    
    public XMLParser(Class<T> targetClass) throws JAXBException {
        context = JAXBContextFactory.createContext(new Class[] {targetClass}, null);
    }
    
    public void marshall(T target, OutputStream os) throws JAXBException {
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(target, os);
    }
    
    public T unmarshall(InputStream is) throws JAXBException {
        Unmarshaller unmarshaller = context.createUnmarshaller();
        unmarshaller.setEventHandler(new DefaultValidationEventHandler());
        return (T) unmarshaller.unmarshal(is);
    }
}
