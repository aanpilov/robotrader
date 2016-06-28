package com.robotrader.storage.adapter;

import com.robotrader.core.factor.Bar;
import com.robotrader.core.factor.FactorSetCollection;
import com.robotrader.core.interval.Interval;
import com.robotrader.core.service.AdapterService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ArchiveSynchonizatorTest {
    private ApplicationContext context;

	@Before
	public void setUp() throws Exception {
            context = new ClassPathXmlApplicationContext("spring-context.xml");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSynchronizeArchiveData() throws Exception {
            AdapterService adapter = (AdapterService) context.getBean("adapter");
            
            Set<String> paperHeaders = new HashSet<String>();
            paperHeaders.add("SBER");
            
            Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse("11/07/2014");
            List<Bar> archiveBars = adapter.getArchiveBars("SBER", Interval.ONE_DAY, startDate, null);
            System.out.println(archiveBars.size());
	}
}
