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
import eu.verdelhan.ta4j.trading.rules.CrossedDownIndicatorRule;
import eu.verdelhan.ta4j.trading.rules.CrossedUpIndicatorRule;
import eu.verdelhan.ta4j.trading.rules.InPipeRule;
import eu.verdelhan.ta4j.trading.rules.OverIndicatorRule;
import eu.verdelhan.ta4j.trading.rules.UnderIndicatorRule;

/**
 *
 * @author aanpilov
 */
public class InvestmentStrategy implements Strategy {
    private final TimeSeries timeSeries;
    private int unstablePeriod;
    private Rule enterLongRule;
    private Rule exitLongRule;
    private Rule trendUpRule;
    private Rule trendDownRule;

    public InvestmentStrategy() {
        timeSeries = new TimeSeries("strategy");
        initRules();
    }

    public InvestmentStrategy(TimeSeries timeSeries) {
        this.timeSeries = timeSeries;
        initRules();
    }
    
    private void initRules() {
        unstablePeriod = 8;
        
        StochasticOscillatorKIndicator stochIndicator = new StochasticOscillatorKIndicator(timeSeries, 5);
        SMAIndicator kIndicator = new SMAIndicator(stochIndicator, 3);
        SMAIndicator dIndicator = new SMAIndicator(kIndicator, 3);
        
        enterLongRule = new UnderIndicatorRule(kIndicator, Decimal.valueOf(20))
                        .and(new UnderIndicatorRule(dIndicator, Decimal.valueOf(20)))
                        .or(new InPipeRule(kIndicator, Decimal.valueOf(80), Decimal.valueOf(20))
                            .and(new CrossedUpIndicatorRule(kIndicator, dIndicator)));
        
        exitLongRule = new OverIndicatorRule(kIndicator, Decimal.valueOf(80))
                       .and(new OverIndicatorRule(dIndicator, Decimal.valueOf(80)))
                       .or(new InPipeRule(kIndicator, Decimal.valueOf(80), Decimal.valueOf(20))
                            .and(new CrossedDownIndicatorRule(kIndicator, dIndicator)));
        
        trendUpRule = new InPipeRule(kIndicator, Decimal.valueOf(80), Decimal.valueOf(20))
                      .and(new OverIndicatorRule(kIndicator, dIndicator));
        
        trendDownRule = new InPipeRule(kIndicator, Decimal.valueOf(80), Decimal.valueOf(20))
                      .and(new UnderIndicatorRule(kIndicator, dIndicator));
    }

    @Override
    public TimeSeries getTimeSeries() {
        return timeSeries;
    }

    @Override
    public Advice getAdvice(int index) {
        if(shouldEnterLong(index)) {
            return Advice.ENTER_LONG;
        }
        
        if(shouldExitLong(index)) {
            return Advice.EXIT_LONG;
        }
        
        if(isTrendUp(index)) {
            return Advice.TREND_UP;
        }
        
        if(isTrendDownRule(index)) {
            return Advice.TRENF_DOWN;
        }
        
        return Advice.NOTHING;
    }

    protected boolean isUnstableAt(int index) {
        return index < unstablePeriod;
    }
    
    protected boolean shouldEnterLong(int index) {
        if (isUnstableAt(index)) {
            return false;
        }
        return enterLongRule.isSatisfied(index);
    }
    
    protected boolean shouldExitLong(int index) {
        if (isUnstableAt(index)) {
            return false;
        }
        return exitLongRule.isSatisfied(index);
    }
    
    protected boolean isTrendUp(int index) {
        if (isUnstableAt(index)) {
            return false;
        }
        return trendUpRule.isSatisfied(index);
    }
    
    protected boolean isTrendDownRule(int index) {
        if (isUnstableAt(index)) {
            return false;
        }
        return trendDownRule.isSatisfied(index);
    }
}
