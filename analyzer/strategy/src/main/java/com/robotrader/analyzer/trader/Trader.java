/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.trader;

import com.robotrader.analyzer.chart.AsyncChartManager;
import com.robotrader.analyzer.portfolio.AsyncPortfolioManager;
import com.robotrader.analyzer.strategy.StrategyManager;
import com.robotrader.core.objects.Security;
import com.robotrader.core.objects.ServerStatus;
import com.robotrader.core.service.AsyncAdapterService;
import eu.verdelhan.ta4j.Decimal;
import java.io.StringReader;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.apache.camel.CamelContext;
import org.apache.camel.Consumer;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ta4j.strategy.ReductionStrategy;

/**
 *
 * @author aav
 */
public class Trader implements Processor {
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    private AsyncAdapterService adapterService;
    private CamelContext camelContext;
    
    private Unmarshaller unmarshaller;
    private final Set<Consumer> consumers = new HashSet<>();
    private ProducerTemplate producer;
    
    private boolean connected;

    public void setAdapterService(AsyncAdapterService adapterService) {
        this.adapterService = adapterService;
    }
    
    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }
    
    public void start() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(ServerStatus.class);
        unmarshaller = jaxbContext.createUnmarshaller();
        
        producer = camelContext.createProducerTemplate();
        
        Consumer consumer = camelContext.getEndpoint("amq:topic:SERVER_STATUS", Endpoint.class).createConsumer(this);
        consumers.add(consumer);
        consumer.start();
        
        initStrategy();
        
        connect();
    }
    
    private void connect() throws Exception {
        adapterService.start();
    }
    
    public void stop() throws Exception {
        adapterService.stop();
        
        for(Consumer consumer : consumers) {
            consumer.stop();
        }
    }
    
    private void scheduleConnection() {
        LocalTime currentTime = LocalTime.now();
        LocalTime sessionStartTime = LocalTime.of(9, 45, 0);
        
        ConnectionTask connectionTask = new ConnectionTask(this);
        
        if(currentTime.isBefore(sessionStartTime)) {
            ZonedDateTime sessionStartDateTime = ZonedDateTime.now().with(sessionStartTime);
            Date sessionStartDate = Date.from(sessionStartDateTime.toInstant());
            
            Timer connectionTimer = new Timer("connectionTimer");
            connectionTimer.schedule(connectionTask, sessionStartDate);
            log.info("Connection scheduled at: " + sessionStartDate);
        } else {
            log.info("Try connect now...");
            connectionTask.run();            
        }
    }

    private void sendSmsMessage(String text) throws Exception {
        producer.sendBodyAndHeader("amq:SMS", text, "phoneNumber", "79150037293");
    }
    
    @Override
    public void process(Exchange exchange) throws Exception {
        String messageBody = exchange.getIn().getBody(String.class);
        Object message = unmarshaller.unmarshal(new StringReader(messageBody));
        
        if(message instanceof ServerStatus) {
            ServerStatus serverStatus = (ServerStatus) message;
            onServerStatus(serverStatus);
        }
    }

    public void onServerStatus(ServerStatus serverStatus) throws Exception {
        if(!"true".equals(serverStatus.getStatus())) {
            log.info("Disconnected from server. Prev status: " + connected);
            scheduleConnection();
            if(connected) {
                //Разрыв соединения
                connected = false;
                sendSmsMessage("Disconnected from server");                
            }
        } else {
            log.info("Сonnected to server. Prev status: " + connected);
            if(!connected) {
                connected = true;
                sendSmsMessage("Connected to server");
                onConnected();
            }            
        }
    }
    
    private void initStrategy() throws Exception {
        AsyncPortfolioManager portfolioManager = new AsyncPortfolioManager(camelContext);
        portfolioManager.start();
        
        Security fut = new Security(null, null, "FUT", "SRH7");
        AsyncChartManager futChartManager = new AsyncChartManager(camelContext, fut, Period.hours(1));
        futChartManager.start();
        
        Security base = new Security(null, null, "TQBR", "SBER");
        AsyncChartManager baseChartManager = new AsyncChartManager(camelContext, base, Period.hours(1));
        baseChartManager.start();
        
        ReductionStrategy strategy = new ReductionStrategy(baseChartManager.getChart());
        StrategyManager strategyManager = new StrategyManager(baseChartManager, strategy);
        
        StrategyTrader strategyTrader = new StrategyTrader(portfolioManager, futChartManager, strategyManager, adapterService, Decimal.valueOf(0.01), Decimal.valueOf(14));
    }
    
    private void onConnected() {
        Security fut = new Security(null, null, "FUT", "SRH7");
        Security base = new Security(null, null, "TQBR", "SBER");
        try{
            adapterService.getHistory(base, Period.seconds(3600), 100);
            adapterService.getHistory(fut, Period.seconds(3600), 10);
            adapterService.subscribe(base);
            adapterService.subscribe(fut);
        } catch(Exception e) {
            log.error("Error: ", e);
        }
    }
    
    class ConnectionTask extends TimerTask {
        private final Trader trader;

        public ConnectionTask(Trader trader) {
            this.trader = trader;
        }

        @Override
        public void run() {
            try {
                trader.connect();
            } catch (Exception ex) {
                log.error("ConnectionTask error", ex);
                throw new RuntimeException(ex);
            }
        }
        
        
    }
}
