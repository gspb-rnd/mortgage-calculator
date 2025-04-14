package com.mortgagecalc.model;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MortgageOptionTest {
    
    @Test
    void testGettersAndSetters() {
        MortgageOption option = new MortgageOption();
        
        option.setMortgageType("30-Year Fixed");
        option.setRate(6.5);
        option.setPoints(0.5);
        option.setApr(6.75);
        List<String> rules = Arrays.asList("Rule 1", "Rule 2");
        option.setAppliedRules(rules);
        
        assertEquals("30-Year Fixed", option.getMortgageType());
        assertEquals(6.5, option.getRate());
        assertEquals(0.5, option.getPoints());
        assertEquals(6.75, option.getApr());
        assertEquals(2, option.getAppliedRules().size());
        assertEquals("Rule 1", option.getAppliedRules().get(0));
        assertEquals("Rule 2", option.getAppliedRules().get(1));
    }
    
    @Test
    void testAddAppliedRule() {
        MortgageOption option = new MortgageOption();
        
        option.addAppliedRule("Rule 1");
        option.addAppliedRule("Rule 2");
        
        assertEquals(2, option.getAppliedRules().size());
        assertEquals("Rule 1", option.getAppliedRules().get(0));
        assertEquals("Rule 2", option.getAppliedRules().get(1));
    }
}
