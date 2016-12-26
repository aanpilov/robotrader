/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ta4j.strategy;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Rule;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.oscillators.StochasticOscillatorKIndicator;
import eu.verdelhan.ta4j.indicators.trackers.SMAIndicator;
import eu.verdelhan.ta4j.trading.rules.OverIndicatorRule;
import eu.verdelhan.ta4j.trading.rules.UnderIndicatorRule;

/**
 *
 * @author aanpilov
 */
public class ReductionStrategy extends SimpleStrategy {
    private Rule reduceLongRule;
    private Rule reduceShortRule;

    public ReductionStrategy(TimeSeries timeSeries) {
        super(timeSeries);
    }

    @Override
    protected void initRules() {
        super.initRules();
        
        StochasticOscillatorKIndicator stochIndicator = new StochasticOscillatorKIndicator(getTimeSeries(), 8);
        SMAIndicator kIndicator = new SMAIndicator(stochIndicator, 5);
        SMAIndicator dIndicator = new SMAIndicator(kIndicator, 3);
        
        reduceLongRule = new OverIndicatorRule(kIndicator, Decimal.valueOf(80)).and(new OverIndicatorRule(dIndicator, Decimal.valueOf(80)));
        
        reduceShortRule = new UnderIndicatorRule(kIndicator, Decimal.valueOf(20)).and(new UnderIndicatorRule(dIndicator, Decimal.valueOf(20)));
    }
    
    protected boolean shouldReduceLong(int index) {
        if (isUnstableAt(index)) {
            return false;
        }
        return reduceLongRule.isSatisfied(index);
    }
    
    protected boolean shouldReduceShort(int index) {
        if (isUnstableAt(index)) {
            return false;
        }
        return reduceShortRule.isSatisfied(index);
    }

    @Override
    public Advice getAdvice(int index) {
        Advice advice = super.getAdvice(index);
        
        if(shouldReduceLong(index)) {
            return Advice.REDUCE_LONG;
        }
        
        if(shouldReduceShort(index)) {
            return Advice.REDUCE_SHORT;
        }
        
        return advice;
    }
}
