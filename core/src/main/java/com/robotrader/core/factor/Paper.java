/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.factor;

/**
 * Класс, описывающий торговый инструмент
 * @author aav
 */
public class Paper {
    /**
     * Код биржи 
     */
    private String exchangeCode;
    
    /**
     * Код рынка
     */
    private String marketCode;
    
    /**
     * Код торгового раздела
     */
    private String boardCode;
    
    /**
     * Торговый код инструмента
     */
    private String paperTradeCode;

    public Paper(String exchangeCode, String marketCode, String boardCode, String paperTradeCode) {
        this.exchangeCode = exchangeCode;
        this.marketCode = marketCode;
        this.boardCode = boardCode;
        this.paperTradeCode = paperTradeCode;
    }

    public String getExchangeCode() {
        return exchangeCode;
    }

    public void setExchangeCode(String exchangeCode) {
        this.exchangeCode = exchangeCode;
    }

    public String getMarketCode() {
        return marketCode;
    }

    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
    }

    public String getBoardCode() {
        return boardCode;
    }

    public void setBoardCode(String boardCode) {
        this.boardCode = boardCode;
    }

    public String getPaperTradeCode() {
        return paperTradeCode;
    }

    public void setPaperTradeCode(String paperTradeCode) {
        this.paperTradeCode = paperTradeCode;
    }

    @Override
    public String toString() {
        return "Paper{" + "exchangeCode=" + exchangeCode + ", marketCode=" + marketCode + ", boardCode=" + boardCode + ", paperTradeCode=" + paperTradeCode + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (this.exchangeCode != null ? this.exchangeCode.hashCode() : 0);
        hash = 71 * hash + (this.marketCode != null ? this.marketCode.hashCode() : 0);
        hash = 71 * hash + (this.boardCode != null ? this.boardCode.hashCode() : 0);
        hash = 71 * hash + (this.paperTradeCode != null ? this.paperTradeCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Paper other = (Paper) obj;
        if ((this.exchangeCode == null) ? (other.exchangeCode != null) : !this.exchangeCode.equals(other.exchangeCode)) {
            return false;
        }
        if ((this.marketCode == null) ? (other.marketCode != null) : !this.marketCode.equals(other.marketCode)) {
            return false;
        }
        if ((this.boardCode == null) ? (other.boardCode != null) : !this.boardCode.equals(other.boardCode)) {
            return false;
        }
        if ((this.paperTradeCode == null) ? (other.paperTradeCode != null) : !this.paperTradeCode.equals(other.paperTradeCode)) {
            return false;
        }
        return true;
    }
}
