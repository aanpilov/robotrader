/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ta4j.analysis.criteria;

import eu.verdelhan.ta4j.Order;
import java.util.List;

/**
 *
 * @author aanpilov
 */
public class ClearProfitCriterion {

    public double calculate(List<Order> orders) {
        return GrossProfitCriterion.calculate(orders) - GrossLossCriterion.calculate(orders);
    }
}
