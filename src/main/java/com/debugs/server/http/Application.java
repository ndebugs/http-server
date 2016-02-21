package com.debugs.server.http;

import com.debugs.server.http.config.PackagerManager;
import com.debugs.server.http.config.Settings;
import com.debugs.server.http.connection.HTTPHandler;
import com.debugs.server.http.connection.HTTPLifeCycle;
import com.debugs.server.http.xml.XMLParser;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class Application {

    private static Application application;
    
    private Settings settings;
    private Server server;

    private Application() throws IOException, JAXBException {
        init();
    }
    
    public Settings getSettings() {
        return settings;
    }

    private void init() throws IOException, JAXBException {
        System.setProperty("log4j.configurationFile", "config/log4j2.xml");
        
        InputStream is = new FileInputStream("config/settings.xml");
        XMLParser<Settings> parser = new XMLParser(Settings.class);
        settings = parser.unmarshall(is);
    }
    
    public void start() throws Exception {
        server = new Server(settings.getPort());
        PackagerManager manager = new PackagerManager();
        HTTPHandler handler = new HTTPHandler(manager);
        server.setHandler(handler);
        server.addLifeCycleListener(new HTTPLifeCycle());
        server.start();
    }
    
    public void stop() throws Exception {
        server.stop();
        Logger log = LogManager.getLogger();
        log.info("Server closed.");
    }
    
    public static Application getInstance() {
        return application;
    }
    
    public static void main(String[] args) throws Exception {
        application = new Application();
        application.start();
        
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    application.stop();
                } catch (Exception ex) {}
            }
        });
        Runtime.getRuntime().addShutdownHook(thread);
    }
}
