/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.adapter.business;

import com.robotrader.core.factor.Bar;
import com.robotrader.core.factor.Paper;
import com.robotrader.core.interval.Interval;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aav
 */
public class MoexAdapterServiceImplTest {
    
    public MoexAdapterServiceImplTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getArchiveBars method, of class MoexAdapterServiceImpl.
     */
    @Test
    public void testGetArchiveBars() {
        System.out.println("getArchiveBars");
        Paper paper = new Paper("TEST", "TEST", "TEST", "CLSB");
        Interval interval = Interval.ONE_WEEK;
        int expectedCount = 10;
        
        MoexAdapterServiceImpl instance = new MoexAdapterServiceImpl();
        List<Bar> result = instance.getArchiveBars(paper, interval, expectedCount);
        assertEquals(expectedCount, result.size());        
    }
    
}
