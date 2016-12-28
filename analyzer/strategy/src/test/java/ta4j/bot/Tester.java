/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ta4j.bot;

import eu.verdelhan.ta4j.Order;
import eu.verdelhan.ta4j.Strategy;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.TradingRecord;
import eu.verdelhan.ta4j.analysis.criteria.AverageProfitCriterion;
import eu.verdelhan.ta4j.analysis.criteria.AverageProfitableTradesCriterion;
import eu.verdelhan.ta4j.analysis.criteria.BuyAndHoldCriterion;
import eu.verdelhan.ta4j.analysis.criteria.LinearTransactionCostCriterion;
import eu.verdelhan.ta4j.analysis.criteria.MaximumDrawdownCriterion;
import eu.verdelhan.ta4j.analysis.criteria.NumberOfTicksCriterion;
import eu.verdelhan.ta4j.analysis.criteria.NumberOfTradesCriterion;
import eu.verdelhan.ta4j.analysis.criteria.RewardRiskRatioCriterion;
import eu.verdelhan.ta4j.analysis.criteria.TotalProfitCriterion;
import eu.verdelhan.ta4j.analysis.criteria.VersusBuyAndHoldCriterion;
import java.io.File;
import com.robotrader.analyzer.io.FinamCsvTicksLoader;
import ta4j.strategy.ExampleStrategy;

/**
 *
 * @author aanpilov
 */
public class Tester {

    public static void main(String[] args) throws Exception {
        TimeSeries series = FinamCsvTicksLoader.loadSeries(new File("src/test/resources/finam/SBER_H.csv")); 

        Strategy strategy = ExampleStrategy.build(series);

        TradingRecord tradingRecord = series.run(strategy, Order.OrderType.BUY);

        /**
         * Analysis criteria
         */
        // Total profit
        TotalProfitCriterion totalProfit = new TotalProfitCriterion();
        System.out.println("Total profit: " + totalProfit.calculate(series, tradingRecord));
        // Number of ticks
        System.out.println("Number of ticks: " + new NumberOfTicksCriterion().calculate(series, tradingRecord));
        // Average profit (per tick)
        System.out.println("Average profit (per tick): " + new AverageProfitCriterion().calculate(series, tradingRecord));
        // Number of trades
        System.out.println("Number of trades: " + new NumberOfTradesCriterion().calculate(series, tradingRecord));
        // Profitable trades ratio
        System.out.println("Profitable trades ratio: " + new AverageProfitableTradesCriterion().calculate(series, tradingRecord));
        // Maximum drawdown
        System.out.println("Maximum drawdown: " + new MaximumDrawdownCriterion().calculate(series, tradingRecord));
        // Reward-risk ratio
        System.out.println("Reward-risk ratio: " + new RewardRiskRatioCriterion().calculate(series, tradingRecord));
        // Total transaction cost
        System.out.println("Total transaction cost (from $1000): " + new LinearTransactionCostCriterion(1000, 0.005).calculate(series, tradingRecord));
        // Buy-and-hold
        System.out.println("Buy-and-hold: " + new BuyAndHoldCriterion().calculate(series, tradingRecord));
        // Total profit vs buy-and-hold
        System.out.println("Custom strategy profit vs buy-and-hold strategy profit: " + new VersusBuyAndHoldCriterion(totalProfit).calculate(series, tradingRecord));
    }
}
