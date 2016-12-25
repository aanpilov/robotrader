/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

/**
 *
 * @author aav
 */
public class AdapterRoutes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("amq:topic:MYTRADES").routeId("myTrades")
        .to("log:com.robotrader.analyzer.strategy.routes?level=INFO&showAll=true&multiline=true")
        .setBody(constant("NEW_DEAL"))
        .to("amq:SMS");
                
        from("amq:SMS").routeId("smsService")
        .setHeader(Exchange.HTTP_QUERY, simple("api_id=9BD6848C-7DEE-895A-D749-0692BCAF2C7F&to=79150037293&text=${body}"))
        .to("log:com.robotrader.analyzer?showAll=true&multiline=true")
        .to("http4://sms.ru/sms/send")
        .convertBodyTo(String.class, "UTF-8")
        .to("log:com.robotrader.analyzer?showAll=true&multiline=true");
    }
}
