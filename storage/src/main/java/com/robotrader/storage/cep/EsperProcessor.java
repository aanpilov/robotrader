/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.storage.cep;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.robotrader.core.factor.FactorSet;
import com.robotrader.core.factor.OnlineFactor;
import com.robotrader.core.util.jaxb.JAXBUtil;
import com.robotrader.storage.adapter.FactorStorage;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author 1
 */
@Component(value = "esperProcessor")
public class EsperProcessor {
    private Logger log = Logger.getLogger(getClass());
    private EPServiceProvider serviceProvider;
    @Autowired
    private FactorStorage factorStorage;
    
    @PostConstruct
    public void init() {
        serviceProvider = EPServiceProviderManager.getDefaultProvider();
        String expression = "select header, "
                            + "first(date) as date, "
                            + "first(value) as openValue, "
                            + "last(value) as closeValue, "
                            + "min(value) as minValue, "
                            + "max(value) as maxValue "
                            + "from com.robotrader.core.factor.OnlineFactor.win:ext_timed_batch(date.getTime(), 1 min, 0L) where active = true group by header";
        EPStatement statement = serviceProvider.getEPAdministrator().createEPL(expression);
        IntervalFactorListener listener = new IntervalFactorListener(factorStorage);
        statement.addListener(listener);
    }
    
    public void sendOnlineFactors(FactorSet factorSet) {
        for(OnlineFactor onlineFactor : factorSet.getOnlineFactors().getFactors()) {
            serviceProvider.getEPRuntime().sendEvent(onlineFactor);
        }
    }
}
