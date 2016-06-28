/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.strategy;

/**
 *
 * @author aav
 */
public enum Advice {
    advBuy,
    advSell,
    advNothing,
    advAny;
    
    public Advice and(Advice other) {
        if(this == other || other == advAny){
            return this;
        } else if(this == advAny) {
            return other;
        } else {
            return advNothing;
        }
    }
    
    public Advice not() {
        if(this == advBuy) {
            return advSell;
        } else if(this == advSell) {
            return advBuy;
        } else {
            return advNothing;
        }
    }
    
    public Advice or(Advice other) {
        if(this == advNothing || this == advAny) {
            return other;
        }
        
        if(this == other || other == advNothing || other == advAny) {
            return this;
        } else {
            return advNothing;
        }
    }
    
    public static Advice map(int iAdvice) {
        if(iAdvice == 0) {
            return advNothing;
        } else if(iAdvice > 0) {
            return advBuy;
        } else {
            return advSell;
        }
    }
}
