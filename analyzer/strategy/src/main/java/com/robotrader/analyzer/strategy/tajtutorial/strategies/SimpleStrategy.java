/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.strategy.tajtutorial.strategies;

import com.robotrader.analyzer.strategy.tajtutorial.ScaledTimeSeries;
import com.robotrader.analyzer.strategy.tajtutorial.loader.FinamCsvTicksLoader;
import com.robotrader.analyzer.strategy.tajtutorial.rule.ScaledRule;
import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Order;
import eu.verdelhan.ta4j.Rule;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.Trade;
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
import eu.verdelhan.ta4j.indicators.oscillators.StochasticOscillatorKIndicator;
import eu.verdelhan.ta4j.indicators.trackers.SMAIndicator;
import eu.verdelhan.ta4j.trading.rules.InPipeRule;
import eu.verdelhan.ta4j.trading.rules.OverIndicatorRule;
import eu.verdelhan.ta4j.trading.rules.UnderIndicatorRule;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.Period;

/**
 *
 * @author aanpilov
 */
public class SimpleStrategy {
    private final TimeSeries timeSeries;
    private final ScaledTimeSeries parentTimeSeries;
    private int unstablePeriod;
    private Rule enterLongRule;
    private Rule enterShortRule;
    private Rule exitLongRule;    
    private Rule exitShortRule;    

    public SimpleStrategy(TimeSeries timeSeries) {
        this.timeSeries = timeSeries;
        parentTimeSeries = new ScaledTimeSeries("parent", timeSeries, Period.days(1));
        initRules();
    }

    private void initRules() {
        unstablePeriod = 63;
        
        StochasticOscillatorKIndicator parentStochIndicator = new StochasticOscillatorKIndicator(parentTimeSeries, 5);        
        SMAIndicator parentKIndicator = new SMAIndicator(parentStochIndicator, 3);
        SMAIndicator parentDIndicator = new SMAIndicator(parentKIndicator, 3);
        
        Rule parentLongEnterRule = new InPipeRule(parentKIndicator, Decimal.valueOf(80), Decimal.valueOf(20))
                                .and(new OverIndicatorRule(parentKIndicator, parentDIndicator))
                               .or(new OverIndicatorRule(parentKIndicator, Decimal.valueOf(80)));
        ScaledRule parentLongEnterRuleWrapper = new ScaledRule(parentLongEnterRule, parentTimeSeries);
        
        Rule parentShortEnterRule = new InPipeRule(parentKIndicator, Decimal.valueOf(80), Decimal.valueOf(20))
                                .and(new UnderIndicatorRule(parentKIndicator, parentDIndicator))
                               .or(new UnderIndicatorRule(parentKIndicator, Decimal.valueOf(20)));
        ScaledRule parentShortEnterRuleWrapper = new ScaledRule(parentShortEnterRule, parentTimeSeries);
        
        StochasticOscillatorKIndicator stochIndicator = new StochasticOscillatorKIndicator(timeSeries, 8);
        SMAIndicator kIndicator = new SMAIndicator(stochIndicator, 5);
        SMAIndicator dIndicator = new SMAIndicator(kIndicator, 3);
        
        enterLongRule = parentLongEnterRuleWrapper.and(new OverIndicatorRule(kIndicator, dIndicator)
                        .and(new InPipeRule(kIndicator, Decimal.valueOf(80), Decimal.valueOf(20))));
        
        exitLongRule = new UnderIndicatorRule(kIndicator, dIndicator).and(new UnderIndicatorRule(kIndicator, Decimal.valueOf(80)));
        
        enterShortRule = parentShortEnterRuleWrapper.and(new UnderIndicatorRule(kIndicator, dIndicator)
                        .and(new InPipeRule(kIndicator, Decimal.valueOf(80), Decimal.valueOf(20))));
        
        exitShortRule = new OverIndicatorRule(kIndicator, dIndicator).and(new OverIndicatorRule(kIndicator, Decimal.valueOf(20)));
    }
    
    public boolean shouldEnterLong(int index) {
        if (isUnstableAt(index)) {
            return false;
        }
        return enterLongRule.isSatisfied(index);
    }
    
    public boolean shouldEnterShort(int index) {
        if (isUnstableAt(index)) {
            return false;
        }
        return enterShortRule.isSatisfied(index);
    }
    
    public boolean shouldExitLong(int index) {
        if (isUnstableAt(index)) {
            return false;
        }
        return exitLongRule.isSatisfied(index);
    }
    
    public boolean shouldExitShort(int index) {
        if (isUnstableAt(index)) {
            return false;
        }
        return exitShortRule.isSatisfied(index);
    }
    
    public boolean isUnstableAt(int index) {
        return index < unstablePeriod;
    }
    
    public static void main(String[] args) throws Exception {
        TimeSeries finamSeries = FinamCsvTicksLoader.loadSeries(new File("src/test/resources/SBER_H.csv"));
        TimeSeries series = new TimeSeries("test", finamSeries.getTimePeriod());
        
        SimpleStrategy strategy = new SimpleStrategy(series);
        TradingRecord longTradingRecord = new TradingRecord(Order.OrderType.BUY);
        TradingRecord shortTradingRecord = new TradingRecord(Order.OrderType.SELL);
        
        int positionSize = 0;
        
        for (int i = 0; i < finamSeries.getTickCount(); i++) {
            Tick newTick = finamSeries.getTick(i);
            
            series.addTick(newTick);

            int endIndex = series.getEnd();
            if(strategy.shouldExitLong(endIndex) && positionSize > 0) {
                positionSize--;
                System.out.println("Strategy should EXIT_LONG on " + newTick.getEndTime());
                longTradingRecord.exit(endIndex, newTick.getClosePrice(), Decimal.valueOf(1));
            }
            if(strategy.shouldExitShort(endIndex) && positionSize < 0) {
                positionSize++;
                System.out.println("Strategy should EXIT_SHORT on " + newTick.getEndTime());
                shortTradingRecord.exit(endIndex, newTick.getClosePrice(), Decimal.valueOf(1));
            }
            if (strategy.shouldEnterLong(endIndex) && positionSize <= 0) {
                positionSize++;                
                System.out.println("Strategy should ENTER_LONG on " + newTick.getEndTime());
                longTradingRecord.enter(endIndex, newTick.getClosePrice(), Decimal.valueOf(1));
            } 
            if (strategy.shouldEnterShort(endIndex) && positionSize >= 0) {
                positionSize--;                
                System.out.println("Strategy should ENTER_SHORT on " + newTick.getEndTime());
                shortTradingRecord.enter(endIndex, newTick.getClosePrice(), Decimal.valueOf(1));
            }
        }
        
        List<Order> orders = new ArrayList<>();
        for(Trade trade : longTradingRecord.getTrades()) {
            orders.add(trade.getEntry());
            orders.add(trade.getExit());
        }
        
        for(Trade trade : shortTradingRecord.getTrades()) {
            orders.add(trade.getEntry());
            orders.add(trade.getExit());
        }
        Order[] arrayOrders = new Order[1];
        arrayOrders = orders.toArray(arrayOrders);
        TradingRecord tradingRecord = new TradingRecord(arrayOrders);
        
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
