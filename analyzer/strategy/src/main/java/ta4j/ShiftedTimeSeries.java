/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ta4j;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Order;
import eu.verdelhan.ta4j.Strategy;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.TradingRecord;
import java.util.List;
import org.joda.time.Period;

/**
 *
 * @author aanpilov
 */
public class ShiftedTimeSeries extends TimeSeries {        
    private final TimeSeries child;
    private TimeSeries result;
    
    public ShiftedTimeSeries(String name, TimeSeries timeSeries) {
        super(name);
        child = timeSeries;
    }
    
//    public void rebuild() {        
//        rebuild(child.getEnd());
//    }
//    
//    public void rebuild(int index) {
//        result = new TimeSeries();
//        
//        if(child.getTickCount() == 0) {
//            return;
//        }
//        
//        TimeSeries subseries = child.subseries(0, index);
//        for (int i = 1; i < subseries.getEnd(); i++) {
//            Tick prevTick = subseries.getTick(i - 1);
//            Tick currentTick = subseries.getTick(i);
//            
//            Tick newTick = new Tick(prevTick.getTimePeriod(), prevTick.getEndTime(), currentTick.getOpenPrice(), currentTick.getMaxPrice(), currentTick.getMinPrice(), currentTick.getClosePrice(), currentTick.getVolume());
//            result.addTick(newTick);
//        }
//        Tick lastTick = subseries.getLastTick();        
//        result.addTick(lastTick);
//    }
//
//    @Override
//    public TradingRecord run(Strategy strategy, Order.OrderType orderType, Decimal amount) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public TradingRecord run(Strategy strategy, Order.OrderType orderType) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public TradingRecord run(Strategy strategy) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public List<TimeSeries> split(Period duration) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public List<TimeSeries> split(Period splitDuration, Period sliceDuration) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public List<TimeSeries> split(int nbTicks) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public TimeSeries subseries(int beginIndex, Period duration) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public TimeSeries subseries(int beginIndex, int endIndex) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void addTick(Tick tick) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

    @Override
    public int getRemovedTicksCount() {
        return child.getRemovedTicksCount();
    }

    @Override
    public int getMaximumTickCount() {
        return child.getMaximumTickCount();
    }

//    @Override
//    public void setMaximumTickCount(int maximumTickCount) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

    @Override
    public String getSeriesPeriodDescription() {        
        return super.getSeriesPeriodDescription();
    }

    @Override
    public int getEnd() {
        return child.getEnd();
    }

//    @Override
//    public int getBegin() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

    @Override
    public int getTickCount() {
        return child.getTickCount();
    }

    @Override
    public Tick getLastTick() {
        return child.getLastTick();
    }

//    @Override
//    public Tick getFirstTick() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

    @Override
    public Tick getTick(int i) {
        int counter = (i == child.getEnd())? i : (i + 1);
        return child.getTick(counter);
    }

    @Override
    public String getName() {
        return super.getName();
    }
}
