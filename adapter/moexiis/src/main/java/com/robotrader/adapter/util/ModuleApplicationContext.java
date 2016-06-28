/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.adapter.util;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author aav
 */
public class ModuleApplicationContext {
    private static org.springframework.context.ApplicationContext context;

    public static org.springframework.context.ApplicationContext getContext() {
        if(context == null) {
            initContext();
        }
        
        return context;
    }

    private static void initContext() {
        context = new ClassPathXmlApplicationContext("META-INF/spring/spring-context.xml");
    }
}
