/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.factor;

import java.util.Date;

/**
 *
 * @author 1
 */
public interface Factor {
    public Paper getPaper();
    public Date getDate();
    public boolean isInterval();
}
