/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ta4j.strategy;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Rule;
import eu.verdelhan.ta4j.Strategy;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.oscillators.StochasticOscillatorKIndicator;
import eu.verdelhan.ta4j.indicators.trackers.SMAIndicator;
import eu.verdelhan.ta4j.trading.rules.CrossedDownIndicatorRule;
import eu.verdelhan.ta4j.trading.rules.CrossedUpIndicatorRule;
import eu.verdelhan.ta4j.trading.rules.InPipeRule;
import eu.verdelhan.ta4j.trading.rules.OverIndicatorRule;
import org.joda.time.Period;
import ta4j.ScaledTimeSeries;
import ta4j.rule.ScaledRule;

/**
 *
 * @author aanpilov
 */
public class ExampleStrategy {    
    
    public static Strategy build(TimeSeries timeSeries) {
        ScaledTimeSeries parentTimeSeries = new ScaledTimeSeries("parent", timeSeries, Period.days(1));
        
        StochasticOscillatorKIndicator parentStochIndicator = new StochasticOscillatorKIndicator(parentTimeSeries, 5);        
        SMAIndicator parentKIndicator = new SMAIndicator(parentStochIndicator, 3);
        SMAIndicator parentDIndicator = new SMAIndicator(parentKIndicator, 3);
        
        Rule parentEntryRule = new InPipeRule(parentStochIndicator, Decimal.valueOf(80), Decimal.valueOf(20))
                                .and(new OverIndicatorRule(parentKIndicator, parentDIndicator))
                               .or(new OverIndicatorRule(parentKIndicator, Decimal.valueOf(80)));
        ScaledRule parentEntryRuleWrapper = new ScaledRule(parentEntryRule, parentTimeSeries);
        
        StochasticOscillatorKIndicator stochIndicator = new StochasticOscillatorKIndicator(timeSeries, 8);
        SMAIndicator kIndicator = new SMAIndicator(stochIndicator, 5);
        SMAIndicator dIndicator = new SMAIndicator(kIndicator, 3);
        
        Rule entryRule = parentEntryRuleWrapper.and(new CrossedUpIndicatorRule(kIndicator, dIndicator)
                        .and(new InPipeRule(kIndicator, Decimal.valueOf(80), Decimal.valueOf(20)))
                        .or(new CrossedUpIndicatorRule(kIndicator, Decimal.valueOf(20))));
//        Rule entryRule = new CrossedUpIndicatorRule(kIndicator, dIndicator)
//                        .and(new InPipeRule(kIndicator, Decimal.valueOf(80), Decimal.valueOf(20)))
//                        .or(new CrossedUpIndicatorRule(kIndicator, Decimal.valueOf(20)));
        Rule exitRule = new CrossedDownIndicatorRule(kIndicator, dIndicator)
                        .and(new InPipeRule(kIndicator, Decimal.valueOf(80), Decimal.valueOf(20)))
                        .or(new CrossedDownIndicatorRule(kIndicator, Decimal.valueOf(80)));
        
        Strategy strategy = new Strategy(entryRule, exitRule);
        strategy.setUnstablePeriod(100);        
        
        return strategy;
    }
}
