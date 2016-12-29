/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.mock;

import com.robotrader.analyzer.chart.AbstractChartManager;
import com.robotrader.core.objects.Security;
import eu.verdelhan.ta4j.Tick;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aanpilov
 */
public class MockChartManager extends AbstractChartManager {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public MockChartManager(Security security, Period period) {
        super(security, period);
    }

    public void addOnlineTick(Tick tick) {
        series.addTick(tick);
        
        listeners.forEach((listener) -> {listener.onlineTickAdded(tick);});
    }

    @Override
    public synchronized void start() {
        log.info("Started");
    }
    
    
}
