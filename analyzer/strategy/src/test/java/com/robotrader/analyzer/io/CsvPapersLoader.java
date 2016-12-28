/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.io;

import au.com.bytecode.opencsv.CSVReader;
import com.robotrader.core.factor.Paper;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author aav
 */
public class CsvPapersLoader {
    public static Set<Paper> load(File file) throws Exception {
        Set<Paper> result = new HashSet<>();
        
        InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");        
        CSVReader csvReader = new CSVReader(isr, ',');
        
        String[] line;
        while ((line = csvReader.readNext()) != null) {
            if(line[0].startsWith("//")) continue;
            result.add(new Paper(line[0], line[1], line[2], line[3]));
        }
        
        return result;
    }
}
