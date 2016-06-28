/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.storage.adapter;

import com.robotrader.core.factor.FactorSet;
import com.robotrader.core.factor.FactorSetCollection;
import com.robotrader.core.interval.Interval;
import com.robotrader.core.service.AdapterService;
import com.robotrader.storage.entities.FactorDao;
import com.robotrader.storage.entities.FactorHeader;
import com.robotrader.storage.entities.FactorHeaderDao;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author 1
 */
public class ArchiveSynchonizator {
    private Logger log = Logger.getLogger(this.getClass());
    
    private AdapterService adapterService;
    private FactorStorage factorStorage;
    private FactorHeaderDao factorHeaderDao;
    private FactorDao factorDao;

    public void setAdapterService(AdapterService adapterService) {
        this.adapterService = adapterService;
    }

    public void setFactorStorage(FactorStorage factorStorage) {
        this.factorStorage = factorStorage;
    }

    public void setFactorHeaderDao(FactorHeaderDao factorHeaderDao) {
        this.factorHeaderDao = factorHeaderDao;
    }

    public void setFactorDao(FactorDao factorDao) {
        this.factorDao = factorDao;
    }
    
    public void synchronizeArchiveData() throws Exception {
        log.debug("synchronizeArchiveData() start...");
        Set<String> paperHeaders = listPaperFactorHeaders();
        
        Date lastArchiveStartDate = factorDao.getLastArchiveStartDate();
        log.debug("lastArchiveStartDate: " + lastArchiveStartDate);
        
        log.debug("receiving archive history...");
        FactorSetCollection archiveHistory = getArchiveHistory(paperHeaders, Interval.ONE_MINUTE, lastArchiveStartDate, null);
        log.debug("archive history received: ");
        
        saveArchiveHistory(archiveHistory);
    }

    private Set<String> listPaperFactorHeaders() throws Exception {
        List<FactorHeader> paperFactorsList = factorHeaderDao.listPapers();
        
        Set<String> factorHeaders = new HashSet<String>();
        for (Iterator<FactorHeader> iter = paperFactorsList.iterator(); iter.hasNext();) {
            FactorHeader factorHeader = iter.next();
            factorHeaders.add(factorHeader.getCode());
        }
        
        return factorHeaders;
    }

    private FactorSetCollection getArchiveHistory(Set<String> paperHeaders, Interval interval, Date startDate, Date endDate) {
        return adapterService.getArchiveFactorSetCollection(paperHeaders, interval, startDate, endDate);
    }

    private void saveArchiveHistory(FactorSetCollection archiveHistory) throws Exception {
        for (Iterator<FactorSet> iter = archiveHistory.getCollection().iterator(); iter.hasNext();) {
            FactorSet factorSet = iter.next();
            factorStorage.importFactorSet(factorSet);
        }
    }
}
