/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.objects;

/**
 * Класс, описывающий торговый инструмент
 * @author aav
 */
public class Security {
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
    private String securityCode;

    public Security() {
    }

    public Security(String exchangeCode, String marketCode, String boardCode, String securityCode) {
        this.exchangeCode = exchangeCode;
        this.marketCode = marketCode;
        this.boardCode = boardCode;
        this.securityCode = securityCode;
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

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    @Override
    public String toString() {
        return "Paper{" + "exchangeCode=" + exchangeCode + ", marketCode=" + marketCode + ", boardCode=" + boardCode + ", securityCode=" + securityCode + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (this.exchangeCode != null ? this.exchangeCode.hashCode() : 0);
        hash = 71 * hash + (this.marketCode != null ? this.marketCode.hashCode() : 0);
        hash = 71 * hash + (this.boardCode != null ? this.boardCode.hashCode() : 0);
        hash = 71 * hash + (this.securityCode != null ? this.securityCode.hashCode() : 0);
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
        final Security other = (Security) obj;
        if ((this.exchangeCode == null) ? (other.exchangeCode != null) : !this.exchangeCode.equals(other.exchangeCode)) {
            return false;
        }
        if ((this.marketCode == null) ? (other.marketCode != null) : !this.marketCode.equals(other.marketCode)) {
            return false;
        }
        if ((this.boardCode == null) ? (other.boardCode != null) : !this.boardCode.equals(other.boardCode)) {
            return false;
        }
        if ((this.securityCode == null) ? (other.securityCode != null) : !this.securityCode.equals(other.securityCode)) {
            return false;
        }
        return true;
    }
}
