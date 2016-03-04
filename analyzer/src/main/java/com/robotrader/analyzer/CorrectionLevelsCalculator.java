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
        
        retLevels.add(b - tDirection * tMod * 0.382);
        retLevels.add(b - tDirection * tMod * 0.5);
        retLevels.add(b - tDirection * tMod * 0.618);
        retLevels.add(b - tDirection * tMod * 0.786);
        
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
        calculate(109.2, 105.4, 107.8, 106.7);        
    }
}
