package com.mortgagecalc.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CsvUtility {
    private final Map<String, List<RatePoint>> mortgageRates = new HashMap<>();
    
    @PostConstruct
    public void init() {
        loadCsvData("fixed_30", "csv/fixed_30.csv");
        loadCsvData("fixed_15", "csv/fixed_15.csv");
        loadCsvData("arm_5_1", "csv/arm_5_1.csv");
        loadCsvData("arm_7_1", "csv/arm_7_1.csv");
    }
    
    private void loadCsvData(String mortgageType, String resourcePath) {
        List<RatePoint> ratePoints = new ArrayList<>();
        
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);
             CSVReader reader = new CSVReader(new InputStreamReader(is))) {
            
            reader.readNext();
            
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length >= 2) {
                    double points = Double.parseDouble(line[0]);
                    double rate = Double.parseDouble(line[1]);
                    ratePoints.add(new RatePoint(points, rate));
                }
            }
            
            mortgageRates.put(mortgageType, ratePoints);
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Error loading CSV data for " + mortgageType, e);
        }
    }
    
    public List<RatePoint> getRatePoints(String mortgageType) {
        return mortgageRates.getOrDefault(mortgageType, new ArrayList<>());
    }
    
    public static class RatePoint {
        private final double points;
        private final double rate;
        
        public RatePoint(double points, double rate) {
            this.points = points;
            this.rate = rate;
        }
        
        public double getPoints() {
            return points;
        }
        
        public double getRate() {
            return rate;
        }
    }
}
