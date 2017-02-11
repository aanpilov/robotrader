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
import eu.verdelhan.ta4j.analysis.criteria.AbstractAnalysisCriterion;

/**
 *
 * @author aav
 */
public class TotalProfitCriterion extends AbstractAnalysisCriterion {

    @Override
    public double calculate(TimeSeries series, TradingRecord tradingRecord) {
        double value = 1d;
        for (Trade trade : tradingRecord.getTrades()) {
            value *= calculateProfit(series, trade);
        }
        return value;
    }

    @Override
    public double calculate(TimeSeries series, Trade trade) {
        return calculateProfit(series, trade);
    }

    @Override
    public boolean betterThan(double criterionValue1, double criterionValue2) {
        return criterionValue1 > criterionValue2;
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
        return profit.toDouble();
    }
}

