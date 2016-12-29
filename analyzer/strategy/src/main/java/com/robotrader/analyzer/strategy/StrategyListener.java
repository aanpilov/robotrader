/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.strategy;

import eu.verdelhan.ta4j.Tick;
import ta4j.strategy.Advice;

/**
 *
 * @author aanpilov
 */
public interface StrategyListener {
    public void onAdvice(Tick tick, Advice advice);
}
