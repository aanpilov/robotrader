/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ta4j.analysis.criteria;

import eu.verdelhan.ta4j.Order;
import java.util.Collection;

/**
 *
 * @author aanpilov
 */
public class GrossProfitCriterion {
    public static double calculate(Collection<Order> orders) {
        return orders.stream()
               .filter(order -> order.isSell())
                .mapToDouble(order -> order.getAmount().multipliedBy(order.getPrice()).toDouble()).sum();
    }
}
