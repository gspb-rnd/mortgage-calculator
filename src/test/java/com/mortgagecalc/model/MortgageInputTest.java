package com.mortgagecalc.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import jakarta.validation.ConstraintViolation;

import static org.junit.jupiter.api.Assertions.*;

class MortgageInputTest {
    
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();
    
    @Test
    void testValidMortgageInput() {
        MortgageInput input = new MortgageInput();
        input.setCreditScore(750);
        input.setLoanValue(400000.0);
        input.setState("CA");
        input.setHomeType("Single Family");
        input.setPropertyPrice(500000.0);
        input.setDownPayment(100000.0);
        input.setIncome(120000.0);
        input.setPoints(0.0);
        input.setAssetsUnderManagement(200000.0);
        
        Set<ConstraintViolation<MortgageInput>> violations = validator.validate(input);
        
        assertTrue(violations.isEmpty());
    }
    
    @ParameterizedTest
    @ValueSource(ints = {299, 851})
    void testInvalidCreditScore(int creditScore) {
        MortgageInput input = new MortgageInput();
        input.setCreditScore(creditScore);
        input.setLoanValue(400000.0);
        input.setState("CA");
        input.setHomeType("Single Family");
        input.setPropertyPrice(500000.0);
        input.setDownPayment(100000.0);
        input.setIncome(120000.0);
        input.setPoints(0.0);
        input.setAssetsUnderManagement(200000.0);
        
        Set<ConstraintViolation<MortgageInput>> violations = validator.validate(input);
        
        assertFalse(violations.isEmpty());
    }
    
    @Test
    void testNegativeLoanValue() {
        MortgageInput input = new MortgageInput();
        input.setCreditScore(750);
        input.setLoanValue(-400000.0);
        input.setState("CA");
        input.setHomeType("Single Family");
        input.setPropertyPrice(500000.0);
        input.setDownPayment(100000.0);
        input.setIncome(120000.0);
        input.setPoints(0.0);
        input.setAssetsUnderManagement(200000.0);
        
        Set<ConstraintViolation<MortgageInput>> violations = validator.validate(input);
        
        assertFalse(violations.isEmpty());
    }
    
    @Test
    void testMissingRequiredFields() {
        MortgageInput input = new MortgageInput();
        
        Set<ConstraintViolation<MortgageInput>> violations = validator.validate(input);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.size() > 1);
    }
    
    @Test
    void testGettersAndSetters() {
        MortgageInput input = new MortgageInput();
        
        input.setCreditScore(750);
        input.setLoanValue(400000.0);
        input.setState("CA");
        input.setHomeType("Single Family");
        input.setPropertyPrice(500000.0);
        input.setDownPayment(100000.0);
        input.setIncome(120000.0);
        input.setPoints(0.5);
        input.setAssetsUnderManagement(200000.0);
        
        assertEquals(750, input.getCreditScore());
        assertEquals(400000.0, input.getLoanValue());
        assertEquals("CA", input.getState());
        assertEquals("Single Family", input.getHomeType());
        assertEquals(500000.0, input.getPropertyPrice());
        assertEquals(100000.0, input.getDownPayment());
        assertEquals(120000.0, input.getIncome());
        assertEquals(0.5, input.getPoints());
        assertEquals(200000.0, input.getAssetsUnderManagement());
    }
}
