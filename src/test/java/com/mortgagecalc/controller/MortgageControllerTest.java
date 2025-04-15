package com.mortgagecalc.controller;

import com.mortgagecalc.model.MortgageInput;
import com.mortgagecalc.model.MortgageOption;
import com.mortgagecalc.service.RateCalculationService;
import com.mortgagecalc.service.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.mockito.Mockito;

@ExtendWith(MockitoExtension.class)
class MortgageControllerTest {
    
    @Mock
    private ValidationService validationService;
    
    @Mock
    private RateCalculationService rateCalculationService;
    
    @InjectMocks
    private MortgageController mortgageController;
    
    private MortgageInput validInput;
    private List<MortgageOption> mortgageOptions;
    
    @BeforeEach
    void setUp() {
        validInput = new MortgageInput();
        validInput.setCreditScore(750);
        validInput.setLoanValue(400000.0);
        validInput.setState("CA");
        validInput.setHomeType("Single Family");
        validInput.setPropertyPrice(500000.0);
        validInput.setDownPayment(100000.0);
        validInput.setIncome(120000.0);
        validInput.setPoints(0.0);
        validInput.setAssetsUnderManagement(200000.0);
        
        mortgageOptions = new ArrayList<>();
        MortgageOption option1 = new MortgageOption();
        option1.setMortgageType("30-Year Fixed");
        option1.setRate(6.5);
        option1.setPoints(0.0);
        option1.setApr(6.75);
        
        MortgageOption option2 = new MortgageOption();
        option2.setMortgageType("15-Year Fixed");
        option2.setRate(6.0);
        option2.setPoints(0.0);
        option2.setApr(6.25);
        
        mortgageOptions.add(option1);
        mortgageOptions.add(option2);
    }
    
    @Test
    void testCalculate_ValidInput() {
        Set<ConstraintViolation<MortgageInput>> emptyViolations = new HashSet<>();
        when(validationService.validateInput(any(MortgageInput.class))).thenReturn(emptyViolations);
        when(validationService.isDownPaymentValid(any(MortgageInput.class))).thenReturn(true);
        when(rateCalculationService.calculateMortgageOptions(any(MortgageInput.class))).thenReturn(mortgageOptions);
        
        ResponseEntity<?> response = mortgageController.calculate(validInput);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof List);
        
        @SuppressWarnings("unchecked")
        List<MortgageOption> responseOptions = (List<MortgageOption>) response.getBody();
        assertEquals(2, responseOptions.size());
        assertEquals("30-Year Fixed", responseOptions.get(0).getMortgageType());
        assertEquals("15-Year Fixed", responseOptions.get(1).getMortgageType());
    }
    
    @Test
    void testCalculate_InvalidDownPayment() {
        Set<ConstraintViolation<MortgageInput>> emptyViolations = new HashSet<>();
        when(validationService.validateInput(any(MortgageInput.class))).thenReturn(emptyViolations);
        when(validationService.isDownPaymentValid(any(MortgageInput.class))).thenReturn(false);
        
        ResponseEntity<?> response = mortgageController.calculate(validInput);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Down payment cannot exceed property price", response.getBody());
    }
}
