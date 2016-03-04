/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.chart;

import com.robotrader.core.factor.Candle;
import com.robotrader.core.factor.CandleStorage;
import java.util.Date;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.OHLCDataItem;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author aanpilov
 */
public class CandleStoragePlotter {
    private CandleStorage candleStorage;

    public CandleStoragePlotter(CandleStorage candleStorage) {
        this.candleStorage = candleStorage;
    }
    
    public XYDataset getDataset() {
        OHLCDataItem[] data = new OHLCDataItem[candleStorage.getDates().size()];
        
        int i = 0;
        for(Date date : candleStorage.getDates()) {
            Candle candle = candleStorage.getBar(date);
            OHLCDataItem item = new OHLCDataItem(date, candle.getOpenValue().doubleValue(), candle.getMaxValue().doubleValue(), candle.getMinValue().doubleValue(), candle.getCloseValue().doubleValue(), candle.getVolume().doubleValue());
            data[i] = item;
            i++;
        }
        
        return new DefaultOHLCDataset("Storage", data);
    }
    
    public XYItemRenderer getRenderer() {
        return new CandlestickRenderer();
    }
}
