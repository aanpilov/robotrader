/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ta4j.strategy;

import eu.verdelhan.ta4j.TimeSeries;

/**
 *
 * @author aanpilov
 */
public interface Strategy {
    public TimeSeries getTimeSeries();
    public Advice getAdvice(int index);
}
