/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.adapter;

/**
 *
 * @author 1
 */
public class AdapterStockThreadTest {
    public AdapterStockThreadTest() {
    }

    @org.junit.BeforeClass
    public static void setUpClass() throws Exception {
    }

    @org.junit.AfterClass
    public static void tearDownClass() throws Exception {
    }

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    /**
     * Test of run method, of class AdapterStockThread.
     */
    @org.junit.Test
    public void testRun() throws Exception {
        System.out.println("run");
        AdapterStockThread instance = new AdapterStockThread();
        instance.start();
        
        Thread.sleep(5000);
        
        instance.setActive(false);
        instance.join();
    }
}
