/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.storage.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 *
 * @author 1
 */
public class FactorArchiveExtractor implements ResultSetExtractor<Set<FactorArchive>>{

    public Set<FactorArchive> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Set<FactorArchive> result = new HashSet<FactorArchive>();
        
        while(rs.next()) {
            FactorArchive factorArchive = new FactorArchive();
            factorArchive.setFactorId(rs.getLong("FACTOR_ID"));
            factorArchive.setStartDate(rs.getTimestamp("START_DATE"));
            factorArchive.setOpenPrice(rs.getBigDecimal("OPEN_PRICE"));
            factorArchive.setClosePrice(rs.getBigDecimal("CLOSE_PRICE"));
            factorArchive.setMinPrice(rs.getBigDecimal("MIN_PRICE"));
            factorArchive.setMaxPrice(rs.getBigDecimal("MAX_PRICE"));
            factorArchive.setVolume(rs.getLong("VOLUME"));
            
            result.add(factorArchive);
        }
        
        return result;
    }    
}
