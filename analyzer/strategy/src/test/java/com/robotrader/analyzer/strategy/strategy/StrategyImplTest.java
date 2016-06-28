/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.strategy.strategy;

import com.robotrader.adapter.util.ModuleApplicationContext;
import com.robotrader.analyzer.strategy.Advice;
import com.robotrader.core.factor.Paper;
import com.robotrader.core.interval.Interval;
import com.robotrader.core.service.AdapterService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author aav
 */
public class StrategyImplTest {
    
    public StrategyImplTest() {
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
     * Test of getAdvice method, of class StrategyImpl.
     */
    @Test
    public void testGetAdvice() {
        System.out.println("getAdvice");
        
        ApplicationContext context = ModuleApplicationContext.getContext();
        AdapterService adapterService = (AdapterService) context.getBean("adapter");
        
        Advice expResult = Advice.advBuy;
        
        StrategySpecification strategySpecification = new StrategySpecification(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "NKSH"), Interval.ONE_MONTH, expResult);
        
        StrategyImpl instance = new StrategyImpl(adapterService, strategySpecification);
        instance.run();
        
        Advice result = instance.getAdvice();
        
        System.out.println("Real advice: " + result);
        
        assertEquals(expResult, result);        
    }        
}
