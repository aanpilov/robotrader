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
import com.robotrader.mock.MockPortfolioManager;
import com.robotrader.mock.MockStrategyTrader;
import com.robotrader.mock.TradingRecordBuilder;
import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Order;
import eu.verdelhan.ta4j.TradingRecord;
import java.io.File;
import java.util.List;
import java.util.Optional;
import org.joda.time.Period;
import ta4j.analysis.criteria.ClearProfitCriterion;
import ta4j.analysis.criteria.MaximumLossSeries;
import ta4j.analysis.criteria.ProfitableTradesRatio;
import ta4j.analysis.criteria.TotalProfitCriterion;
import ta4j.strategy.ReductionStrategy;
import ta4j.strategy.SimpleStrategy;
import ta4j.strategy.Strategy;

/**
 *
 * @author aanpilov
 */
public class StrategyTraderTest {
        public static void main(String[] args) throws Exception {
        Security security = new Security(null, null, null, "SBER");
        
        FinamFileAdapter adapter = new FinamFileAdapter(new File("src/test/resources/finam/SBER_H_2016.csv"));
        MockChartManager chartManager = new MockChartManager(security, Period.hours(1));        
        
        MockPortfolioManager portfolioManager = new MockPortfolioManager();
        Portfolio portfolio = portfolioManager.getSecurityPortfolio(security);
        adapter.setPortfolio(portfolio);
        
//        Strategy strategy = new SimpleStrategy(chartManager.getChart());
        ReductionStrategy strategy = new ReductionStrategy(chartManager.getChart());
        StrategyManager strategyManager = new StrategyManager(chartManager, strategy);
        
        MockStrategyTrader strategyTrader = new MockStrategyTrader(portfolioManager, chartManager, strategyManager, adapter, Decimal.valueOf(0.01), Decimal.valueOf(1000));
        
        adapter.addChartManager(chartManager);
        adapter.mockProcessTicks();
        

        List<Order> orders = adapter.getDeals();
        
        Optional<Order> liquidationOrder = adapter.getLiquidationOrder();
        if(liquidationOrder.isPresent()) {
            orders.add(liquidationOrder.get());
        }
        
        Order[] ordersArray = new Order[orders.size()];
        orders.toArray(ordersArray);
        
        TradingRecord tradingRecord = TradingRecordBuilder.build(ordersArray);
        System.out.println("Trades size: " + tradingRecord.getTradeCount());
        
        //Clear profit
        System.out.println("TotalProfit: " + new TotalProfitCriterion().calculate(chartManager.getChart(), tradingRecord));
        System.out.println("MaximumLossSeries: " + new MaximumLossSeries().calculate(tradingRecord));
        System.out.println("ProfitableTradesRatio: " + new ProfitableTradesRatio().calculate(tradingRecord));
    }
}
