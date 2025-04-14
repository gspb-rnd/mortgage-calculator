package com.mortgagecalc.service;

import com.mortgagecalc.model.MortgageInput;
import com.mortgagecalc.model.MortgageOption;
import com.mortgagecalc.utils.CsvUtility;
import com.mortgagecalc.utils.CsvUtility.RatePoint;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RateCalculationService {
    private final CsvUtility csvUtility;
    
    private static final double SMALL_LOAN_THRESHOLD = 500000.0;
    private static final double SMALL_LOAN_RATE_INCREASE = 1.0;
    
    private static final double HIGH_AUM_THRESHOLD = 10000000.0;
    private static final double HIGH_AUM_RATE_DECREASE = 0.25;
    
    private static final String NEW_YORK_STATE = "NY";
    private static final double NEW_YORK_POINTS_INCREASE = 0.25;
    
    public RateCalculationService(CsvUtility csvUtility) {
        this.csvUtility = csvUtility;
    }
    
    public List<MortgageOption> calculateMortgageOptions(MortgageInput input) {
        List<MortgageOption> options = new ArrayList<>();
        
        options.add(calculateOption("fixed_30", "30-Year Fixed", input, 30));
        options.add(calculateOption("fixed_15", "15-Year Fixed", input, 15));
        options.add(calculateOption("arm_5_1", "5/1 ARM", input, 30));
        options.add(calculateOption("arm_7_1", "7/1 ARM", input, 30));
        
        return options;
    }
    
    private MortgageOption calculateOption(String rateKey, String mortgageType, MortgageInput input, int loanTermYears) {
        MortgageOption option = new MortgageOption();
        option.setMortgageType(mortgageType);
        
        RatePoint baseRatePoint = findClosestRatePoint(rateKey, input.getPoints());
        
        double rate = baseRatePoint.getRate();
        double points = baseRatePoint.getPoints();
        
        rate = applyRateRules(rate, input, option);
        points = applyPointsRules(points, input, option);
        
        option.setRate(rate);
        option.setPoints(points);
        
        double fees = (points / 100) * input.getLoanValue();
        double totalInterest = calculateTotalInterest(input.getLoanValue(), rate, loanTermYears);
        double apr = (totalInterest + fees) / input.getLoanValue() / loanTermYears;
        
        option.setApr(apr);
        
        return option;
    }
    
    private RatePoint findClosestRatePoint(String mortgageType, double requestedPoints) {
        List<RatePoint> ratePoints = csvUtility.getRatePoints(mortgageType);
        
        if (ratePoints.isEmpty()) {
            throw new IllegalStateException("No rate data available for " + mortgageType);
        }
        
        RatePoint closest = ratePoints.get(0);
        double minDiff = Math.abs(closest.getPoints() - requestedPoints);
        
        for (RatePoint rp : ratePoints) {
            double diff = Math.abs(rp.getPoints() - requestedPoints);
            if (diff < minDiff) {
                minDiff = diff;
                closest = rp;
            }
        }
        
        return closest;
    }
    
    private double applyRateRules(double rate, MortgageInput input, MortgageOption option) {
        if (input.getLoanValue() < SMALL_LOAN_THRESHOLD) {
            rate += SMALL_LOAN_RATE_INCREASE;
            option.addAppliedRule("Small Loan Amount (< $500,000): +1.00% to rate");
        }
        
        if (input.getAssetsUnderManagement() > HIGH_AUM_THRESHOLD) {
            rate -= HIGH_AUM_RATE_DECREASE;
            option.addAppliedRule("High Assets Under Management (> $10,000,000): -0.25% to rate");
        }
        
        return rate;
    }
    
    private double applyPointsRules(double points, MortgageInput input, MortgageOption option) {
        if (NEW_YORK_STATE.equals(input.getState())) {
            points += NEW_YORK_POINTS_INCREASE;
            option.addAppliedRule("New York State: +0.25 points");
        }
        
        return points;
    }
    
    private double calculateTotalInterest(double principal, double annualRate, int years) {
        return principal * (annualRate / 100) * years;
    }
}
