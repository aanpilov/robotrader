/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.util.jaxb;

import com.robotrader.core.factor.FactorSet;
import com.robotrader.core.factor.IntervalFactor;
import com.robotrader.core.factor.LevelFactor;
import com.robotrader.core.factor.OnlineFactor;
import com.robotrader.core.factor.Paper;
import com.robotrader.core.interval.Interval;
import java.math.BigDecimal;
import java.util.Date;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.junit.*;

/**
 *
 * @author 1
 */
public class JAXBUtilTest extends TestCase {
    private JAXBUtil jaxbUtil;
    private Logger log = Logger.getLogger(getClass());
    
    public JAXBUtilTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {   
        jaxbUtil = new JAXBUtilImpl();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testMarshallFactorSet() throws Exception {
        FactorSet factorSet = new FactorSet();
        
        Paper testPaper = new Paper("TEST", "TEST", "TEST", "ONLINE_FACTOR_CODE");
        OnlineFactor onlineFactor = new OnlineFactor(testPaper, new Date(), true, BigDecimal.ZERO);
        factorSet.add(onlineFactor);
        
        IntervalFactor intervalFactor = new IntervalFactor(testPaper, new Date(), Interval.ONE_HOUR, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        factorSet.add(intervalFactor);
        
        LevelFactor levelFactor = new LevelFactor("LEVEL_CODE", "LEVEL_BASE_CODE", BigDecimal.ZERO);
        factorSet.add(levelFactor);
        
        String factorSetStr = jaxbUtil.marshallObject(factorSet);
        
        log.debug("FactorSet: \n" + factorSetStr);
        
        FactorSet factorSetUnmarshalled = (FactorSet) jaxbUtil.unmarshallObject(factorSetStr, FactorSet.class);
        
        assertEquals(factorSet.getIntervalFactors().size(), factorSetUnmarshalled.getIntervalFactors().size());
        assertEquals(factorSet.getOnlineFactors().size(), factorSetUnmarshalled.getOnlineFactors().size());
    }
}
