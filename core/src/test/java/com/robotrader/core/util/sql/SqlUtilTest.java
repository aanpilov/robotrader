/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.util.sql;

import com.robotrader.core.util.CoreContextInstance;
import org.junit.*;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author 1
 */
public class SqlUtilTest {
    
    public SqlUtilTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getJdbcTemplate method, of class SqlUtil.
     */
    @Test
    public void testGetJdbcTemplate() {
        ApplicationContext context = CoreContextInstance.getContext();
        context.getBean("sqlUtil");
    }
}
