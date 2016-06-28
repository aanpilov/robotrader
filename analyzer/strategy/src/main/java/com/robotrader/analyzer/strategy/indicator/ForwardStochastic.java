/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.strategy.indicator;

import com.robotrader.core.factor.BarStorage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 *
 * @author aav
 */
public class ForwardStochastic extends Stochastic {

    public ForwardStochastic(int kPeriod, int kSlowPeriod, int dSlowPeriod, BarStorage barStorage) {
        super(kPeriod, kSlowPeriod, dSlowPeriod, barStorage);
    }

    @Override
    protected Double calculateKValue(Date date) {
        Double result = null;

        ArrayList<Date> dateList = new ArrayList<>(closeValues.keySet());
        int currentPosition = dateList.indexOf(date);

        if (currentPosition + 1 >= kPeriod) {
            int startPosition = currentPosition - kPeriod + 2;

            Double lastCloseValue = closeValues.get(date);
//            Double minLowValue = (Double) Collections.min(new ArrayList(lowValues.values()).subList(startPosition, currentPosition - 1));
//            Double maxHighValue = (Double) Collections.max(new ArrayList(highValues.values()).subList(startPosition, currentPosition - 1));
            Double minLowValue = lowValues.get(date);
            Double maxHighValue = highValues.get(date);
            
            for (int i = startPosition; i < currentPosition; i++) {
                Date currentDate = dateList.get(i);
                
                Double lowValue = lowValues.get(currentDate);
                if(minLowValue.compareTo(lowValue) == 1)minLowValue = lowValue;
                
                Double highValue = highValues.get(currentDate);
                if(maxHighValue.compareTo(highValue) == -1) maxHighValue = highValue;
            }

            result = ((lastCloseValue - minLowValue) / (maxHighValue - minLowValue)) * 100;
        }

        return result;
    }

}
