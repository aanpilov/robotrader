/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ta4j.indicators.helpers;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Indicator;
import eu.verdelhan.ta4j.indicators.CachedIndicator;

/**
 *
 * @author aanpilov
 */
public class CrossIndicator extends CachedIndicator<Boolean> {
    private final Indicator<Decimal> first;
    private final Indicator<Decimal> second;

    public CrossIndicator(Indicator<Decimal> first, Indicator<Decimal> second) {
        super(first);
        this.first = first;
        this.second = second;
    }

    @Override
    protected Boolean calculate(int index) {
        if(index == 0) {
            return Boolean.FALSE;
        }
        
        Decimal currentDiff = first.getValue(index).minus(second.getValue(index));
        Decimal prevDiff = first.getValue(index - 1).minus(second.getValue(index - 1));
        
        if(currentDiff.isZero()) {
            return Boolean.TRUE;
        }
        
        return currentDiff.multipliedBy(prevDiff).isNegative();
    }
    
}
