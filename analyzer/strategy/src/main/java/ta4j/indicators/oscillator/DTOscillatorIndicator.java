/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ta4j.indicators.oscillator;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Indicator;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.CachedIndicator;
import eu.verdelhan.ta4j.indicators.helpers.HighestValueIndicator;
import eu.verdelhan.ta4j.indicators.helpers.LowestValueIndicator;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.trackers.RSIIndicator;

/**
 *
 * @author aav
 */
public class DTOscillatorIndicator extends CachedIndicator<Decimal> {
    private final Indicator<Decimal> indicator;
    private final int timeFrame;

    public DTOscillatorIndicator(TimeSeries series, int timeFrame) {
        super(series);
        this.timeFrame = timeFrame;
        indicator = new RSIIndicator(new ClosePriceIndicator(series), timeFrame);
    }
    

    @Override
    protected Decimal calculate(int index) {
        HighestValueIndicator highestHigh = new HighestValueIndicator(indicator, 5);
        LowestValueIndicator lowestMin = new LowestValueIndicator(indicator, 5);

        Decimal highestHighPrice = highestHigh.getValue(index);
        Decimal lowestLowPrice = lowestMin.getValue(index);

        return indicator.getValue(index).minus(lowestLowPrice)
                .dividedBy(highestHighPrice.minus(lowestLowPrice))
                .multipliedBy(Decimal.HUNDRED);
    }
    
}
