/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.util.sql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 *
 * @author 1
 */
public class SqlUtilImpl extends JdbcDaoSupport implements SqlUtil {
    @Override
    public Object getFirstResult(String sql, Object[] args) throws Exception {
        List resultList = getJdbcTemplate().queryForList(sql, args);
        if (resultList == null || resultList.isEmpty()) {
            return null;
        } else {
            return resultList.get(0);
        }
    }

    @Override
    public boolean executeProcedure(String procedureCall, final Object[] args) throws Exception {
        CallableStatementCreator csCreator = getCallStatementCreator(procedureCall);
        CallableStatementCallback csCallback = new CallableStatementCallback() {

            @Override
            public Object doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
                if (args != null && args.length > 0) {
                    for (int i = 0; i < args.length; i++) {
                        Object arg = args[i];
                        if (instanceOfJavaDate(arg)) {
                            Date date = (Date) arg;
                            arg = new java.sql.Timestamp(date.getTime());
                        }
                        callableStatement.setObject(i + 1, arg);
                    }
                }
                return Boolean.valueOf(callableStatement.execute());
            }
        };
        return ((Boolean) getJdbcTemplate().execute(csCreator, csCallback)).booleanValue();
    }

    @Override
    public Object executeFunction(String functionCall, final Object[] args, final int resultType) throws Exception {        

        CallableStatementCreator csCreator = getCallStatementCreator(functionCall);
        CallableStatementCallback csCallback = new CallableStatementCallback() {

            @Override
            public Object doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
                callableStatement.registerOutParameter(1, resultType);
                setSqlArgs(callableStatement, args);
                callableStatement.execute();
                return callableStatement.getObject(1);
            }
        };
        return getJdbcTemplate().execute(csCreator, csCallback);
    }

    private void setSqlArgs(CallableStatement callableStatement, Object[] args) throws SQLException {
        if (args != null && args.length > 0) {
            for (int i = 1; i <= args.length; i++) {
                Object arg = args[i - 1];
                if (instanceOfJavaDate(arg) && !instanceOfSqlDate(arg) && !instanceOfTimestamp(arg)) {
                    Date date = (Date) arg;
                    arg = new java.sql.Date(date.getTime());
                }
                callableStatement.setObject(i + 1, arg);
            }
        }
    }

    private boolean instanceOfTimestamp(Object arg) {
        return arg instanceof Timestamp;
    }

    private boolean instanceOfJavaDate(Object arg) {
        return arg instanceof java.util.Date;
    }

    private boolean instanceOfSqlDate(Object arg) {
        return arg instanceof java.sql.Date;
    }

    private CallableStatementCreator getCallStatementCreator(final String functionCall) {
        return new CallableStatementCreator() {

            @Override
            public CallableStatement createCallableStatement(Connection connection) throws SQLException {
                return connection.prepareCall(functionCall);
            }
        };
    }

    /**
     * Добавляет строку с sql запросом знаками ? для каждого параметра
     * @param sql - строка с SQL-запросом
     * @param parameters - коллекция параметров
     * @return - String - параметризованная строка запроса
     */
    @Override
    public String appendSqlParams(String sql, List parameters) {
        StringBuilder result = new StringBuilder(sql);

        for (Iterator iter = parameters.iterator(); iter.hasNext();) {
            iter.next();
            result.append("?");
            if (iter.hasNext()) {
                result.append(", ");
            }
        }

        return result.toString();
    }

    @Override
    public String appendSqlParams(String sql, Object[] parameters) {
        List listParameters = Arrays.asList(parameters);
        return appendSqlParams(sql, listParameters);
    }
}
