package com.mortgagecalc.service;

import com.mortgagecalc.model.MortgageInput;
import com.mortgagecalc.model.MortgageOption;
import com.mortgagecalc.utils.CsvUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RateCalculationServiceTest {
    
    @Mock
    private CsvUtility csvUtility;
    
    private RateCalculationService rateCalculationService;
    
    @BeforeEach
    void setUp() {
        rateCalculationService = new RateCalculationService(csvUtility);
        
        setupMockRatePoints("fixed_30");
        setupMockRatePoints("fixed_15");
        setupMockRatePoints("arm_5_1");
        setupMockRatePoints("arm_7_1");
    }
    
    @Test
    void testCalculateMortgageOptions_StandardCase() {
        MortgageInput input = createStandardInput();
        
        List<MortgageOption> options = rateCalculationService.calculateMortgageOptions(input);
        
        assertNotNull(options);
        assertEquals(4, options.size());
        
        assertEquals("30-Year Fixed", options.get(0).getMortgageType());
        assertEquals("15-Year Fixed", options.get(1).getMortgageType());
        assertEquals("5/1 ARM", options.get(2).getMortgageType());
        assertEquals("7/1 ARM", options.get(3).getMortgageType());
    }
    
    @Test
    void testCalculateMortgageOptions_SmallLoanRule() {
        MortgageInput input = createStandardInput();
        input.setLoanValue(400000.0); // Below $500,000 threshold
        
        List<MortgageOption> options = rateCalculationService.calculateMortgageOptions(input);
        
        assertNotNull(options);
        assertEquals(4, options.size());
        
        for (MortgageOption option : options) {
            assertEquals(8.0, option.getRate()); // Base rate 7.0 + 1.0
            assertTrue(option.getAppliedRules().contains("Small Loan Amount (< $500,000): +1.00% to rate"));
        }
    }
    
    @Test
    void testCalculateMortgageOptions_HighAumRule() {
        MortgageInput input = createStandardInput();
        input.setAssetsUnderManagement(15000000.0); // Above $10,000,000 threshold
        
        List<MortgageOption> options = rateCalculationService.calculateMortgageOptions(input);
        
        assertNotNull(options);
        assertEquals(4, options.size());
        
        for (MortgageOption option : options) {
            assertEquals(6.75, option.getRate()); // Base rate 7.0 - 0.25
            assertTrue(option.getAppliedRules().contains("High Assets Under Management (> $10,000,000): -0.25% to rate"));
        }
    }
    
    @Test
    void testCalculateMortgageOptions_NewYorkStateRule() {
        MortgageInput input = createStandardInput();
        input.setState("NY"); // New York state
        
        List<MortgageOption> options = rateCalculationService.calculateMortgageOptions(input);
        
        assertNotNull(options);
        assertEquals(4, options.size());
        
        for (MortgageOption option : options) {
            assertEquals(0.25, option.getPoints()); // Base points 0.0 + 0.25
            assertTrue(option.getAppliedRules().contains("New York State: +0.25 points"));
        }
    }
    
    @Test
    void testCalculateMortgageOptions_MultipleRules() {
        MortgageInput input = createStandardInput();
        input.setLoanValue(400000.0); // Below $500,000 threshold
        input.setAssetsUnderManagement(15000000.0); // Above $10,000,000 threshold
        input.setState("NY"); // New York state
        
        List<MortgageOption> options = rateCalculationService.calculateMortgageOptions(input);
        
        assertNotNull(options);
        assertEquals(4, options.size());
        
        for (MortgageOption option : options) {
            assertEquals(7.75, option.getRate()); // Base rate 7.0 + 1.0 - 0.25
            assertEquals(0.25, option.getPoints()); // Base points 0.0 + 0.25
            assertEquals(3, option.getAppliedRules().size());
            assertTrue(option.getAppliedRules().contains("Small Loan Amount (< $500,000): +1.00% to rate"));
            assertTrue(option.getAppliedRules().contains("High Assets Under Management (> $10,000,000): -0.25% to rate"));
            assertTrue(option.getAppliedRules().contains("New York State: +0.25 points"));
        }
    }
    
    private void setupMockRatePoints(String mortgageType) {
        List<CsvUtility.RatePoint> ratePoints = new ArrayList<>();
        ratePoints.add(new CsvUtility.RatePoint(-1.0, 7.5));
        ratePoints.add(new CsvUtility.RatePoint(-0.5, 7.2));
        ratePoints.add(new CsvUtility.RatePoint(0.0, 7.0));
        ratePoints.add(new CsvUtility.RatePoint(0.5, 6.8));
        ratePoints.add(new CsvUtility.RatePoint(1.0, 6.6));
        
        when(csvUtility.getRatePoints(eq(mortgageType))).thenReturn(ratePoints);
    }
    
    private MortgageInput createStandardInput() {
        MortgageInput input = new MortgageInput();
        input.setCreditScore(750);
        input.setLoanValue(600000.0); // Above small loan threshold
        input.setState("CA"); // Not NY
        input.setHomeType("Single Family");
        input.setPropertyPrice(750000.0);
        input.setDownPayment(150000.0);
        input.setIncome(150000.0);
        input.setPoints(0.0);
        input.setAssetsUnderManagement(5000000.0); // Below high AUM threshold
        return input;
    }
}
