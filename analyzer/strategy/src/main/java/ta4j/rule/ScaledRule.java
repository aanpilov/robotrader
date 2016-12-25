/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ta4j.rule;

import ta4j.ScaledTimeSeries;
import eu.verdelhan.ta4j.Rule;
import eu.verdelhan.ta4j.TradingRecord;
import eu.verdelhan.ta4j.trading.rules.AbstractRule;

/**
 *
 * @author aanpilov
 */
public class ScaledRule extends AbstractRule {
    private final ScaledTimeSeries timeSeries;
    private final Rule baseRule;

    public ScaledRule(Rule baseRule, ScaledTimeSeries timeSeries) {
        this.timeSeries = timeSeries;
        this.baseRule = baseRule;
    }

    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        timeSeries.rebuild(index);
        return baseRule.isSatisfied(timeSeries.getEnd(), tradingRecord);
    }
}
