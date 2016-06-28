/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author 1
 */
public class CoreContextInstance {
    private static ApplicationContext context;
    
    public static ApplicationContext getContext() {
        if(context == null) {
            context = new ClassPathXmlApplicationContext("META-INF/spring/spring-context.xml");
        }
        
        return context;
    }
}
