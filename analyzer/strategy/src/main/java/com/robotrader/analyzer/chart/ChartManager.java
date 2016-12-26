/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.chart;

import eu.verdelhan.ta4j.TimeSeries;

/**
 *
 * @author aanpilov
 */
public interface ChartManager extends Runnable {    
    public TimeSeries getChart();
    public void addChartListener(ChartListener listener);
    public void start();
}
