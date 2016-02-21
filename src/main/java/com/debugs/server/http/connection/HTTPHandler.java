/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.http.connection;

import com.debugs.server.http.Application;
import com.debugs.server.http.config.PackagerManager;
import com.debugs.server.http.config.Route;
import com.debugs.server.http.config.Settings;
import com.debugs.server.http.message.HTTPMessage;
import com.debugs.server.http.message.parser.MessagePacker;
import com.debugs.server.http.message.parser.MessageUnpacker;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class HTTPHandler extends AbstractHandler {

    private final PackagerManager manager;
    private final Logger log;

    public HTTPHandler(PackagerManager manager) {
        this.manager = manager;
        log = LogManager.getLogger();
    }
    
    @Override
    public void handle(String target, Request baseRequest,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        byte[] requestData = translateData(request.getInputStream());
        
        log.info("--- REQUEST ---\n{}", new RequestLogger(request, requestData));
        
        Application app = Application.getInstance();
        Settings settings = app.getSettings();
        Map<String,Route> routes = settings.getRoutes();
        Route route = routes.get(request.getRequestURI());
        
        byte[] responseData = null;
        if (route != null) {
            try {
                MessageUnpacker unpacker = MessageUnpacker.newInstance(request, requestData, route);
                HTTPMessage message = unpacker.unpack(manager);
                
                MessagePacker packer = MessagePacker.newInstance(response, route);
                packer.setPrettyPrint(settings.isPrettyPrint());
                responseData = packer.pack(manager, message);
                
                response.setStatus(HttpServletResponse.SC_OK);
                OutputStream os = response.getOutputStream();
                os.write(responseData);
                os.flush();
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                log.catching(e);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        log.info("--- RESPONSE ---\n{}", new ResponseLogger(response, responseData));
    }
    
    private byte[] translateData(InputStream data) throws IOException {
        byte[] result = new byte[data.available()];
        data.read(result);
        return result;
    } 
}
