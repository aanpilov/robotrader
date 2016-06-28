/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.strategy.strategy;

import com.robotrader.analyzer.strategy.Advice;
import com.robotrader.core.factor.Paper;
import com.robotrader.core.interval.Interval;

/**
 *
 * @author aav
 */
public class StrategySpecification {
    private Paper paper;
    private Interval interval;
    private Advice expected;

    public StrategySpecification(Paper paper, Interval interval, Advice expected) {
        this.paper = paper;
        this.interval = interval;
        this.expected = expected;
    }
    
    public boolean isSatisfied(Strategy strategy) {
        Advice advice = strategy.getAdvice();
        if (advice == expected) {
            return true;
        }
        
        if((expected == Advice.advAny) && (advice != Advice.advNothing)) {
            return true;
        }
        
        if((advice == Advice.advAny) && (expected != Advice.advNothing)) {
            return true;
        }
        
        return false;
    }

    public Paper getPaper() {
        return paper;
    }

    public Interval getInterval() {
        return interval;
    }

    public Advice getExpected() {
        return expected;
    }
}
