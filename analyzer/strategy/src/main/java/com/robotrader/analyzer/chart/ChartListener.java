/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.chart;

import eu.verdelhan.ta4j.Tick;

/**
 *
 * @author aanpilov
 */
public interface ChartListener {
    public void archiveTickAdded(Tick tick);
    public void onlineTickAdded(Tick tick);
}
