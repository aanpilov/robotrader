/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.trader;

import com.robotrader.analyzer.chart.ChartManager;
import com.robotrader.analyzer.portfolio.Portfolio;
import com.robotrader.analyzer.portfolio.PortfolioListener;
import com.robotrader.analyzer.strategy.StrategyListener;
import com.robotrader.analyzer.strategy.StrategyManager;
import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Tick;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ta4j.strategy.Advice;

/**
 *
 * @author aav
 */
public abstract class AbstractStrategyTrader implements StrategyListener, PortfolioListener {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected final Portfolio portfolio;
    protected final ChartManager chartManager;

    public AbstractStrategyTrader(Portfolio portfolio, ChartManager chartManager, StrategyManager strategyManager) {
        this.portfolio = portfolio;
        this.chartManager = chartManager;
        
        strategyManager.addStrategyListener(this);
        portfolio.addPortfolioListener(this);
    }

    @Override
    public void onAdvice(Tick tick, Advice advice) {
        Optional<Tick> tickOpt = chartManager.getTickStartedAt(tick.getBeginTime());
        if(!tickOpt.isPresent()) {
            log.warn("TradeTick empty on base: " + tick + " Advice: " + advice);
        }
        Tick tradeTick = tickOpt.get();

        try {
            if(portfolio.getPosition().isZero()) {
                removeActiveOrders();
            }
            
            if (portfolio.getPosition().isPositive() && advice == Advice.EXIT_LONG) {
                closePosition(tradeTick);
            }

            if (portfolio.getPosition().isNegative() && advice == Advice.EXIT_SHORT) {
                closePosition(tradeTick);
            }

            if (!portfolio.getPosition().isPositive() && advice == Advice.ENTER_LONG) {
                openPosition(tradeTick, Decimal.ONE);
            }

            if (!portfolio.getPosition().isNegative() && advice == Advice.ENTER_SHORT) {
                openPosition(tradeTick, Decimal.valueOf(-1));
            }

            if (portfolio.getPosition().isPositive() && advice == Advice.REDUCE_LONG) {
                reducePosition(tradeTick);
            }

            if (portfolio.getPosition().isNegative() && advice == Advice.REDUCE_SHORT) {
                reducePosition(tradeTick);
            }
        } catch (Exception e) {
            log.error("Processing error", e);
            return;
        }
    }

    protected abstract void closePosition(Tick tick) throws Exception;

    protected abstract void openPosition(Tick tick, Decimal requiredPositionSign) throws Exception;

    protected abstract void reducePosition(Tick tick) throws Exception;

    protected abstract void removeActiveOrders() throws Exception;        
}
