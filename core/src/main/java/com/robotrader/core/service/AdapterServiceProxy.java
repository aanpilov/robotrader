/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.service;

import com.robotrader.core.factor.Bar;
import com.robotrader.core.factor.Paper;
import com.robotrader.core.interval.Interval;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 *
 * @author aav
 */
public abstract class AdapterServiceProxy implements AdapterService {
    @Override
    public List<Bar> getArchiveBars(Paper paper, Interval interval, int lastCount, Date dateTo) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(dateTo);
        
        //Костыль для получения правильного количества баров на Interval.ONE_DAY
        if(interval == Interval.ONE_DAY) {
            //Запросим за 1 месяц Interval.ONE_DAY
            calendar.add(Interval.ONE_MONTH.getCalendarField(), -1);
            calendar = truncate(calendar, interval.getCalendarField());
            
            List<Bar> archiveBars = getArchiveBars(paper, interval, calendar.getTime(), dateTo);
            if(archiveBars.size() > lastCount) {
                //Вернулось больше - обрежем
                archiveBars = archiveBars.subList(archiveBars.size() - lastCount, archiveBars.size());
            }
            
            return archiveBars;
        }
        
        calendar.add(interval.getCalendarField(), -interval.getCalendarFieldsCount() * (lastCount - 1));
        calendar = truncate(calendar, interval.getCalendarField());
        
        List<Bar> archiveBars = getArchiveBars(paper, interval, calendar.getTime(), dateTo);
        
        /*@todo если в прошлом нет сделок - зависает поиск в истории
                 Надо придумать критерий глубины поиска
        if(archiveBars.size() < lastCount) {
            calendar.add(Calendar.MILLISECOND, -1);
            List<Bar> previosBars = getArchiveBars(paper, interval, lastCount - archiveBars.size(), calendar.getTime());
            previosBars.addAll(archiveBars);
            archiveBars = previosBars;
        }*/
        
        return archiveBars;
    }
    
    @Override
    public List<Bar> getArchiveBars(Paper paper, Interval interval, int lastCount) {
        Date currentDate = new Date();
        
        return getArchiveBars(paper, interval, lastCount, currentDate);
    }
    
    protected Calendar truncate(Calendar calendar, int field) {        
        int[] fields = new int[]{Calendar.YEAR, Calendar.MONTH, Calendar.WEEK_OF_MONTH, Calendar.DATE, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND};

        Calendar cal = (Calendar) calendar.clone();
        boolean truncate = false;

        for (int f : fields) {
            if (truncate) {
                cal.set(f, cal.getMinimum(f));
            }
            if (f == field) {
                truncate = true;
            }
        }
        return cal;
    }
}
