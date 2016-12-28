/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.strategy;

import com.robotrader.analyzer.chart.ChartManager;
import com.robotrader.analyzer.chart.FinamFileChartManager;
import com.robotrader.analyzer.trader.Portfolio;
import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Order;
import java.io.File;
import java.util.List;
import org.joda.time.Period;
import ta4j.analysis.criteria.ClearProfitCriterion;
import ta4j.strategy.ReductionStrategy;

/**
 *
 * @author aav
 */
public class StrategyManagerTest {
        public static void main(String[] args) throws Exception {
        Decimal initCapital = Decimal.valueOf(2200);
        
        Portfolio portfolio = new Portfolio(initCapital.dividedBy(Decimal.valueOf(22)), Decimal.NaN);
        ChartManager chartManager = new FinamFileChartManager(new File("src/test/resources/finam/SBER_H_2016.csv"), Period.hours(1));
        
//        Strategy strategy = new SimpleStrategy(chartManager.getChart());
        ReductionStrategy strategy = new ReductionStrategy(chartManager.getChart());
        
        StrategyManager strategyManager = new StrategyManager(chartManager, strategy, portfolio);
        
        strategyManager.start();
        
        Thread.sleep(15 * 1000);

        List<Order> orders = strategyManager.getOrders();
        if(strategyManager.getLiquidationOrder().isPresent()) {
            orders.add(strategyManager.getLiquidationOrder().get());
        }
        
        //Clear profit
        System.out.println("Clear profit: " + new ClearProfitCriterion().calculate(orders));
        System.out.println("Clear profit %: " + (new ClearProfitCriterion().calculate(orders)/initCapital.toDouble()) * 100);
    }
}
