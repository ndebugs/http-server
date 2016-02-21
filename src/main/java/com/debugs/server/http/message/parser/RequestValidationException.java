/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.http.message.parser;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class RequestValidationException extends Exception {

    public RequestValidationException(HttpServletRequest request) {
        super("Invalid message for request: " + request);
    }
}
