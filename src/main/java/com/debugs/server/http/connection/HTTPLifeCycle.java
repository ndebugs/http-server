/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.http.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.util.component.LifeCycle;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class HTTPLifeCycle implements LifeCycle.Listener {

    private final Logger log = LogManager.getLogger();
    
    @Override
    public void lifeCycleStarting(LifeCycle event) {
        log.info("Server starting..");
    }

    @Override
    public void lifeCycleStarted(LifeCycle event) {
        log.info("Server started.");
    }

    @Override
    public void lifeCycleFailure(LifeCycle event, Throwable cause) {
        log.error("Server failure.", cause);
    }

    @Override
    public void lifeCycleStopping(LifeCycle event) {
        log.info("Server stopping..");
    }

    @Override
    public void lifeCycleStopped(LifeCycle event) {
        log.info("Server stopped.");
    }

}
