/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer;

import com.robotrader.core.factor.CandleStorage;
import com.robotrader.core.factor.Interval;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aanpilov
 */
public class WavesStorageTest {
    
    @Test
    public void testScanner() throws Exception {
        CandleStorage storage = new FinamFileParser(null, Interval.ONE_HOUR).parse("src/test/resources/candles/candles.txt");
        
        PivotsScanner scanner = new PivotsScanner();
        for(Date date : storage.getDates()) {
            scanner.add(storage.getBar(date));
        }
        
        WavesStorage wavesStorage = new WavesStorage();
        for(Pivot pivot : scanner.getPivots()) {
            wavesStorage.addPivot(pivot);
        }
        
        System.out.println("Detected waves count: " + wavesStorage.getWaves().size());
//        System.out.println("" + wavesScanner.getWaves());
        
        WavesFilter wavesFilter = new WavesFilter();
        WavesStorage filteredWaves = wavesFilter.filterCorrectionModule(wavesStorage);
        
        System.out.println("Filtered waves count: " + filteredWaves.getWaves().size());
        System.out.println("" + filteredWaves.getWaves());
    }
}