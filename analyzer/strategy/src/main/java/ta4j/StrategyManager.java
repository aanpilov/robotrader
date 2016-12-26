/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ta4j;

import com.robotrader.analyzer.chart.ChartListener;
import com.robotrader.analyzer.chart.ChartManager;
import com.robotrader.analyzer.chart.FinamFileChartManager;
import com.robotrader.analyzer.trader.Portfolio;
import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Order;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.joda.time.Period;
import ta4j.analysis.criteria.ClearProfitCriterion;
import ta4j.strategy.Advice;
import ta4j.strategy.ReductionStrategy;
import ta4j.strategy.SimpleStrategy;
import ta4j.strategy.Strategy;

/**
 *
 * @author aanpilov
 */
public class StrategyManager implements ChartListener {    
    private final TimeSeries series;
    
    private final Strategy strategy;    
    private final Portfolio portfolio;
    private final ChartManager chartManager;
    
    private final List<Order> orders = new ArrayList<>();
    
    public StrategyManager(ChartManager chartManager, Strategy strategy, Portfolio portfolio) {
        this.portfolio = portfolio;        
        this.strategy = strategy;
        series = strategy.getTimeSeries();
        this.chartManager = chartManager;        
    }

    public void start() {
        chartManager.addChartListener(this);
        chartManager.start();
    }
    
    @Override
    public void archiveTickAdded(Tick tick) {        
    }

    @Override
    public void onlineTickAdded(Tick tick) {
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
