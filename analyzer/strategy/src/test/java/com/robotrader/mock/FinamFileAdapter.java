/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.mock;

import com.robotrader.analyzer.io.FinamCsvTicksLoader;
import com.robotrader.analyzer.portfolio.Portfolio;
import com.robotrader.core.objects.ConditionalOrder;
import com.robotrader.core.objects.Security;
import com.robotrader.core.service.AsyncAdapterService;
import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Order;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aanpilov
 */
public class FinamFileAdapter implements AsyncAdapterService {
    private final Map<Long, ConditionalOrder> orders = new HashMap<>();
    private final Set<MockChartManager> chartManagers = new HashSet<>();
    private Portfolio portfolio;
    private long position = 0;
    private int dealCounter = 0;
    
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final File file;
    private final List<Order> deals = new ArrayList<>();

    public FinamFileAdapter(File file) {
        this.file = file;
    }

    public List<Order> getDeals() {
        return deals;
    }
    
    public void mockProcessTicks() throws Exception {
        TimeSeries series = FinamCsvTicksLoader.loadSeries(file);
        for (int i = 0; i <= series.getEnd(); i++) {
            Tick tick = series.getTick(i);          
            log.info("New tick: " + tick);
            matchOrders(tick);
            chartManagers.forEach(manager -> manager.addOnlineTick(tick));
        }
    }
    
    @Override
    public void start() throws Exception {
        log.info("Started");
    }

    @Override
    public void stop() throws Exception {
        log.info("Stopped");
    }

    @Override
    public void getHistory(Security security, Period period, int count) throws Exception {
        log.info("Get history is disabled");
    }

    @Override
    public void subscribe(Security security) throws Exception {
        log.info("Subscribed to: " + security);
    }

    public synchronized void deleteConditionalOrder(ConditionalOrder order) {        
        log.info("Remove order: " + order);
        orders.remove(order.getOrderId());
        updatePortfolioOrders();
    }

    @Override
    public synchronized void createConditionalOrder(ConditionalOrder order) throws Exception {        
        long newOrderId = System.nanoTime();
        order.setOrderId(newOrderId);
        orders.put(order.getOrderId(), order);
        log.info("Add order: " + order);
        
        updatePortfolioOrders();
    }
    
    public void addChartManager(MockChartManager chartManager) {
        chartManagers.add(chartManager);
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    private synchronized void matchOrders(Tick tick) {
        List<Long> matchedIdentifiers = new ArrayList();
        for (ConditionalOrder order : orders.values()) {
            if(tick.getMinPrice().toDouble() <= order.getConditionValue()
               && order.getConditionValue() <= tick.getMaxPrice().toDouble()) {
                matchedIdentifiers.add(order.getOrderId());
            }
        }
        
        for (Long matchedIdentifier : matchedIdentifiers) {
            ConditionalOrder order = orders.get(matchedIdentifier);
            matchOrder(order);
        }
        
        for (Long matchedIdentifier : matchedIdentifiers) {
            orders.remove(matchedIdentifier);
        }
    }

    @Override
    public synchronized void removeOrder(long orderId) throws Exception {
        log.info("Remove order: " + orderId);
        orders.get(orderId).setStatus(ConditionalOrder.OrderStatus.CANCELLED);
        updatePortfolioOrders();
        orders.remove(orderId);
    }
    
    private void matchOrder(ConditionalOrder order) {
        order.setStatus(ConditionalOrder.OrderStatus.MATCHED);
        Order deal = null;
        if(order.isBuy()) {
            position += order.getQuantity();
            deal = Order.buyAt(dealCounter++, Decimal.valueOf(order.getPrice()), Decimal.valueOf(order.getQuantity()));
        } else {
            position -= order.getQuantity();
            deal = Order.sellAt(dealCounter++, Decimal.valueOf(order.getPrice()), Decimal.valueOf(order.getQuantity()));
        }
        
        deals.add(deal);
        log.info("New Deal: " + deal);
        
        updatePortfolioPositions();
        updatePortfolioOrders();
    }

    private synchronized void updatePortfolioOrders() {
        portfolio.importOrders(orders.values());
    }
    
    private void updatePortfolioPositions() {
        portfolio.importPositions(position);
    }
}
