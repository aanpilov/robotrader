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
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.trackers.EMAIndicator;
import eu.verdelhan.ta4j.indicators.trackers.SMAIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.BollingerBandWidthIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.BollingerBandsLowerIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.BollingerBandsMiddleIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.BollingerBandsUpperIndicator;
import eu.verdelhan.ta4j.trading.rules.InPipeRule;
import eu.verdelhan.ta4j.trading.rules.OverIndicatorRule;
import eu.verdelhan.ta4j.trading.rules.UnderIndicatorRule;
import org.joda.time.Period;
import ta4j.ScaledTimeSeries;
import ta4j.indicators.statistics.StandardDeviationIndicator;
import ta4j.rule.ScaledRule;

/**
 *
 * @author aanpilov
 */
public class SimpleStrategy implements Strategy {
    private final TimeSeries timeSeries;
    private final ScaledTimeSeries parentTimeSeries;
    private int unstablePeriod;
    private Rule enterLongRule;
    private Rule enterShortRule;
    private Rule exitLongRule;    
    private Rule exitShortRule;    
    
    public SimpleStrategy(TimeSeries timeSeries) {
        this.timeSeries = timeSeries;
        parentTimeSeries = new ScaledTimeSeries("parent", timeSeries, Period.days(1));
        initRules();
    }

    protected void initRules() {
        unstablePeriod = 72;
        
        StochasticOscillatorKIndicator parentStochIndicator = new StochasticOscillatorKIndicator(parentTimeSeries, 5);        
        SMAIndicator parentKIndicator = new SMAIndicator(parentStochIndicator, 3);
        SMAIndicator parentDIndicator = new SMAIndicator(parentKIndicator, 3);
        
        Rule parentLongEnterRule = new InPipeRule(parentKIndicator, Decimal.valueOf(80), Decimal.valueOf(20))
                                .and(new OverIndicatorRule(parentKIndicator, parentDIndicator))
                               .or(new OverIndicatorRule(parentKIndicator, Decimal.valueOf(80)));
        ScaledRule parentLongEnterRuleWrapper = new ScaledRule(parentLongEnterRule, parentTimeSeries);
        
        Rule parentShortEnterRule = new InPipeRule(parentKIndicator, Decimal.valueOf(80), Decimal.valueOf(20))
                                .and(new UnderIndicatorRule(parentKIndicator, parentDIndicator))
                               .or(new UnderIndicatorRule(parentKIndicator, Decimal.valueOf(20)));
        ScaledRule parentShortEnterRuleWrapper = new ScaledRule(parentShortEnterRule, parentTimeSeries);
        
        StochasticOscillatorKIndicator stochIndicator = new StochasticOscillatorKIndicator(timeSeries, 8);
        SMAIndicator kIndicator = new SMAIndicator(stochIndicator, 5);
        SMAIndicator dIndicator = new SMAIndicator(kIndicator, 3);
        
        ClosePriceIndicator closePriceIndicator = new ClosePriceIndicator(timeSeries);        
        EMAIndicator emaIndicator = new EMAIndicator(closePriceIndicator, 13);
        StandardDeviationIndicator standardDeviationIndicator = new StandardDeviationIndicator(emaIndicator, closePriceIndicator, 13);
        
        BollingerBandsMiddleIndicator bbm = new BollingerBandsMiddleIndicator(emaIndicator);
        BollingerBandsLowerIndicator bbl = new BollingerBandsLowerIndicator(bbm, standardDeviationIndicator);
        BollingerBandsUpperIndicator bbu = new BollingerBandsUpperIndicator(bbm, standardDeviationIndicator);
        BollingerBandWidthIndicator widthIndicator = new BollingerBandWidthIndicator(bbu, bbm, bbl);
        
        enterLongRule = parentLongEnterRuleWrapper.and(new OverIndicatorRule(kIndicator, dIndicator)
                        .and(new InPipeRule(kIndicator, Decimal.valueOf(80), Decimal.valueOf(20))))
                        .and(new OverIndicatorRule(widthIndicator, Decimal.valueOf(1)));
        
        exitLongRule = new UnderIndicatorRule(kIndicator, dIndicator).and(new UnderIndicatorRule(kIndicator, Decimal.valueOf(80)));
        
        enterShortRule = parentShortEnterRuleWrapper.and(new UnderIndicatorRule(kIndicator, dIndicator)
                        .and(new InPipeRule(kIndicator, Decimal.valueOf(80), Decimal.valueOf(20))))
                        .and(new OverIndicatorRule(widthIndicator, Decimal.valueOf(1)));
        
        exitShortRule = new OverIndicatorRule(kIndicator, dIndicator).and(new OverIndicatorRule(kIndicator, Decimal.valueOf(20)));
    }
    
    protected boolean shouldEnterLong(int index) {
        if (isUnstableAt(index)) {
            return false;
        }
        return enterLongRule.isSatisfied(index);
    }
    
    protected boolean shouldEnterShort(int index) {
        if (isUnstableAt(index)) {
            return false;
        }
        return enterShortRule.isSatisfied(index);
    }
    
    protected boolean shouldExitLong(int index) {
        if (isUnstableAt(index)) {
            return false;
        }
        return exitLongRule.isSatisfied(index);
    }
    
    protected boolean shouldExitShort(int index) {
        if (isUnstableAt(index)) {
            return false;
        }
        return exitShortRule.isSatisfied(index);
    }
    
    protected boolean isUnstableAt(int index) {
        return index < unstablePeriod;
    }
    
    @Override
    public Advice getAdvice(int index) {
        if(shouldEnterLong(index)) {
            return Advice.ENTER_LONG;
        } 
        
        if(shouldEnterShort(index)) {
            return Advice.ENTER_SHORT;
        } 
        
        if(shouldExitLong(index)) {
            return Advice.EXIT_LONG;
        } 
        
        if(shouldExitShort(index)) {
            return Advice.EXIT_SHORT;
        }
        
        return Advice.NOTHING;
    }

    @Override
    public TimeSeries getTimeSeries() {
        return timeSeries;
    }
}
