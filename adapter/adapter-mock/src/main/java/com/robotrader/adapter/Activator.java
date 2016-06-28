package com.robotrader.adapter;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
    private AdapterStockThread stockThread = null;
    
    public void start(BundleContext context) throws Exception {
        stockThread = new AdapterStockThread();
        stockThread.start();
    }

    public void stop(BundleContext context) throws Exception {
        stockThread.setActive(false);
        stockThread.join();
    }

}
