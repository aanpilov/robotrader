/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.storage.routes;

import org.apache.camel.builder.RouteBuilder;

/**
 *
 * @author 1
 */
public class StockOnlineProcessingRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {        
        from("activemq:STOCK.ONLINE")
        .beanRef("factorSetMarshaller", "unmarshall")
        .beanRef("esperProcessor", "sendOnlineFactors");        
        
        from("activemq:topic:FACTOR.ARCHIVE")
        .to("log:com.robotrader.factor.archive?level=INFO")
        .beanRef("factorSetMarshaller", "unmarshall")
        .beanRef("fiboLevelsProcessor", "corellate");
    }
    
}
