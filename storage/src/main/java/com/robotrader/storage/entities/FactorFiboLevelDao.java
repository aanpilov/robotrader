/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.storage.entities;

import com.robotrader.core.util.sql.SqlUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 *
 * @author 1
 */
public class FactorFiboLevelDao {
    private Logger log = Logger.getLogger(getClass());
    
    private SqlUtil sqlUtil;

    public void setSqlUtil(SqlUtil sqlUtil) {
        this.sqlUtil = sqlUtil;
    }
    
    public FactorFiboLevel getLevel(Long factorId) {
        String sql =    "select *\n" +
                        "from robotrader.factor_fibo_levels\n" +
                        "where factor_id = ?;";
        Object[] args = new Object[] {
            factorId
        };
        
        List<FactorFiboLevel> fiboLevels = sqlUtil.getJdbcTemplate().query(sql, args, new FiboLevelsExtractor());
        if(!fiboLevels.isEmpty()) {
            return fiboLevels.get(0);
        } else {
            return null;
        }
    }
    
    public List<FactorFiboLevel> listLevels() {
        String sql =    "select *\n" +
                        "from robotrader.factor_fibo_levels;";
        return sqlUtil.getJdbcTemplate().query(sql, new FiboLevelsExtractor());
    }
    
    public void updateLevel(FactorFiboLevel level) {
        String sql =    "update robotrader.factor_fibo_levels "
                      + "   set LEVEL_FROM = ?,\n"
                      + "       LEVEL_TO = ?\n"
                      + "where FACTOR_ID = ?;";
        
        Object[] args = new Object[] {
            level.getLowLevel(),
            level.getHighLevel(),
            level.getFactorId()
        };
        
        sqlUtil.getJdbcTemplate().update(sql, args);
    }
    
    class FiboLevelsExtractor implements ResultSetExtractor<List<FactorFiboLevel>> {
        @Override
        public List<FactorFiboLevel> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<FactorFiboLevel> levels = new ArrayList<FactorFiboLevel>();
                
                while(rs.next()) {
                    FactorFiboLevel level = new FactorFiboLevel();
                    
                    level.setFactorId(rs.getLong("FACTOR_ID"));
                    level.setLowLevel(rs.getBigDecimal("LEVEL_FROM"));
                    level.setHighLevel(rs.getBigDecimal("LEVEL_TO"));
                    
                    levels.add(level);
                }
                
                return levels;
        }
    }
}
