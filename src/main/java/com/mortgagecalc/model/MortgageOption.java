package com.mortgagecalc.model;

import java.util.ArrayList;
import java.util.List;

public class MortgageOption {
    private String mortgageType;
    private double rate;
    private double points;
    private double apr;
    private List<String> appliedRules = new ArrayList<>();
    
    public String getMortgageType() {
        return mortgageType;
    }
    
    public void setMortgageType(String mortgageType) {
        this.mortgageType = mortgageType;
    }
    
    public double getRate() {
        return rate;
    }
    
    public void setRate(double rate) {
        this.rate = rate;
    }
    
    public double getPoints() {
        return points;
    }
    
    public void setPoints(double points) {
        this.points = points;
    }
    
    public double getApr() {
        return apr;
    }
    
    public void setApr(double apr) {
        this.apr = apr;
    }
    
    public List<String> getAppliedRules() {
        return appliedRules;
    }
    
    public void setAppliedRules(List<String> appliedRules) {
        this.appliedRules = appliedRules;
    }
    
    public void addAppliedRule(String rule) {
        this.appliedRules.add(rule);
    }
}
