/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer;

import com.robotrader.core.factor.Candle;
import com.robotrader.core.factor.CandleStorage;
import com.robotrader.core.factor.Interval;
import com.robotrader.core.factor.Paper;
import java.io.FileReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author aanpilov
 */
public class FinamFileParser {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd hh:mm:ss");
    private Paper paper;
    private Interval interval;

    public FinamFileParser(Paper paper, Interval interval) {
        this.paper = paper;
        this.interval = interval;
    }
    
    public CandleStorage parse(String fileName) throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader(new String[]{"DATE", "TIME", "OPEN", "HIGH", "LOW", "CLOSE", "VOL"}).withSkipHeaderRecord();
        
        List<Candle> candleList = new ArrayList<>();
        CSVParser parser = csvFormat.parse(new FileReader("src/test/resources/candles/candles.txt"));
        for(CSVRecord record : parser) {
            Map<String, String> recordMap = record.toMap();
            
            String dateTimeStr = recordMap.get("DATE") + " " + recordMap.get("TIME");
            Candle candle = new Candle(paper, sdf.parse(dateTimeStr), interval, new BigDecimal(recordMap.get("OPEN")), new BigDecimal(recordMap.get("CLOSE")), new BigDecimal(recordMap.get("LOW")), new BigDecimal(recordMap.get("HIGH")), new BigDecimal(recordMap.get("VOL")));            
            candleList.add(candle);
        }
        
        CandleStorage storage = new CandleStorage(paper, interval, candleList.size());
        for(Candle candle : candleList) {
            storage.addBar(candle);
        }
        
        return storage;
    }
}
