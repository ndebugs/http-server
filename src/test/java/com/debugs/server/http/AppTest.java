package com.debugs.server.http;

import com.debugs.server.http.config.PackagerManager;
import com.debugs.server.http.config.Route;
import com.debugs.server.http.config.Settings;
import com.debugs.server.http.message.HTTPMessage;
import com.debugs.server.http.message.HTTPPackager;
import com.debugs.server.http.message.parser.MessagePacker;
import com.debugs.server.http.message.parser.MessageUnpacker;
import com.debugs.server.http.xml.XMLParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void test() throws Exception {
        System.setProperty("log4j.configurationFile", "config/log4j2.xml");
        File parent = new File("config");
        for (File file : parent.listFiles()) {
            testPackagers(file, HTTPPackager.class);
        }
        testXML("config/settings.xml", Settings.class);
        
        InputStream is = new FileInputStream("config/settings.xml");
        XMLParser<Settings> parser = new XMLParser(Settings.class);
        Settings settings = parser.unmarshall(is);
        PackagerManager manager = new PackagerManager();
        
        HTTPServletResponseImpl response = new HTTPServletResponseImpl();
        
        HTTPServletRequestImpl request = new HTTPServletRequestImpl();
        request.setRequestURI("/example/services");
        request.setMethod("GET");
        request.setQueryString("type=hello&name=Amelia");
        testMessage(manager, settings, request, response);
    }
    
    public void testPackagers(File parent, Class cls) throws Exception {
        File[] files = parent.listFiles();
        if (files != null) {
            for (File file : parent.listFiles()) {
                if (file.isDirectory()) {
                    testPackagers(file, cls);
                } else {
                    testXML(file.getAbsolutePath(), HTTPPackager.class);
                }
            }
        }
    }
    
    public void testXML(String file, Class cls) throws Exception {
        System.out.println("# --- TEST XML [" + file + "] --- #");
        InputStream is = new FileInputStream(file);
        XMLParser parser = new XMLParser(cls);
        Object target = parser.unmarshall(is);
        
        parser.marshall(target, System.out);
        System.out.println();
    }
    
    public void testMessage(PackagerManager manager, Settings settings, HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("# --- TEST MESSAGE --- #");
        Map<String,Route> routes = settings.getRoutes();
        Route route = routes.get(request.getRequestURI());
        
        MessageUnpacker unpacker = MessageUnpacker.newInstance(request, null, route);
        HTTPMessage message = unpacker.unpack(manager);
        
        MessagePacker packer = MessagePacker.newInstance(response, route);
        packer.setPrettyPrint(true);
        byte[] responseData = packer.pack(manager, message);
        System.out.println("Result: " + new String(responseData));
        System.out.println();
    }
}
