/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.adapter;

import com.robotrader.core.factor.Factor;
import com.robotrader.core.factor.FactorSet;
import com.robotrader.core.factor.FactorSetCollection;
import com.robotrader.core.factor.OnlineFactor;
import com.robotrader.core.util.CoreContextInstance;
import com.robotrader.core.util.jaxb.JAXBUtil;
import com.robotrader.core.util.sql.SqlUtil;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;

/**
 *
 * @author 1
 */
public class AdapterStockThread extends Thread {
    private Logger log = Logger.getLogger(this.getClass());
    
    private boolean active;
    private Long delay;
    private SqlUtil sqlUtil;
    private JAXBUtil jaxbUtil;
    private JmsTemplate jmsTemplate;
    
    private Calendar localDate;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    
    public AdapterStockThread() {
        ApplicationContext context = CoreContextInstance.getContext();
        sqlUtil = (SqlUtil) context.getBean("sqlUtil");
        jaxbUtil = (JAXBUtil) context.getBean("jaxbUtil");
        jmsTemplate = (JmsTemplate) context.getBean("jmsTemplate");
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void run() {
        init();
        
        while(active) {
            try {
                FactorSetCollection factors = getFactors(localDate.getTime());
                log.debug("Factors collection size: " + factors.getCollection().size());
                
                for (Iterator<FactorSet> iter = factors.getCollection().iterator(); iter.hasNext();) {
                    FactorSet factorSet = iter.next();
                    String factorSetMessage = jaxbUtil.marshallObject(factorSet);
                    log.debug("FactorSet: " + factorSetMessage);
                    jmsTemplate.convertAndSend("ADAPTER.FACTORS", factorSetMessage);
                }
                
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                log.error(ex);
            } catch(Exception e) {
                log.error(e);
            }
            incrementLocalDate();
        }
    }
    
    private FactorSetCollection getFactors(Date factorsDate) {
        FactorSetCollection collection = new FactorSetCollection();
        
        String sql =      "select   paper.factor_code, "
                        + "         price.price_date, "
                        + "         price.value, "
                        + "         price.volume "
                        + "from mock_adapter.paper_price price "
                        + " join mock_adapter.papers paper on paper.id = price.paper_id "
                        + "where 1 = 1 "
                        + "and price.price_date = ? "
                        + "order by price.price_date, price.id;";
        
        Object[] args = new Object[]{factorsDate};
        
        List<Map<String, Object>> priceList = sqlUtil.getJdbcTemplate().queryForList(sql, args);
        
        for (Iterator<Map<String, Object>> iter = priceList.iterator(); iter.hasNext();) {
            Map<String, Object> priceMap = iter.next();
            
            String factorCode = (String) priceMap.get("FACTOR_CODE");
            Date priceDate = (Date) priceMap.get("PRICE_DATE");
            BigDecimal price = new BigDecimal(((Number)priceMap.get("VALUE")).doubleValue());
            Factor factor = new OnlineFactor(factorCode, priceDate, true, price);
            
            collection.add(factor);
        }
        
        return collection;
    }

    private void init() {
        String sql =    "select * "
                      + "from mock_adapter.params";
        Map<String, Object> params = sqlUtil.getJdbcTemplate().queryForMap(sql);
        
        localDate = new GregorianCalendar();
        Object currDateParam = params.get("curr_date");
        Date currDate = (Date) ((currDateParam == null)? new Date():currDateParam);
        localDate.setTime(currDate);
        log.debug("init local date: " + sdf.format(localDate.getTime()));
        
        Object delayParam = params.get("second_delay");
        delay = (delayParam == null)? new Long(1000): new Long(((Number) delayParam).longValue());
        log.debug("init delay: " + delay);
        
        active = true;
    }

    private void incrementLocalDate() {
        localDate.add(Calendar.SECOND, 1);
        
        String sql =    "update mock_adapter.params "
                      + "   set curr_date = ?";
        Object[] args = new Object[]{localDate.getTime()};
        sqlUtil.getJdbcTemplate().update(sql, args);
        
        log.debug("increment local date: " + sdf.format(localDate.getTime()));
    }
}
