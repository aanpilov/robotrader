/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ta4j.indicators.simple;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Indicator;
import eu.verdelhan.ta4j.indicators.CachedIndicator;

/**
 *
 * @author aanpilov
 */
public class DivisorIndicator extends CachedIndicator<Decimal> {
    private Indicator<Decimal> first;
    
    private Indicator<Decimal> second;

    public DivisorIndicator(Indicator<Decimal> first, Indicator<Decimal> second) {
        super(first);
        this.first = first;
        this.second = second;
    }

    @Override
    protected Decimal calculate(int index) {
        return first.getValue(index).dividedBy(second.getValue(index));
    }
    
}
