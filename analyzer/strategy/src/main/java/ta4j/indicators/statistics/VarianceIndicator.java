/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ta4j.indicators.statistics;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Indicator;
import eu.verdelhan.ta4j.indicators.CachedIndicator;

/**
 *
 * @author aanpilov
 */
public class VarianceIndicator extends CachedIndicator<Decimal> {
    private Indicator<Decimal> baseIndicator;
    private Indicator<Decimal> indicator;
    private int timeFrame;

    public VarianceIndicator(Indicator<Decimal> baseIndicator, Indicator<Decimal> indicator, int timeFrame) {
        super(baseIndicator);
        this.baseIndicator = baseIndicator;
        this.indicator = indicator;
        this.timeFrame = timeFrame;
    }
    
    @Override
    protected Decimal calculate(int index) {
        final int startIndex = Math.max(0, index - timeFrame + 1);
        final int numberOfObservations = index - startIndex + 1;
        Decimal variance = Decimal.ZERO;
        Decimal average = baseIndicator.getValue(index);
        for (int i = startIndex; i <= index; i++) {
            Decimal pow = indicator.getValue(i).minus(average).pow(2);
            variance = variance.plus(pow);
        }
        variance = variance.dividedBy(Decimal.valueOf(numberOfObservations));
        return variance;
    }
    
}
