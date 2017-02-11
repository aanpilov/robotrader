/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ta4j.analysis.criteria;

import eu.verdelhan.ta4j.Trade;
import eu.verdelhan.ta4j.TradingRecord;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aanpilov
 */
public class MaximumLossSeries {
    public List<Trade> calculate(TradingRecord tradingRecord) throws Exception {
        List<Trade> lossSeries = new ArrayList();
        List<Trade> currentSeries = new ArrayList();
        int max = 0;
        int current = 0;
        for (Trade trade : tradingRecord.getTrades()) {
            if(!isProfitableTrade(trade)) {
                current++;
                currentSeries.add(trade);
            } else {
                if(current > max) {
                    max = current;
                    lossSeries.clear();
                    lossSeries.addAll(currentSeries);
                    currentSeries.clear();
                }
                current = 0;
            }
        }
        
        return lossSeries;
//        return max;
    }

    private boolean isProfitableTrade(Trade trade) {
        if(trade.getEntry().isBuy()) {
            return trade.getEntry().getPrice().isLessThan(trade.getExit().getPrice());
        } else {
            return trade.getEntry().getPrice().isGreaterThan(trade.getExit().getPrice());
        }
    }
}
