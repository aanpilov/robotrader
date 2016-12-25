/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.strategy.strategy;

import ta4j.strategy.Advice;
import ta4j.strategy.SimpleStrategy;
import com.robotrader.core.objects.Candle;
import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.camel.CamelContext;
import org.apache.camel.Consumer;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aav
 */
public class SberStrategy implements Processor {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private CamelContext camelContext;
    private Unmarshaller unmarshaller;
    private TimeSeries baseTimeSeries;
    private SimpleStrategy strategy;
    private Set<Consumer> consumers = new HashSet<>();

    public SberStrategy() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Candle.class);
        unmarshaller = jaxbContext.createUnmarshaller();
        baseTimeSeries = new TimeSeries("SBER_H");
        strategy = new SimpleStrategy();
    }

    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }
    
    public void start() throws Exception {
        String baseCandlesEndpointUriOptions = "selector=board='" + "TQBR" + "' AND secCode='" + "SBER" + "' AND period=" + "'PT1H'";
        Consumer consumer = camelContext.getEndpoint("amq:topic:CANDLES?" + baseCandlesEndpointUriOptions, Endpoint.class).createConsumer(this);
        consumers.add(consumer);
        consumer.start();
    }

    public void stop() throws Exception {
        for(Consumer consumer : consumers) {
            consumer.stop();
        }
    }
    
    @Override
    public void process(Exchange exchange) throws Exception {
        String messageBody = exchange.getIn().getBody(String.class);
        Object message = unmarshaller.unmarshal(new StringReader(messageBody));
        
        if(message instanceof Candle) {
            boolean isNew = exchange.getIn().getHeader("isNew", Boolean.class);
            String board = exchange.getIn().getHeader("board", String.class);
            String secCode = exchange.getIn().getHeader("secCode", String.class);
            String periodId = exchange.getIn().getHeader("period", String.class);
            Period period = Period.parse(periodId);
            
            Tick tick = unmarshall((Candle) message, period);
            
            processBaseTick(tick, isNew);
        }
    }
    
    private Tick unmarshall(Candle candle, Period period) {
        LocalDateTime startDateTime = candle.getDate();
        DateTime endDateTime = DateTime.parse(startDateTime.toString());
        endDateTime.plus(period);
        
        Tick tick = new Tick(period, endDateTime, Decimal.valueOf(candle.getOpen()), Decimal.valueOf(candle.getHigh()), Decimal.valueOf(candle.getLow()), Decimal.valueOf(candle.getClose()), Decimal.valueOf(candle.getVolume()));
        
        return tick;
    }

    private void processBaseTick(Tick tick, boolean isNew) {
        baseTimeSeries.addTick(tick);

        int endIndex = baseTimeSeries.getEnd();
        if (strategy.getAdvice(endIndex) == Advice.EXIT_LONG) {
            log.info("Strategy should EXIT_LONG on " + tick.getEndTime() + " at " + tick.getMinPrice());
        }
        if (strategy.getAdvice(endIndex) == Advice.EXIT_SHORT) {
            log.info("Strategy should EXIT_SHORT on " + tick.getEndTime() + " at " + tick.getMaxPrice());
        }
        if (strategy.getAdvice(endIndex) == Advice.ENTER_LONG) {
            log.info("Strategy should ENTER_LONG on " + tick.getEndTime() + " at " + tick.getMaxPrice());
        }
        if (strategy.getAdvice(endIndex) == Advice.EXIT_SHORT) {
            log.info("Strategy should ENTER_SHORT on " + tick.getEndTime() + " at " + tick.getMinPrice());
        }
    }
}
