/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.event;

import com.robotrader.core.factor.FactorSet;
import java.util.EventObject;

/**
 *
 * @author 1
 */
public class FactorSetEvent extends EventObject {
    private FactorSet factorsCombination;
    private boolean archiveFactors;
    private String archivePeriodCode;
    
    public FactorSetEvent(Object source) {
        super(source);
    }

    public FactorSetEvent(FactorSet factorsCombination, String periodCode, Object source) {
        super(source);
        this.factorsCombination = factorsCombination;
        this.archivePeriodCode = periodCode;
    }

    public FactorSet getFactorsCombination() {
        return factorsCombination;
    }

    public boolean isArchiveFactors() {
        return archiveFactors;
    }

    public String getArchivePeriodCode() {
        return archivePeriodCode;
    }
}
