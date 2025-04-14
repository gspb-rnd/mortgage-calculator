package com.mortgagecalc.service;

import com.mortgagecalc.model.MortgageInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import jakarta.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ValidationServiceTest {
    
    private ValidationService validationService;
    private LocalValidatorFactoryBean validator;
    
    @BeforeEach
    void setUp() {
        validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        validationService = new ValidationService(validator);
    }
    
    @Test
    void testValidateInput_ValidInput() {
        MortgageInput input = createValidInput();
        
        Set<ConstraintViolation<MortgageInput>> violations = validationService.validateInput(input);
        
        assertTrue(violations.isEmpty());
        assertTrue(validationService.isValid(input));
    }
    
    @Test
    void testValidateInput_InvalidInput() {
        MortgageInput input = new MortgageInput();
        
        Set<ConstraintViolation<MortgageInput>> violations = validationService.validateInput(input);
        
        assertFalse(violations.isEmpty());
        assertFalse(validationService.isValid(input));
    }
    
    @Test
    void testIsDownPaymentValid_Valid() {
        MortgageInput input = createValidInput();
        input.setPropertyPrice(500000.0);
        input.setDownPayment(100000.0);
        
        assertTrue(validationService.isDownPaymentValid(input));
    }
    
    @Test
    void testIsDownPaymentValid_Invalid() {
        MortgageInput input = createValidInput();
        input.setPropertyPrice(500000.0);
        input.setDownPayment(600000.0);
        
        assertFalse(validationService.isDownPaymentValid(input));
    }
    
    @Test
    void testIsDownPaymentValid_Equal() {
        MortgageInput input = createValidInput();
        input.setPropertyPrice(500000.0);
        input.setDownPayment(500000.0);
        
        assertTrue(validationService.isDownPaymentValid(input));
    }
    
    private MortgageInput createValidInput() {
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
        return input;
    }
}
