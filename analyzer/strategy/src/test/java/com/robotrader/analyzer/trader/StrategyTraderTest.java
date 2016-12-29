/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.trader;

import com.robotrader.analyzer.portfolio.Portfolio;
import com.robotrader.analyzer.strategy.StrategyManager;
import com.robotrader.core.objects.Security;
import com.robotrader.mock.FinamFileAdapter;
import com.robotrader.mock.MockChartManager;
import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Order;
import java.io.File;
import java.util.List;
import java.util.Optional;
import org.joda.time.Period;
import ta4j.analysis.criteria.ClearProfitCriterion;
import ta4j.strategy.ReductionStrategy;

/**
 *
 * @author aanpilov
 */
public class StrategyTraderTest {
        public static void main(String[] args) throws Exception {
        Decimal initCapital = Decimal.valueOf(22000);
        Security security = new Security(null, null, null, "SBER");
        
        FinamFileAdapter adapter = new FinamFileAdapter(new File("src/test/resources/finam/SBER_H_2016.csv"));
        MockChartManager chartManager = new MockChartManager(security, Period.hours(1));        
        
        Portfolio portfolio = new Portfolio();
        adapter.setPortfolio(portfolio);
        
//        Strategy strategy = new SimpleStrategy(chartManager.getChart());
        ReductionStrategy strategy = new ReductionStrategy(chartManager.getChart());
        StrategyManager strategyManager = new StrategyManager(chartManager, strategy);
        
        StrategyTrader strategyTrader = new StrategyTrader(portfolio, chartManager, strategyManager, adapter, initCapital.dividedBy(Decimal.valueOf(22)), Decimal.valueOf(1000));
        
        adapter.addChartManager(chartManager);
        adapter.mockProcessTicks();
        

        List<Order> orders = adapter.getDeals();
        System.out.println("Orders size: " + orders.size());
        Optional<Order> liquidationOrder = strategyTrader.getLiquidationOrder();
        if(liquidationOrder.isPresent()) {
            orders.add(liquidationOrder.get());
        }
        
        //Clear profit
        System.out.println("Clear profit: " + new ClearProfitCriterion().calculate(orders));
        System.out.println("Clear profit %: " + (new ClearProfitCriterion().calculate(orders)/initCapital.toDouble()) * 100);
    }
}
