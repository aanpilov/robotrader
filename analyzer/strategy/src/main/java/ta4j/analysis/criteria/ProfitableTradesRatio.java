/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ta4j.analysis.criteria;

import eu.verdelhan.ta4j.Trade;
import eu.verdelhan.ta4j.TradingRecord;

/**
 *
 * @author aav
 */
public class ProfitableTradesRatio {
    public double calculate(TradingRecord tradingRecord) throws Exception {
        int profitableCounter = 0;
        for (Trade trade : tradingRecord.getTrades()) {
            if(isProfitableTrade(trade)) {
                profitableCounter++;
            }
        }
        
        return new Double(profitableCounter).doubleValue()/tradingRecord.getTradeCount();
    }
    
    private boolean isProfitableTrade(Trade trade) {
        if(trade.getEntry().isBuy()) {
            return trade.getEntry().getPrice().isLessThan(trade.getExit().getPrice());
        } else {
            return trade.getEntry().getPrice().isGreaterThan(trade.getExit().getPrice());
        }
    }
}
