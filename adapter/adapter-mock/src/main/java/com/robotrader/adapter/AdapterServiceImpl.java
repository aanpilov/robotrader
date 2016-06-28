/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.adapter;

import com.robotrader.core.factor.FactorHeader;
import com.robotrader.core.factor.FactorSetCollection;
import com.robotrader.core.interval.Interval;
import com.robotrader.core.service.AdapterService;
import com.robotrader.core.util.CoreContextInstance;
import com.robotrader.core.util.sql.SqlUtil;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author 1
 */
public class AdapterServiceImpl implements AdapterService {
    private SqlUtil sqlUtil;
    private Date onlineStartDate;
    private Date onlineEndDate;
    private Long onlineDelay;
    private Long onlineMaxCount;
    
    public AdapterServiceImpl() {
        ApplicationContext context = CoreContextInstance.getContext();
        sqlUtil = (SqlUtil) context.getBean("sqlUtil");        
    }

    public FactorSetCollection getArchiveFactorSetCollection(Set<FactorHeader> papersHeaders, Interval interval, Date dateFrom, Date dateTo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void initParams() {
        String sql =    "select * "
                      + "from mock_adapter.mock_params";
        Map<String, Object> params = sqlUtil.getJdbcTemplate().queryForMap(sql);
        
        onlineStartDate = (Date) params.get("online_start_date");
        onlineEndDate = (Date) params.get("online_end_date");
        onlineDelay = new Long(((Number) params.get("online_delay")).longValue());
        onlineMaxCount = new Long(((Number) params.get("online_max_count")).longValue());
    }
    
    public static void main(String[] args) {
        AdapterService adapterService = new AdapterServiceImpl();        
    }
}
