/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.adapter.core;

import com.robotrader.adapter.core.jaxb.Candle;
import com.robotrader.adapter.core.jaxb.Document;
import java.util.List;
import java.util.Map;
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
            ResteasyWebTarget target = (ResteasyWebTarget) client.target("http://www.moex.com/iss/");
            instance = target.proxy(MoexProxy.class);
        }
        
        return instance;
    }
}
