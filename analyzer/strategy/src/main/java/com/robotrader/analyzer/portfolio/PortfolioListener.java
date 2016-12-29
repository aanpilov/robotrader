/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.portfolio;

import eu.verdelhan.ta4j.Decimal;

/**
 *
 * @author aanpilov
 */
public interface PortfolioListener {
    public void positionChanged(Decimal position);
}
