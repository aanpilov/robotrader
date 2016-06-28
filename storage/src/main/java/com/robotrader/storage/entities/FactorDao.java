/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.storage.entities;

import com.robotrader.core.interval.Interval;
import com.robotrader.core.util.sql.SqlUtil;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author 1
 */
public class FactorDao {
    private Logger log = Logger.getLogger(getClass());
    private SqlUtil sqlUtil;

    public SqlUtil getSqlUtil() {
        return sqlUtil;
    }

    public void setSqlUtil(SqlUtil sqlUtil) {
        this.sqlUtil = sqlUtil;
    }
    
    public void importFactor(FactorArchive factorArchive) throws Exception {
        String procedureName = "robotrader.load_interval_factor";
        
        Object[] args = new Object[] {
            factorArchive.getFactorId(),
            factorArchive.getStartDate(),
            factorArchive.getOpenPrice(),
            factorArchive.getClosePrice(),
            factorArchive.getMinPrice(),
            factorArchive.getMaxPrice(),
            factorArchive.getVolume()
        };
        
        executeStoredProcedure(procedureName, args);
    }
    
    public void importFactor(FactorOnline factor) throws Exception {
        String procedureName = "robotrader.load_online_factor";
        
        Object[] args = new Object[] {
            factor.getFactorId(),
            factor.getDate(),
            factor.getValue(),
            factor.isActive()
        };
        
        log.debug("importFactor: " + Arrays.asList(args));
        
        executeStoredProcedure(procedureName, args);
    }
    
    protected void executeStoredProcedure(String procedureName, Object[] params) throws Exception {
        String callString = "{call " + procedureName + " (";
        callString = sqlUtil.appendSqlParams(callString, params);
        callString += ")}";

        sqlUtil.executeProcedure(callString, params);
    }

    protected Object executeFunction(String functionName, Object[] params, int resultType) throws Exception {
        String callString = "{? = call " + functionName + " (";
        callString = sqlUtil.appendSqlParams(callString, params);
        callString += ")}";

        return sqlUtil.executeFunction(callString, params, resultType);
    }
    
    public Date getLastArchiveStartDate() throws Exception {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -2);
        Date lastArchiveStartDate = calendar.getTime();
        
        String sql =    "select max(START_DATE) MAX_START_DATE from robotrader.factor_archive";
        Map maxStartDateMap = (Map) sqlUtil.getFirstResult(sql, new Object[]{});
        if(maxStartDateMap.get("MAX_START_DATE") != null) {
            Timestamp ts = (Timestamp) maxStartDateMap.get("MAX_START_DATE");
            lastArchiveStartDate = new Date(ts.getTime());
        }
        
        return lastArchiveStartDate;
    }

    public Set<FactorArchive> getActiveFactors() {
        String sql =    "select archive.*\n" +
                        "from robotrader.factor_board board\n" +
                        "   join robotrader.factor_archive_15_minute archive\n" +
                        "       on archive.factor_id = board.factor_id\n" +
                        "	and archive.start_date = date_sub(date_sub(stock_date, INTERVAL SECOND(stock_date) SECOND), INTERVAL MINUTE(stock_date)%15 MINUTE)\n" +
                        "where board.is_active = 1;";
        
        return sqlUtil.getJdbcTemplate().query(sql, new FactorArchiveExtractor());
    }
    
    public Set<FactorArchive> getArchiveFactors(Date startDate) throws Exception {
        Date lastArchiveStartDate = getLastArchiveStartDate();
        
        String sql =    "select archive.*\n" +
                        "from robotrader.factor_archive_15_minute archive\n" +
                        "where 1 = 1\n"
                      + "and start_date >= ?\n"
                      + "and start_date < ?;";
        
        Object[] args = new Object[]{
            startDate,
            lastArchiveStartDate
        };
        
        return sqlUtil.getJdbcTemplate().query(sql, args, new FactorArchiveExtractor());
    }

    public BigDecimal getAverageValue(Long factorId, Interval interval, Long intervalsCount) {
        //TODO - пока возвращаем для 15_M интервала
        String sql =    "select AVG(CLOSE_PRICE)\n" +
                        "from (\n" +
                        "	select *\n" +
                        "	from robotrader.factor_archive_15_minute\n" +
                        "	where factor_id = ?\n" +
                        "	order by start_date desc\n" +
                        "	LIMIT ?\n" +
                        ") intervals;";
        Object[] args = new Object[] {
            factorId,
            intervalsCount
        };
        
        return sqlUtil.getJdbcTemplate().queryForObject(sql, args, BigDecimal.class);
    }
}
