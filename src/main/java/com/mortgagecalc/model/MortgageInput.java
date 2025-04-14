package com.mortgagecalc.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class MortgageInput {
    @NotNull
    @Min(300)
    @Max(850)
    private Integer creditScore;
    
    @NotNull
    @Positive
    private Double loanValue;
    
    @NotBlank
    private String state;
    
    @NotBlank
    private String homeType;
    
    @NotNull
    @Positive
    private Double propertyPrice;
    
    @NotNull
    @Positive
    private Double downPayment;
    
    @NotNull
    @Positive
    private Double income;
    
    @NotNull
    private Double points = 0.0;
    
    @NotNull
    @Positive
    private Double assetsUnderManagement;

    public Integer getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(Integer creditScore) {
        this.creditScore = creditScore;
    }

    public Double getLoanValue() {
        return loanValue;
    }

    public void setLoanValue(Double loanValue) {
        this.loanValue = loanValue;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getHomeType() {
        return homeType;
    }

    public void setHomeType(String homeType) {
        this.homeType = homeType;
    }

    public Double getPropertyPrice() {
        return propertyPrice;
    }

    public void setPropertyPrice(Double propertyPrice) {
        this.propertyPrice = propertyPrice;
    }

    public Double getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(Double downPayment) {
        this.downPayment = downPayment;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public Double getAssetsUnderManagement() {
        return assetsUnderManagement;
    }

    public void setAssetsUnderManagement(Double assetsUnderManagement) {
        this.assetsUnderManagement = assetsUnderManagement;
    }
}
