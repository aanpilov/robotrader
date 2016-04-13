/*
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author aanpilov
 */
public class CorrectionLevelsCalculator {
    private static void calculateTLevels(double start, double v1, double v2, double v3, double v4) {
        SortedSet<Double> firstLevels=  new TreeSet<>();
        SortedSet<Double> thirdLevels=  new TreeSet<>();
        SortedSet<Double> fourthLevels=  new TreeSet<>();
        
        double firstMod = Math.abs(v1 - start);
        double thirdMod = Math.abs(v3 - start);
        double fourthMod = Math.abs(v4 - v3);
        
        double tDirection = 1;
        if(v4 < start) {
            tDirection = -1;
        }
        
        firstLevels.add(v4 + tDirection * firstMod * 1);
        
        System.out.println("firstLevels: " + firstLevels);
        
        thirdLevels.add(v4 + tDirection * thirdMod * 0.382);
        thirdLevels.add(v4 + tDirection * thirdMod * 0.618);
        thirdLevels.add(v4 + tDirection * thirdMod * 1);
        
        System.out.println("thirdLevels: " + thirdLevels);
        
        fourthLevels.add(v4 + tDirection * fourthMod * 1.27);
        fourthLevels.add(v4 + tDirection * fourthMod * 1.618);
        fourthLevels.add(v4 + tDirection * fourthMod * 2.618);
        
        System.out.println("fourthLevels: " + fourthLevels);
        
        nearestPoints(firstLevels, thirdLevels, fourthLevels);
    }
    
    private static void calculate(double start, double finish, double a, double b) {
        SortedSet<Double> retLevels=  new TreeSet<>();
        SortedSet<Double> aLevels=  new TreeSet<>();
        SortedSet<Double> bLevels=  new TreeSet<>();
        
        double tMod = Math.abs(finish - start);
        double aMod = Math.abs(finish - a);
        double bMod = Math.abs(b - a);
        
        double tDirection = 1;
        if(finish < start) {
            tDirection = -1;
        }
        
        retLevels.add(finish - tDirection * tMod * 0.382);
        retLevels.add(finish - tDirection * tMod * 0.5);
        retLevels.add(finish - tDirection * tMod * 0.618);
        retLevels.add(finish - tDirection * tMod * 0.786);
        
        aLevels.add(b - tDirection * aMod * 0.618);
        aLevels.add(b - tDirection * aMod);
        aLevels.add(b - tDirection * aMod * 1.618);
        
        bLevels.add(b - tDirection * bMod * 1.270);
        bLevels.add(b - tDirection * bMod * 1.618);
        bLevels.add(b - tDirection * bMod * 2.618);
        
        nearestPoints(retLevels, aLevels, bLevels);        
    }
    
    private static void nearestPoints(Set<Double> set1, Set<Double> set2, Set<Double> set3) {
        Map<Double, double[]> result = new TreeMap<>();
        
        for(double value1 : set1) {
            for(double value2 : set2) {
                for(double value3 : set3) {
                    double maxDiff = Math.abs(value1 - value2);
                    
                    if(Math.abs(value1 - value3) > maxDiff) {
                        maxDiff = Math.abs(value1 - value3);
                    }
                    
                    if(Math.abs(value2 - value3) > maxDiff) {
                        maxDiff = Math.abs(value2 - value3);
                    }
                    
                    result.put(new Double(maxDiff), new double[]{value1, value2, value3});
                }
            }
        }
        
        int counter = 0;
        for(Double diff : result.keySet()) {            
            Arrays.sort(result.get(diff));
            System.out.println("Diff: " + diff + " for [" + Arrays.toString(result.get(diff)) + "]");
            
            counter++;
            if(counter == 2) {
                break;
            }
        }
        System.out.println("");
    }
    
    public static void main(String[] args) {
        calculate(106, 119.39, 115.82, 119.08);        
//        calculateTLevels(85.96, 74.59, 80.63, 66.7, 69.49);
    }
}
