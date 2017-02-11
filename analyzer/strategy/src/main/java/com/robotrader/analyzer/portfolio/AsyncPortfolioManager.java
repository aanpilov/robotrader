/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.portfolio;

import com.robotrader.core.objects.ConditionalOrder;
import com.robotrader.core.objects.Positions;
import com.robotrader.core.objects.Security;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aav
 */
public class AsyncPortfolioManager implements PortfolioManager {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private CamelContext camelContext;
    
    private Map<Security, Portfolio> mergedPortfolio = new HashMap<>();

    public AsyncPortfolioManager(CamelContext camelContext) {
        this.camelContext = camelContext;
    }
    
    public void start() {
        try{
            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("amq:topic:POSITIONS").routeId("positions")
                    .beanRef("jaxbContext", "unmarshall")
                    .process(exchange -> onPositions(exchange.getIn().getBody(Positions.class)));
                    
                    from("amq:topic:ORDERS").routeId("orders")
                    .beanRef("jaxbContext", "unmarshall")
                    .process(exchange -> onOrder(exchange.getIn().getBody(ConditionalOrder.class)));
                }
            });
        } catch(Exception e) {
            log.error("Error", e);
        }
    }
    
    @Override
    public Portfolio getSecurityPortfolio(Security security) {
        if(!mergedPortfolio.containsKey(security)) {
            mergedPortfolio.put(security, new Portfolio());
        }
        
        return mergedPortfolio.get(security);
    }
    
    public void onPositions(Positions positions) {
        positions.getSecurityPositions().forEach(securityPosition -> {
            if(!mergedPortfolio.containsKey(securityPosition.getSecurity())) {
                mergedPortfolio.put(securityPosition.getSecurity(), new Portfolio());
            }
            
            Portfolio portfolio = mergedPortfolio.get(securityPosition.getSecurity());
            portfolio.importPositions(securityPosition.getPosition());
        });
    }
    
    public void onOrder(ConditionalOrder order) {
        log.info("Order received:" + order);
        if(!mergedPortfolio.containsKey(order.getSecurity())) {
            mergedPortfolio.put(order.getSecurity(), new Portfolio());
        }
        
        Portfolio portfolio = mergedPortfolio.get(order.getSecurity());
        portfolio.importOrders(Arrays.asList(order));
    }
}
