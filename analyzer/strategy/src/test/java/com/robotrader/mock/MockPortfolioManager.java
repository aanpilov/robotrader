/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.mock;

import com.robotrader.analyzer.portfolio.Portfolio;
import com.robotrader.analyzer.portfolio.PortfolioManager;
import com.robotrader.core.objects.Security;

/**
 *
 * @author aav
 */
public class MockPortfolioManager implements PortfolioManager {
    private Portfolio portfolio = new Portfolio();
    
    @Override
    public Portfolio getSecurityPortfolio(Security security) {
        return portfolio;
    }
    
}
