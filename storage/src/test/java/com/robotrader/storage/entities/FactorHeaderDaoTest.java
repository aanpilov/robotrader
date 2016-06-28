/**
 *
 */
package com.robotrader.storage.entities;

import com.robotrader.storage.util.ModuleApplicationContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;

/**
 * @author 1
 *
 */
public class FactorHeaderDaoTest extends TestCase {
    private ApplicationContext context;
    private FactorHeaderDao factorHeaderDao;
    /*
     * (non-Javadoc) @see junit.framework.TestCase#setUp()
     */
    @Before
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = ModuleApplicationContext.getInstance();
        factorHeaderDao = (FactorHeaderDao) context.getBean("factorHeaderDao");
    }

    /*
     * (non-Javadoc) @see junit.framework.TestCase#tearDown()
     */
    @After
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void test() throws Exception {
        factorHeaderDao.findByCode("SBER_FUT");
    }
}
