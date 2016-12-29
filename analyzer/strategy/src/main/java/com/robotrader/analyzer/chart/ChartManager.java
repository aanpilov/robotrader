/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.chart;

import com.robotrader.core.objects.Security;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import java.util.Optional;
import org.joda.time.DateTime;
import org.joda.time.Period;

/**
 *
 * @author aanpilov
 */
public interface ChartManager extends Runnable {
    public Security getSecurity();
    public Period getPeriod();
    public TimeSeries getChart();
    public Optional<Tick> getLastTick();
    public Optional<Tick> getTickStartedAt(DateTime time);
    public void addChartListener(ChartListener listener);
    public void start();
}
