/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ta4j.analysis.criteria;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.Trade;
import eu.verdelhan.ta4j.TradingRecord;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aanpilov
 */
public class SharpCriterion {

    public double calculate(TimeSeries series, TradingRecord tradingRecord) {
        List<Double> profitList = new ArrayList<>();
        for (Trade trade : tradingRecord.getTrades()) {
            double profit = calculateProfit(series, trade);
            profitList.add(profit);
        }
        
        double avg = profitList.stream().mapToDouble(o -> o).average().getAsDouble();
        double sko = Math.sqrt(profitList.stream().mapToDouble(o -> Math.pow(o - avg, 2)).average().getAsDouble());
        
        double tp = new TotalProfitCriterion().calculate(series, tradingRecord);
        return (tp - 1)/sko;
    }
    
    /**
     * Calculates the profit of a trade (Buy and sell).
     * @param series a time series
     * @param trade a trade
     * @return the profit of the trade
     */
    private double calculateProfit(TimeSeries series, Trade trade) {
        Decimal profit = Decimal.ONE;
        if (trade.isClosed()) {
            Decimal exitPrice = (!trade.getExit().getPrice().isNaN())?trade.getExit().getPrice():series.getTick(trade.getExit().getIndex()).getClosePrice();
            Decimal entryPrice = (!trade.getEntry().getPrice().isNaN())?trade.getEntry().getPrice():series.getTick(trade.getEntry().getIndex()).getClosePrice();

            if (trade.getEntry().isBuy()) {
                profit = exitPrice.dividedBy(entryPrice);
            } else {
                profit = entryPrice.dividedBy(exitPrice);
            }
        }
        return profit.minus(Decimal.ONE).toDouble();
    }
}
