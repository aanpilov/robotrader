/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.service;

import com.robotrader.core.order.StopOrder;
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
     * @param boardCode
     * @param secCode
     * @param period
     * @param count
     * @throws Exception 
     */
    public void getHistory(String boardCode, String secCode, Period period, int count) throws Exception;
    
    /**
     * Подпискана изменения
     * @param boardCode
     * @param secCode
     * @throws Exception 
     */
    public void subscribe(String boardCode, String secCode) throws Exception;
    
    /**
     * Выставление стоп-заявки
     * @param order
     * @return
     * @throws Exception 
     */
    public void createStopOrder(StopOrder order) throws Exception;
}
