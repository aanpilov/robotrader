/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.storage.entities;

import com.robotrader.core.util.sql.SqlUtil;
import com.robotrader.storage.util.ModuleApplicationContext;
import java.math.BigDecimal;
import java.util.Date;
import junit.framework.TestCase;
import org.junit.*;
import static org.junit.Assert.*;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author 1
 */
public class FactorArchiveDaoTest extends TestCase {
    private ApplicationContext context;
    private FactorDao factorArchiveDao;
    
    public FactorArchiveDaoTest() {
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        context = ModuleApplicationContext.getInstance();
        factorArchiveDao = (FactorDao) context.getBean("factorArchiveDao");
    }
    
    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testImportFactor() throws Exception {
        System.out.println("importFactor");
        FactorArchive factorArchive = new FactorArchive();
        factorArchive.setFactorId(new Long("1"));
        factorArchive.setStartDate(new Date());
        factorArchive.setOpenPrice(new BigDecimal("1"));
        factorArchive.setClosePrice(new BigDecimal("1"));
        factorArchive.setMinPrice(new BigDecimal("1"));
        factorArchive.setMaxPrice(new BigDecimal("1"));
        
        factorArchiveDao.importFactor(factorArchive);
    }
}
