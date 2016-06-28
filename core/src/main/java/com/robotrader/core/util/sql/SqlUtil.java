/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.util.sql;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author 1
 */
public interface SqlUtil {
    public String appendSqlParams(String sql, List parameters);
    public String appendSqlParams(String sql, Object[] parameters);
    public boolean executeProcedure(String procedureCall, final Object[] args) throws Exception;
    public Object executeFunction(String functionCall, final Object[] args, final int resultType) throws Exception;
    public Object getFirstResult(String sql, Object[] args) throws Exception;
    public JdbcTemplate getJdbcTemplate();
}
