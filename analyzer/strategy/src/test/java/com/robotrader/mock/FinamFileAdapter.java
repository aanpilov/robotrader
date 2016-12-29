/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.mock;

import com.robotrader.analyzer.io.FinamCsvTicksLoader;
import com.robotrader.analyzer.strategy.StrategyManager;
import com.robotrader.analyzer.trader.Portfolio;
import com.robotrader.analyzer.trader.StrategyTrader;
import com.robotrader.core.objects.ConditionalOrder;
import com.robotrader.core.objects.Security;
import com.robotrader.core.service.AsyncAdapterService;
import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ta4j.strategy.ReductionStrategy;

/**
 *
 * @author aanpilov
 */
public class FinamFileAdapter implements AsyncAdapterService {
    private final Set<ConditionalOrder> orders = new HashSet<>();
    private final Set<ChartManager> chartManagers = new HashSet<>();
    
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final File file;

    public FinamFileAdapter(File file) {
        this.file = file;
    }
    
    @Override
    public void start() throws Exception {
        log.info("Started");
    }

    @Override
    public void stop() throws Exception {
        log.info("Stopped");
    }

    @Override
    public void getHistory(Security security, Period period, int count) throws Exception {
        log.info("Get history is disabled");
    }

    @Override
    public void subscribe(Security security) throws Exception {
        TimeSeries series = FinamCsvTicksLoader.loadSeries(file);
        for (int i = 0; i < series.getEnd(); i++) {
            Tick tick = series.getTick(i);
            matchOrders(tick);
            chartManagers.forEach(manager -> manager.addOnlineTick(tick));
        }
    }

    @Override
    public void createConditionalOrder(ConditionalOrder order) throws Exception {
        orders.add(order);
    }
    
    public void addChartManager(ChartManager chartManager) {
        chartManagers.add(chartManager);
    }

    private void matchOrders(Tick tick) {
        for (ConditionalOrder order : orders) {
            if(tick.getMinPrice().toDouble() <= order.getConditionValue().doubleValue()
               && order.getConditionValue().doubleValue() <= tick.getMaxPrice().toDouble()) {
                matchOrder(order);
                orders.remove(order);
            }
        }
    }

    private void matchOrder(ConditionalOrder order) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static void main(String[] args) throws Exception {
        //Init objects
        Security security = new Security(null, null, null, "SBER");
        FinamFileAdapter adapter = new FinamFileAdapter(new File("src/test/resources/finam/SBER_H_2016.csv"));
        
        ChartManager chartManager = new ChartManager(security, Period.hours(1));
        adapter.addChartManager(chartManager);
        
        Decimal initCapital = Decimal.valueOf(22000);        
        Portfolio portfolio = new Portfolio();
        
        ReductionStrategy strategy = new ReductionStrategy(chartManager.getChart());
        StrategyManager strategyManager = new StrategyManager(chartManager, strategy);
        StrategyTrader strategyTrader = new StrategyTrader(portfolio, chartManager, strategyManager, adapter, initCapital.dividedBy(Decimal.valueOf(22)), Decimal.NaN);
        
        //Start
        strategyTrader.start();
        
        //Result
        System.out.println("Tick count: " + chartManager.getChart().getTickCount());        
    }
    
}
