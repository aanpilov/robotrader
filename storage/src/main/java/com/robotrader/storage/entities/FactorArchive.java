/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.storage.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author 1
 */
@Entity
@Table(schema = "robotrader", name = "FACTOR_ARCHIVE")
public class FactorArchive implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name="FACTOR_ID")
    private Long factorId;
    
    @Column(name="START_DATE")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date startDate;
    
    @Column(name="OPEN_PRICE")
    private BigDecimal openPrice;
    
    @Column(name="CLOSE_PRICE")
    private BigDecimal closePrice;
    
    @Column(name="MIN_PRICE")
    private BigDecimal minPrice;
    
    @Column(name="MAX_PRICE")
    private BigDecimal maxPrice;
    
    @Column(name="VOLUME")
    private Long volume;

    public FactorArchive() {
    }

    public BigDecimal getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(BigDecimal closePrice) {
        this.closePrice = closePrice;
    }

    public Long getFactorId() {
        return factorId;
    }

    public void setFactorId(Long factorId) {
        this.factorId = factorId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(BigDecimal openPrice) {
        this.openPrice = openPrice;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "FactorArchive{" + "id=" + id + ", factorId=" + factorId + ", startDate=" + startDate + ", openPrice=" + openPrice + ", closePrice=" + closePrice + ", minPrice=" + minPrice + ", maxPrice=" + maxPrice + ", volume=" + volume + '}';
    }
}
