/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.storage.adapter;

import com.robotrader.core.factor.FactorSet;
import com.robotrader.core.process.FactorSetProcessor;
import com.robotrader.core.process.FactorSetProducer;
import com.robotrader.core.util.jaxb.JAXBUtil;
import java.util.HashSet;
import java.util.Set;
import org.springframework.jms.core.JmsTemplate;

/**
 *
 * @author 1
 */
public class OnlineFactorChangeHandler {    
    private FactorStorage factorStorage;
    private JAXBUtil jaxbUtil;
    private JmsTemplate jmsTemplate;
    
    private static final String FACTOR_ONLINE_TARGET = "FACTOR.ONLINE";
    
    private Set<FactorSetProducer> factorSetProducers = new HashSet<FactorSetProducer>();
    private Set<FactorSetProcessor> factorSetProcessors = new HashSet<FactorSetProcessor>();

    public void setFactorStorage(FactorStorage factorStorage) {
        this.factorStorage = factorStorage;
    }

    public void setJaxbUtil(JAXBUtil jaxbUtil) {
        this.jaxbUtil = jaxbUtil;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setFactorSetProducers(Set<FactorSetProducer> factorSetProducers) {
        this.factorSetProducers = factorSetProducers;
    }

    public void setFactorSetProcessors(Set<FactorSetProcessor> factorSetProcessors) {
        this.factorSetProcessors = factorSetProcessors;
    }
    
    public void onChange() throws Exception {
        FactorSet activeFactorSet = factorStorage.getActiveFactors();

        if (activeFactorSet.getIntervalFactors().size() > 0) {

            FactorSet producedFactorSet = invocateProducers();
            activeFactorSet.add(producedFactorSet);

            activeFactorSet = invocateProcessors(activeFactorSet);

            String factorSetStr = jaxbUtil.marshallObject(activeFactorSet);
            jmsTemplate.convertAndSend(FACTOR_ONLINE_TARGET, factorSetStr);
        }
    }

    private FactorSet invocateProducers() throws Exception {
        FactorSet result = new FactorSet();
        
        for(FactorSetProducer producer : factorSetProducers) {
            FactorSet producedFactorSet = producer.produce();
            result.add(producedFactorSet);
        }
        
        return result;
    }
    
    private FactorSet invocateProcessors(FactorSet factorSet) throws Exception {
        FactorSet result = new FactorSet();
        result.add(factorSet);
        
        for(FactorSetProcessor processor : factorSetProcessors) {
            result = processor.process(result);
        }
        
        return result;
    }
}
