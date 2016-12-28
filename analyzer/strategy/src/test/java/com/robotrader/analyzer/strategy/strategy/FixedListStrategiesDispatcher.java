/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.strategy.strategy;

import com.robotrader.adapter.util.ModuleApplicationContext;
import com.robotrader.analyzer.io.CsvPapersLoader;
import com.robotrader.core.factor.Paper;
import com.robotrader.core.service.AdapterService;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author aav
 */
public class FixedListStrategiesDispatcher {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = ModuleApplicationContext.getContext();
        AdapterService adapterService = (AdapterService) context.getBean("adapter");
        
        Set<Paper> target = CsvPapersLoader.load(new File("src/test/resources/portfolio/TARGET.csv"));
        Set<Paper> portfolio = CsvPapersLoader.load(new File("src/test/resources/portfolio/PORTFOLIO.csv"));
        
        ExecutorService executor = Executors.newFixedThreadPool(1);
        
        Set<Paper> myDivPares = new HashSet<>();
        myDivPares.addAll(portfolio);
        myDivPares.retainAll(target);
        
        Set<Paper> myOtherPares = new HashSet<>();
        myOtherPares.addAll(portfolio);
        myOtherPares.removeAll(myDivPares);
        
        Set<Paper> otherDivPares = new HashSet<>();
        otherDivPares.addAll(target);
        otherDivPares.removeAll(myDivPares);
        
        for (Paper paper : myOtherPares) {
            InvestmentStrategyManager manager = new InvestmentStrategyManager(paper, adapterService, true);
            executor.execute(manager);
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        
        System.out.println("DIVIDEND PARERS");
        executor = Executors.newFixedThreadPool(1);
        
        for (Paper paper : myDivPares) {
            InvestmentStrategyManager manager = new InvestmentStrategyManager(paper, adapterService, true);
            executor.execute(manager);
        }
        
        for (Paper paper : otherDivPares) {
            InvestmentStrategyManager manager = new InvestmentStrategyManager(paper, adapterService, false);
            executor.execute(manager);
        }
        
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
    }
    
}
