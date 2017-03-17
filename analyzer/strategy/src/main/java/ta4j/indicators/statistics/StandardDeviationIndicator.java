/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ta4j.indicators.statistics;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Indicator;
import eu.verdelhan.ta4j.indicators.CachedIndicator;
import eu.verdelhan.ta4j.indicators.trackers.SMAIndicator;

/**
 *
 * @author aanpilov
 */
public class StandardDeviationIndicator extends CachedIndicator<Decimal> {
    private VarianceIndicator variance;

    public StandardDeviationIndicator(Indicator<Decimal> baseIndicator, int timeFrame) {
        super(baseIndicator);
        this.variance = new VarianceIndicator(baseIndicator, new SMAIndicator(baseIndicator, timeFrame), timeFrame);
    }

    @Override
    protected Decimal calculate(int index) {
        return variance.getValue(index).sqrt();
    }
    
}
