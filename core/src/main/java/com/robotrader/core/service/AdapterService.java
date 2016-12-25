/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.service;

import com.robotrader.core.factor.Bar;
import com.robotrader.core.factor.Paper;
import com.robotrader.core.interval.Interval;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 *
 * @author 1
 */
public interface AdapterService {
    public List<Bar> getArchiveBars(Paper paper, Interval interval, Date dateFrom, Date dateTo);
    public List<Bar> getArchiveBars(Paper paper, Interval interval, int lastCount, Date dateTo);
    public List<Bar> getArchiveBars(Paper paper, Interval interval, int lastCount);
//    public void createLimitOrder(LimitOrder order) throws AdapterException;
//    public List<LimitOrder> getActiveLimitOrders() throws AdapterException;
//    public void createStopOrder(StopOrder order) throws AdapterException;
//    public List<StopOrder> getActiveStopOrders() throws AdapterException;
//    public void cancelOrder(Long id) throws AdapterException;
//    public List<Order> getActiveOrders() throws AdapterException;
//    public Long getPositions(String factorCode) throws AdapterException;
//    public BigDecimal getProfit() throws AdapterException;
    public Set<Paper> listPapers(String exchangeCode, String marketCode, String boardCode);
}
