/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.adapter.business;

import com.robotrader.adapter.core.MoexProxy;
import com.robotrader.adapter.core.MoexProxyFactory;
import com.robotrader.adapter.core.ResponseParser;
import com.robotrader.core.factor.Bar;
import com.robotrader.core.factor.Paper;
import com.robotrader.core.interval.Interval;
import com.robotrader.core.service.AdapterService;
import com.robotrader.core.service.AdapterServiceProxy;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author aav
 */
public class MoexAdapterServiceImpl extends AdapterServiceProxy {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private MoexProxy moexProxy = MoexProxyFactory.getInstance();
    private ResponseParser parser = new ResponseParser();

    @Override
    public List<Bar> getArchiveBars(Paper paper, Interval interval, Date dateFrom, Date dateTo) {
        List<Bar> result = new ArrayList<>();
        
        int duration = getDuration(interval);
        String from = sdf.format(dateFrom);
        String till = sdf.format(dateTo);
        
        String candles = moexProxy.getCandles(paper.getPaperTradeCode(), from, till, duration);
        try {
            List<Map<String, String>> table = parser.parseResponse(candles, "candles");
            for(Map<String, String> row : table) {
                Date openDate = sdf.parse(row.get("begin"));
                BigDecimal open = new BigDecimal(row.get("open"));
                BigDecimal close = new BigDecimal(row.get("close"));
                BigDecimal high = new BigDecimal(row.get("high"));
                BigDecimal low = new BigDecimal(row.get("low"));
                BigDecimal volume = new BigDecimal(row.get("volume"));
                Bar bar = new Bar(paper, openDate, interval, open, close, low, high, volume);
                result.add(bar);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        
        return result;
    }
    
    private int getDuration(Interval interval) {
        if(Interval.ONE_DAY == interval) {
            return 24;
        }
        if(Interval.ONE_WEEK == interval) {
            return 7;
        }
        if(Interval.ONE_MONTH == interval) {
            return 31;
        }
        
        throw new UnsupportedOperationException("Not supported yet. Unknown Interval");
    }

    @Override
    public Set<Paper> listPapers(String exchangeCode, String marketCode, String boardCode) {
        Set<Paper> result = new HashSet<>();
        
        String securities = moexProxy.getSecurities();
        try {
            List<Map<String, String>> table = parser.parseResponse(securities, "securities");
            for(Map<String, String> row : table) {
                Paper paper = new Paper(exchangeCode, marketCode, boardCode, row.get("SECID"));
                result.add(paper);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        
        return result;
    }
    
    public static void main(String[] args) throws Exception {
        AdapterService adapterService = new MoexAdapterServiceImpl();
        System.out.println(adapterService.listPapers(null, null, null));
    }
}
