/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.strategy.strategy;

import com.robotrader.adapter.util.ModuleApplicationContext;
import com.robotrader.core.factor.Paper;
import com.robotrader.core.service.AdapterService;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author anpilov_av
 */
public class StrategiesDispatcher {

    private static final Set<Paper> NON_LIQUID = new HashSet<>();
    private static final Set<Paper> MY_PAPERS = new HashSet<>();

    static {
        //Неликвид
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "ZHIV"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "PLSM"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "RLMNP"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "SAGO"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "BISV"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "URKZ"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "RU000A0JNUN9"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "KRKN"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "SELL"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "DZRD"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "VJGZP"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "YKEN"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "JNOSP"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "RTSBP"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "JNOS"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "VRSBP"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "MRSB"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "ROLO"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "DZRDP"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "MOTZ"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "ARSG"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "WTCM"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "ZMZN"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "VJGZ"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "SAGOP"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "KAZT"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "MASZ"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "DALM"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "NSVZ"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "RZSB"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "OSMP"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "SELGP"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "OMSH"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "CHEP"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "ASSB"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "MSST"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "MAGE"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "RKKE"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "HIMCP"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "KMEZ"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "SZPR"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "IGST"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "TAER"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "MOBB"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "NFAZ"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "VTRS"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "VSYD"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "KUZB"));

        //В позиции
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "RUAL"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "DVEC"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "TGKN"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "MRKC"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "ENRU"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "GRAZ"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "RASP"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "QIWI"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "MRKK"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "MRKP"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "VZRZP"));
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext context = ModuleApplicationContext.getContext();
        AdapterService adapterService = (AdapterService) context.getBean("adapter");
        
        Set<Paper> papers = adapterService.listPapers("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST");

        //Очистка
        papers.removeAll(NON_LIQUID);
        papers.removeAll(MY_PAPERS);
        
        //TODO: RestEasy multithreading
        ExecutorService executor = Executors.newFixedThreadPool(1);
        
        //Сначала свои
        for (Paper paper : MY_PAPERS) {
            InvestmentStrategyManager manager = new InvestmentStrategyManager(paper, adapterService, true);
            executor.execute(manager);
        }

        for (Paper paper : papers) {
            InvestmentStrategyManager manager = new InvestmentStrategyManager(paper, adapterService, false);
            executor.execute(manager);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
    }
}
