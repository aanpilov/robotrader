/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.factor;

import java.util.Calendar;

/**
 * @author 1
 * 
 * Периоды, используемые в архиве котировок
 */
public enum Interval {
    ONE_MINUTE(0, "1_M", Calendar.MINUTE, 1, "0 0/1 * * * ?", "robotrader.factor_archive"),
    FIVE_MINUTES(1, "5_M", Calendar.MINUTE, 5, "0 0/5 * * * ?", "robotrader.factor_archive_5_minute"),
    TEN_MINUTES(2, "10_M", Calendar.MINUTE, 10, "0 0/10 * * * ?", "robotrader.factor_archive_10_minute"),
    FIFTEEN_MINUTES(3, "15_M", Calendar.MINUTE, 15,  "0 0/15 * * * ?", "robotrader.factor_archive_15_minute"),
    THIRTY_MINUTES(4, "30_M", Calendar.MINUTE, 30, "0 0/30 * * * ?", "robotrader.factor_archive_30_minute"),
    ONE_HOUR(5, "1_H", Calendar.HOUR_OF_DAY, 1,  "0 0 0/1 * * ?", "table_not_set"),
    ONE_DAY(6, "1_D", Calendar.DATE, 1, "0 0 0/1 * * ?", "table_not_set"),
    ONE_WEEK(7, "1_W", Calendar.WEEK_OF_MONTH, 1, "0 0 0/1 * * ?", "table_not_set"),
    ONE_MONTH(8, "1_MN", Calendar.MONTH, 1, "0 0 0/1 * * ?", "table_not_set"),
    ONE_YEAR(9, "1_Y", Calendar.YEAR, 1, "0 0 0/1 * * ?", "table_not_set");
    
    private final int constant;
    private final String code;
    private final int calendarField;
    private final int calendarFieldsCount;    
    private final String cronExpression;
    private final String archiveTableName;

    private Interval(int constant, String code, int calendarField, int calendarFieldsCount, String cronExpression, String archiveTableName) {
        this.constant = constant; 
        this.code = code;
        this.calendarField = calendarField;
        this.calendarFieldsCount = calendarFieldsCount;
        this.cronExpression = cronExpression;
        this.archiveTableName = archiveTableName;
    }

    public int getConstant() {
        return constant;
    }

    public String getCode() {
        return code;
    }

    public int getCalendarField() {
        return calendarField;
    }

    public int getCalendarFieldsCount() {
        return calendarFieldsCount;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public String getArchiveTableName() {
        return archiveTableName;
    }
}
