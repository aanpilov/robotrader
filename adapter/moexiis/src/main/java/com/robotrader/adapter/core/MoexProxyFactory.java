/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.adapter.core;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

/**
 *
 * @author aav
 */
public class MoexProxyFactory {
    private static MoexProxy instance = null;
    
    public static MoexProxy getInstance() {
        if(instance == null) {
            ResteasyClient client = (ResteasyClient) ResteasyClientBuilder.newClient();
            
            ResteasyWebTarget target = client.target("http://www.moex.com/iss/");
            instance = MyProxyBuilder.builder(MoexProxy.class, target).build();
        }
        
        return instance;
    }
}
