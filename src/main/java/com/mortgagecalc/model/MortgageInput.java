package com.mortgagecalc.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class MortgageInput {
    @NotNull(message = "Credit score is required")
    @Min(value = 300, message = "Credit score must be at least 300")
    @Max(value = 850, message = "Credit score cannot exceed 850")
    private Integer creditScore;
    
    @NotNull(message = "Loan value is required")
    @Positive(message = "Loan value must be positive")
    private Double loanValue;
    
    @NotBlank(message = "State is required")
    @Size(min = 2, max = 2, message = "State must be a 2-letter code")
    private String state;
    
    @NotBlank(message = "Home type is required")
    private String homeType;
    
    @NotNull(message = "Property price is required")
    @Positive(message = "Property price must be positive")
    private Double propertyPrice;
    
    @NotNull(message = "Down payment is required")
    @Positive(message = "Down payment must be positive")
    private Double downPayment;
    
    @NotNull(message = "Income is required")
    @Positive(message = "Income must be positive")
    private Double income;
    
    @NotNull(message = "Points is required")
    private Double points;
    
    @NotNull(message = "Assets under management is required")
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
