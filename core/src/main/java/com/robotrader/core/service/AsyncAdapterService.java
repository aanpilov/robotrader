/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.service;

import com.robotrader.core.objects.ConditionalOrder;
import com.robotrader.core.objects.Security;
import org.joda.time.Period;

/**
 * Интерфейс асинхронного адаптера
 * @author aav
 */
public interface AsyncAdapterService {
    /**
     * Запуск адаптера
     * @throws Exception 
     */
    public void start() throws Exception;
    
    /**
     * Останов адаптера
     * @throws Exception 
     */
    public void stop() throws Exception;
    
    /**
     * Получение исторических данных
     * @param security
     * @param period
     * @param count
     * @throws Exception 
     */
    public void getHistory(Security security, Period period, int count) throws Exception;
    
    /**
     * Подпискана изменения
     * @param security     
     * @throws Exception 
     */
    public void subscribe(Security security) throws Exception;
    
    /**
     * Выставление условной заявки
     * @param order
     * @return
     * @throws Exception 
     */
    public void createConditionalOrder(ConditionalOrder order) throws Exception;
}
