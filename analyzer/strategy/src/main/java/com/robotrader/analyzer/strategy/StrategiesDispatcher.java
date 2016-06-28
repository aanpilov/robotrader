/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.strategy;

import com.robotrader.adapter.util.ModuleApplicationContext;
import com.robotrader.analyzer.strategy.strategy.InvestmentStrategyWithPosition;
import com.robotrader.analyzer.strategy.strategy.InvestmentStrategyWithoutPosition;
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
        //Р СњР ВµР В»Р С‘Р С”Р Р†Р С‘Р Т‘
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
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "PRMB"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "TASBP"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "BISVP"));
        NON_LIQUID.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "KOGK"));

        //Р вЂ™ Р С—Р С•Р В·Р С‘РЎвЂ Р С‘Р С‘
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "DVEC"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "RASP"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "MRKP"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "AMEZ"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "AQUA"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "RLMN"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "CNTLP"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "AVAZ"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "SELG"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "STSBP"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "GRAZ"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "UTAR"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "MSTT"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "DIOD"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "GCHE"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "PHST"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "TRMK"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "SYNG"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "LSRG"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "EONR"));
        MY_PAPERS.add(new Paper("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST", "FESH"));
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext context = ModuleApplicationContext.getContext();
        AdapterService adapterService = (AdapterService) context.getBean("adapter");
        
        Set<Paper> papers = adapterService.listPapers("MICEX", "MICEX_SHR_T", "MCXT_SHR_LST");

        //Р С›РЎвЂЎР С‘РЎРѓРЎвЂљР С”Р В°
        papers.removeAll(NON_LIQUID);
        papers.removeAll(MY_PAPERS);
        
        //TODO: RestEasy multithreading
        ExecutorService executor = Executors.newFixedThreadPool(1);
        
        //Р РЋР Р…Р В°РЎвЂЎР В°Р В»Р В° РЎРѓР Р†Р С•Р С‘
        for (Paper paper : MY_PAPERS) {
            InvestmentStrategyWithPosition strategy = new InvestmentStrategyWithPosition(paper, adapterService);
            executor.execute(strategy);
        }

        for (Paper paper : papers) {
            InvestmentStrategyWithoutPosition strategy = new InvestmentStrategyWithoutPosition(paper, adapterService);
            executor.execute(strategy);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
    }
}
