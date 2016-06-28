package com.robotrader.storage.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ModuleApplicationContext {
	private static ApplicationContext instance;
	
	public static ApplicationContext getInstance() {
		if(instance == null) {
			instance = new ClassPathXmlApplicationContext("storageContext.xml");
		}
		
		return instance;
	}
}
