/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ta4j;

import com.robotrader.analyzer.trader.Portfolio;
import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Order;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import ta4j.analysis.criteria.ClearProfitCriterion;
import ta4j.loader.FinamCsvTicksLoader;
import ta4j.strategy.Advice;
import ta4j.strategy.ReductionStrategy;
import ta4j.strategy.SimpleStrategy;
import ta4j.strategy.Strategy;

/**
 *
 * @author aanpilov
 */
public class StrategyManager {    
    private final TimeSeries series;
    private final Strategy strategy;
    
    private final Portfolio portfolio;
    private final List<Order> orders = new ArrayList<>();
    
    public StrategyManager(Strategy strategy, Portfolio portfolio) {
        this.portfolio = portfolio;        
        this.strategy = strategy;
        series = strategy.getTimeSeries();
    }
    
    public void addTick(Tick tick) {
        series.addTick(tick);
        
        processLastTick();
    }

    public List<Order> getOrders() {
        return orders;
    }
    
    public Optional<Order> getLiquidationOrder() {
        if(portfolio.getPosition().isPositive()) {
            return Optional.of(Order.sellAt(series.getEnd(), series.getLastTick().getClosePrice(), portfolio.getPosition().abs()));
        } else if(portfolio.getPosition().isNegative()) {
            return Optional.of(Order.buyAt(series.getEnd(), series.getLastTick().getClosePrice(), portfolio.getPosition().abs()));
        } else {
            return Optional.empty();
        }
    }
    
    private Optional<Order> processLastTick() {
        int endIndex = series.getEnd();
        
        Optional<Order> order = portfolio.processNewTick(series.getLastTick());
        if(order.isPresent()) {
            orders.add(order.get());
        }
        
        if(portfolio.getPosition().isPositive() && strategy.getAdvice(endIndex) == Advice.EXIT_LONG) {
            portfolio.setOrder(series.getLastTick(), Decimal.ZERO);
        }
        
        if(portfolio.getPosition().isNegative() && strategy.getAdvice(endIndex) == Advice.EXIT_SHORT) {
            portfolio.setOrder(series.getLastTick(), Decimal.ZERO);
        }
        
        if(!portfolio.getPosition().isPositive() && strategy.getAdvice(endIndex) == Advice.ENTER_LONG) {
            portfolio.setOrder(series.getLastTick(), Decimal.ONE);
        }
        
        if(!portfolio.getPosition().isNegative() && strategy.getAdvice(endIndex) == Advice.ENTER_SHORT) {
            portfolio.setOrder(series.getLastTick(), Decimal.valueOf(-1));
        }
        
        if(portfolio.getPosition().isPositive() && strategy.getAdvice(endIndex) == Advice.REDUCE_LONG) {
            portfolio.reducePosition(series.getLastTick());
        }
        
        if(portfolio.getPosition().isNegative() && strategy.getAdvice(endIndex) == Advice.REDUCE_SHORT) {
            portfolio.reducePosition(series.getLastTick());
        }
        
        return order;
    }
    
    public static void main(String[] args) throws Exception {
        Decimal initCapital = Decimal.valueOf(22000);
        Portfolio portfolio = new Portfolio(initCapital.dividedBy(Decimal.valueOf(22)), Decimal.NaN);
        TimeSeries testTimeSeries = FinamCsvTicksLoader.loadSeries(new File("src/test/resources/finam/SBER_H_2013.csv"));
        
//        Strategy strategy = new SimpleStrategy();
        ReductionStrategy strategy = new ReductionStrategy();
        
        StrategyManager strategyManager = new StrategyManager(strategy, portfolio);
        
        for (int i = 0; i < testTimeSeries.getTickCount(); i++) {            
            strategyManager.addTick(testTimeSeries.getTick(i));
        }
        
        List<Order> orders = strategyManager.getOrders();
        if(strategyManager.getLiquidationOrder().isPresent()) {
            orders.add(strategyManager.getLiquidationOrder().get());
        }
        
        //Clear profit
        System.out.println("Clear profit: " + new ClearProfitCriterion().calculate(orders));
        System.out.println("Clear profit %: " + (new ClearProfitCriterion().calculate(orders)/initCapital.toDouble()) * 100);
    }
}
