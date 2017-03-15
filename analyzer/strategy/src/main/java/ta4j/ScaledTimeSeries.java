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
import eu.verdelhan.ta4j.indicators.helpers.HighestValueIndicator;
import eu.verdelhan.ta4j.indicators.helpers.LowestValueIndicator;
import eu.verdelhan.ta4j.indicators.simple.MaxPriceIndicator;
import eu.verdelhan.ta4j.indicators.simple.MinPriceIndicator;
import eu.verdelhan.ta4j.indicators.simple.VolumeIndicator;
import java.util.List;
import org.joda.time.Period;

/**
 *
 * @author aanpilov
 */
public class ScaledTimeSeries extends TimeSeries {
    private final Period period;
    private final Integer count;
    private final boolean isPeriod;
    private final TimeSeries child;
    private TimeSeries scaled;
    
    public ScaledTimeSeries(String name, TimeSeries timeSeries, Period period) {
        super(name);
        this.period = period;
        this.count = null;
        this.isPeriod = true;
        child = timeSeries;
    }
    
    public ScaledTimeSeries(String name, TimeSeries timeSeries, Period period, Integer count) {
        super(name);
        this.period = period;
        this.count = count;
        this.isPeriod = false;
        child = timeSeries;
    }
    
    public final void rebuild() {        
        rebuild(child.getEnd());
    }
    
    public final void rebuild(int index) {
        scaled = new TimeSeries();
        
        if(child.getTickCount() == 0) {
            return;
        }
        
        TimeSeries subseries = child.subseries(0, index);
        
        List<TimeSeries> split = null;
        if(isPeriod) {
            split = subseries.split(period);
        } else {
            split = subseries.split(count);
        }
        for (TimeSeries timeSeries : split) {
            int timeFrame = timeSeries.getEnd() - timeSeries.getBegin() + 1;
            
            Decimal openPrice = timeSeries.getFirstTick().getOpenPrice();
            Decimal closePrice = timeSeries.getLastTick().getClosePrice();
            Decimal maxValue = new HighestValueIndicator(new MaxPriceIndicator(timeSeries), timeFrame).getValue(timeSeries.getEnd());
            Decimal minValue = new LowestValueIndicator(new MinPriceIndicator(timeSeries), timeFrame).getValue(timeSeries.getEnd());
            Decimal volume = new VolumeIndicator(timeSeries).getValue(timeSeries.getEnd());
            
            Tick resultTick = new Tick(period, timeSeries.getLastTick().getEndTime(), openPrice, maxValue, minValue, closePrice, volume);
            scaled.addTick(resultTick);
        }
    }

    @Override
    public TradingRecord run(Strategy strategy, Order.OrderType orderType, Decimal amount) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TradingRecord run(Strategy strategy, Order.OrderType orderType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TradingRecord run(Strategy strategy) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TimeSeries> split(Period duration) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TimeSeries> split(Period splitDuration, Period sliceDuration) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TimeSeries> split(int nbTicks) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TimeSeries subseries(int beginIndex, Period duration) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TimeSeries subseries(int beginIndex, int endIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addTick(Tick tick) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getRemovedTicksCount() {
        return scaled.getRemovedTicksCount();
    }

    @Override
    public int getMaximumTickCount() {
        return scaled.getMaximumTickCount();
    }

    @Override
    public void setMaximumTickCount(int maximumTickCount) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getSeriesPeriodDescription() {        
        return super.getSeriesPeriodDescription();
    }

    @Override
    public int getEnd() {
        return scaled.getEnd();
    }

    @Override
    public int getBegin() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getTickCount() {
        return scaled.getTickCount();
    }

    @Override
    public Tick getLastTick() {
        return scaled.getLastTick();
    }

    @Override
    public Tick getFirstTick() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Tick getTick(int i) {
        return scaled.getTick(i);
    }

    @Override
    public String getName() {
        return super.getName();
    }
}
