/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.strategy.strategy;

import com.robotrader.adapter.util.ModuleApplicationContext;
import com.robotrader.core.factor.Paper;
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
public class InvestmentStrategyWithoutPositionTest {
    private AdapterService adapterService;
    
    public InvestmentStrategyWithoutPositionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        ApplicationContext context = ModuleApplicationContext.getContext();
        adapterService = (AdapterService) context.getBean("adapter");
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of run method, of class InvestmentStrategyWithoutPosition.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        
        Paper paper = new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "VTGK");
        
        InvestmentStrategyWithoutPosition instance = new InvestmentStrategyWithoutPosition(paper, adapterService);
        instance.run();
    }
    
}
