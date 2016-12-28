/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.io;

import com.robotrader.core.factor.Bar;
import com.robotrader.core.factor.Paper;
import com.robotrader.core.interval.Interval;
import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.StringTokenizer;

/**
 *
 * @author anpilov_av
 */
public class FinamFileBarSource implements Enumeration<Bar> {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private BufferedReader fileReader;
    private String readedLine;
    
    public FinamFileBarSource(String filePath) throws Exception {
        fileReader = new BufferedReader(new FileReader(filePath));        
    }
    
    @Override
    public boolean hasMoreElements() {
        try{
            readedLine = fileReader.readLine();
            return readedLine != null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Bar nextElement() {
        try{
            StringTokenizer tokenizer = new StringTokenizer(readedLine, ";");

            String dateStr = tokenizer.nextToken();
            tokenizer.nextToken();//time
            String openStr = tokenizer.nextToken();
            String highStr = tokenizer.nextToken();
            String lowStr = tokenizer.nextToken();
            String closeStr = tokenizer.nextToken();

            Paper paper = null;
            return new Bar(paper, sdf.parse(dateStr), Interval.ONE_MONTH, new BigDecimal(openStr), new BigDecimal(highStr), new BigDecimal(lowStr), new BigDecimal(closeStr), BigDecimal.ZERO);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static void main(String[] args) throws Exception {
        FinamFileBarSource barSource = new FinamFileBarSource("D:\\JavaProjects\\Strategy\\SBER.txt");
        while(barSource.hasMoreElements()) {
            Bar bar = barSource.nextElement();
            System.out.println("Bar: " + bar);
        }
    }    
}
