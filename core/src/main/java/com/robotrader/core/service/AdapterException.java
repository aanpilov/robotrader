/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.service;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * @author 1
 */
public class AdapterException extends Exception {
    private String code;

    public AdapterException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append("Code: ").append(code);
        builder.append("Cause: ").append(getCauseMessage());
        
        return builder.toString();
    }

    private String getCauseMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        
        Throwable cause = getCause();
        if(cause != null) {
            cause.printStackTrace(printWriter);
        }
        
        return stringWriter.toString();
    }
}
