/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.strategy.tajtutorial.loader;

import au.com.bytecode.opencsv.CSVReader;
import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.oscillators.StochasticOscillatorKIndicator;
import eu.verdelhan.ta4j.indicators.simple.MaxPriceIndicator;
import eu.verdelhan.ta4j.indicators.simple.MinPriceIndicator;
import eu.verdelhan.ta4j.indicators.simple.VolumeIndicator;
import eu.verdelhan.ta4j.indicators.trackers.SMAIndicator;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aanpilov
 */
public class FinamCsvTicksLoader {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger("com.loader");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
    
    public static TimeSeries loadSeries(File file) throws Exception {
        InputStream stream = new FileInputStream(file);

        List<Tick> ticks = new ArrayList<>();        

        CSVReader csvReader = new CSVReader(new InputStreamReader(stream, Charset.forName("UTF-8")), ',', '"', 1);
        try {
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                String dateStr = line[0] + " " + line[1];
                DateTime date = new DateTime(DATE_FORMAT.parse(dateStr));
                double open = Double.parseDouble(line[2]);
                double high = Double.parseDouble(line[3]);
                double low = Double.parseDouble(line[4]);
                double close = Double.parseDouble(line[5]);
                double volume = Double.parseDouble(line[6]);

                ticks.add(new Tick(date, open, high, low, close, volume));
            }
        } catch (IOException ioe) {
            log.error("Unable to load ticks from CSV", ioe);
        } catch (ParseException pe) {
            log.error("Error while parsing date", pe);
        } catch (NumberFormatException nfe) {
            log.error("Error while parsing value", nfe);
        }

        return new TimeSeries("finam_ticks", ticks);
    }
    
    public static void main(String[] args) throws Exception {
        TimeSeries series = FinamCsvTicksLoader.loadSeries(new File("src/test/resources/SBER_160919_161011_H.csv"));

        System.out.println("Series: " + series.getName() + " (" + series.getSeriesPeriodDescription() + ")");
        System.out.println("Number of ticks: " + series.getTickCount());
        System.out.println("Time period: " + series.getTimePeriod());
        System.out.println("First tick: \n"
                + "\tVolume: " + series.getTick(0).getVolume() + "\n"
                + "\tOpen price: " + series.getTick(0).getOpenPrice()+ "\n"
                + "\tClose price: " + series.getTick(0).getClosePrice());
        
        List<TimeSeries> dailySeriesList = series.split(Period.days(1));
        TimeSeries dailySeries = new TimeSeries("daily", Period.days(1));
        for(TimeSeries dailyLowSeries : dailySeriesList) {
            Decimal openPrice = dailyLowSeries.getFirstTick().getOpenPrice();
            Decimal closePrice = dailyLowSeries.getLastTick().getClosePrice();
            Decimal maxValue = new MaxPriceIndicator(dailyLowSeries).getValue(dailyLowSeries.getEnd());
            Decimal minValue = new MinPriceIndicator(dailyLowSeries).getValue(dailyLowSeries.getEnd());
            Decimal volume = new VolumeIndicator(series).getValue(dailyLowSeries.getEnd());
            
            Tick dayTick = new Tick(Period.days(1), dailyLowSeries.getLastTick().getEndTime(), openPrice, maxValue, minValue, closePrice, volume);
            dailySeries.addTick(dayTick);
        }
        
        System.out.println("Series: " + dailySeries.getName() + " (" + dailySeries.getSeriesPeriodDescription() + ")");
        System.out.println("Number of ticks: " + dailySeries.getTickCount());
        System.out.println("Time period: " + dailySeries.getTimePeriod());
        System.out.println("Last tick: \n"
                + "\tVolume: " + dailySeries.getLastTick().getVolume() + "\n"
                + "\tOpen price: " + dailySeries.getLastTick().getOpenPrice()+ "\n"
                + "\tClose price: " + dailySeries.getLastTick().getClosePrice());
        
        StochasticOscillatorKIndicator stoch = new StochasticOscillatorKIndicator(dailySeries, 5);        
        SMAIndicator kIndicator = new SMAIndicator(stoch, 3);
        SMAIndicator dIndicator = new SMAIndicator(kIndicator, 3);
        for(int i = 0; i < dailySeries.getTickCount(); i++) {            
            System.out.println("I: " + i);
            Tick tick = dailySeries.getTick(i);
            System.out.println(String.format("[time: %1$td/%1$tm/%1$tY %1$tH:%1$tM:%1$tS, open: %2$f, high: %3$f, low: %4$f, close: %5$f]",
                tick.getEndTime().toGregorianCalendar(), tick.getOpenPrice().toDouble(), tick.getMaxPrice().toDouble(), tick.getMinPrice().toDouble(), tick.getClosePrice().toDouble()));
            System.out.println("St: " + stoch.getValue(i));
            System.out.println("K: " + kIndicator.getValue(i));
            System.out.println("D: " + dIndicator.getValue(i));
        }
    }
}
