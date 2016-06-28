package com.robotrader.storage.entities;

import com.robotrader.core.util.sql.SqlUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class FactorHeaderDao {
    private SqlUtil sqlUtil;
    
    private Map<String, FactorHeader> codeMapping = new HashMap<String, FactorHeader>();
    private Map<Long, FactorHeader> idMapping = new HashMap<Long, FactorHeader>();

    public void setSqlUtil(SqlUtil sqlUtil) {
        this.sqlUtil = sqlUtil;
    }

    public FactorHeader findByCode(String code) throws Exception {
        FactorHeader factorHeader = codeMapping.get(code);

        if(factorHeader == null) {
            String findByCodeQuery = "select * from robotrader.factor where code = ?";
            Object[] params = new Object[] {code};

            List<FactorHeader> factorHeaderList = selectFactorSet(findByCodeQuery, params);
            if (!factorHeaderList.isEmpty()) {
                factorHeader = factorHeaderList.get(0);
            }
            
            cache(factorHeader);
        }

        return factorHeader;
    }
    
    public FactorHeader findById(Long id) throws Exception {
        FactorHeader factorHeader = idMapping.get(id);

        if(factorHeader == null) {
            String findByCodeQuery = "select * from robotrader.factor where id = ?";
            Object[] params = new Object[] {id};

            List<FactorHeader> factorHeaderList = selectFactorSet(findByCodeQuery, params);
            if (!factorHeaderList.isEmpty()) {
                factorHeader = factorHeaderList.get(0);
            }
            
            cache(factorHeader);
        }

        return factorHeader;
    }
    
    public List<FactorHeader> listPapers() throws Exception {
        String findByCodeQuery = "select * from robotrader.factor where is_paper = ?";
        
        Object[] params = new Object[]{Boolean.TRUE};
        
        List<FactorHeader> headers = selectFactorSet(findByCodeQuery, params);
        
        cache(headers);
        
        return headers;
    }

    private List<FactorHeader> selectFactorSet(String sql, Object[] parameters) throws Exception {
        return sqlUtil.getJdbcTemplate().query(sql, parameters, new ResultSetExtractor<List<FactorHeader>>() {
            public List<FactorHeader> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<FactorHeader> result = new ArrayList<FactorHeader>();
                
                while(rs.next()) {
                    FactorHeader factorHeader = new FactorHeader();
                    
                    Long id = rs.getLong("ID");
                    String code = rs.getString("CODE");
                    String name = rs.getString("NAME");
                    Boolean isPaper = rs.getBoolean("IS_PAPER");
                    Long scale = (rs.getObject("SCALE") == null)? 0: rs.getLong("SCALE");
                    
                    factorHeader.setId(id);
                    factorHeader.setCode(code);
                    factorHeader.setName(name);
                    factorHeader.setPaper(isPaper);
                    factorHeader.setScale(scale);
                    
                    result.add(factorHeader);
                }
                
                return result;
            }
        });
    }
    
    private void cache(FactorHeader header) {
        codeMapping.put(header.getCode(), header);
        idMapping.put(header.getId(), header);
    }

    private void cache(List<FactorHeader> headers) {
        for (Iterator<FactorHeader> iter = headers.iterator(); iter.hasNext();) {
            FactorHeader factorHeader = iter.next();
            cache(factorHeader);
        }
    }
}
