/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.strategy.strategy;

import com.robotrader.analyzer.strategy.Advice;
import com.robotrader.analyzer.strategy.indicator.Indicator;
import java.util.List;

/**
 *
 * @author aav
 */
public interface Strategy {    
    public Advice getAdvice();
}
