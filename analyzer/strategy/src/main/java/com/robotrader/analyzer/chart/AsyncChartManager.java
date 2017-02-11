/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.chart;

import com.robotrader.core.objects.Candle;
import com.robotrader.core.objects.Security;
import com.robotrader.core.objects.Trade;
import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Tick;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aav
 */
public class AsyncChartManager extends AbstractChartManager  {
    private Logger log;
    private CamelContext camelContext;
    
    public AsyncChartManager(CamelContext camelContext, Security security, Period period) {
        super(security, period);
        log = LoggerFactory.getLogger("com.robotrader.analyzer.chart_" + security.getBoardCode() + "_" + security.getSecurityCode());
        this.camelContext = camelContext;
    }
    
    private void scheduleFlush() {
        LocalTime flushTime = LocalTime.of(19, 00, 0);
        Date flushDate = Date.from(ZonedDateTime.now().with(flushTime).toInstant());
        
        Timer flushTimer = new Timer("FlushTaimer", true);
        flushTimer.schedule(new FlushLastTask(), flushDate);
    }

    @Override
    public void start() {
        try {
            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    String tradesEndpointUriOptions = "selector=board='" + security.getBoardCode() + "' AND secCode='" + security.getSecurityCode() + "'";
                    String tradesRouteId = "trades_" + security.getBoardCode() + "_" + security.getSecurityCode();
                    
                    from("amq:topic:TRADES?" + tradesEndpointUriOptions).routeId(tradesRouteId)
                    .beanRef("jaxbContext", "unmarshall")
                    .process(exchange -> onTrade(exchange.getIn().getBody(Trade.class)));
                    
                    String candlesEndpointUriOptions = "selector=board='" + security.getBoardCode() + "' AND secCode='" + security.getSecurityCode() + "' AND period='" + getPeriod().toString() + "'";
                    String candlesRouteId = "candles_" + security.getBoardCode() + "_" + security.getSecurityCode();
                    
                    from("amq:topic:CANDLES?" + candlesEndpointUriOptions).routeId(candlesRouteId)
                    .beanRef("jaxbContext", "unmarshall")
                    .process(exchange -> onCandle(exchange.getIn().getBody(Candle.class)));
                }
            });
            
            scheduleFlush();
        } catch (Exception e) {
            log.error("Exception", e);
        }
    }
    
    public void onTrade(Trade trade) {
        DateTime dt = convertToDateTime(trade.getDate());
        addTrade(dt, Decimal.valueOf(trade.getQuantity()), Decimal.valueOf(trade.getPrice()));
    }
    
    public void onCandle(Candle candle) {        
        DateTime openDateTime = new DateTime(Date.from(candle.getDate().atZone(ZoneId.systemDefault()).toInstant()));
        DateTime closeDateTime = openDateTime.withPeriodAdded(period, 1);
        Tick tick = new Tick(closeDateTime, candle.getOpen(), candle.getHigh(), candle.getLow(), candle.getClose(), candle.getVolume());
        addArchiveTick(tick);
    }
    
    private DateTime convertToDateTime(LocalDateTime ldt) {
        ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
        return new DateTime(zdt.toInstant().toEpochMilli());
    }
    
    class FlushLastTask extends TimerTask {
        @Override
        public void run() {
            final Tick lastTick = series.getLastTick();
            log.info("Add FlushLast online tick of " + security.getSecurityCode() + ": " + lastTick);
            listeners.forEach((listener) -> {listener.onlineTickAdded(lastTick);});
        }
    }
}
