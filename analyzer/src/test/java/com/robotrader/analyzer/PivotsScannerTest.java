/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer;

import com.robotrader.core.factor.CandleStorage;
import com.robotrader.core.factor.Interval;
import java.util.Date;
import org.junit.Test;

/**
 *
 * @author aanpilov
 */
public class PivotsScannerTest {    
    
    public PivotsScannerTest() {
    }

    @Test
    public void testSomeMethod() throws Exception {
        PivotsScanner scanner = new PivotsScanner();
        
        CandleStorage storage = new FinamFileParser(null, Interval.ONE_HOUR).parse("src/test/resources/candles/candles.txt");
        
        for(Date date : storage.getDates()) {
            scanner.add(storage.getBar(date));
        }
    }
}