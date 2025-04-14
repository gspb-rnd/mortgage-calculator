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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
    void testCalculate_ValidationErrors() {
        Set<ConstraintViolation<MortgageInput>> violations = new HashSet<>();
        ConstraintViolation<MortgageInput> violation = createMockViolation("creditScore", "must be between 300 and 850");
        violations.add(violation);
        
        when(validationService.validateInput(any(MortgageInput.class))).thenReturn(violations);
        
        ResponseEntity<?> response = mortgageController.calculate(validInput);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof List);
        
        @SuppressWarnings("unchecked")
        List<String> errors = (List<String>) response.getBody();
        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("creditScore"));
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
    
    @SuppressWarnings("unchecked")
    private ConstraintViolation<MortgageInput> createMockViolation(String propertyPath, String message) {
        return new ConstraintViolation<MortgageInput>() {
            @Override
            public String getMessage() {
                return message;
            }
            
            @Override
            public String getMessageTemplate() {
                return message;
            }
            
            @Override
            public MortgageInput getRootBean() {
                return null;
            }
            
            @Override
            public Class<MortgageInput> getRootBeanClass() {
                return null;
            }
            
            @Override
            public Object getLeafBean() {
                return null;
            }
            
            @Override
            public Object[] getExecutableParameters() {
                return new Object[0];
            }
            
            @Override
            public Object getExecutableReturnValue() {
                return null;
            }
            
            @Override
            public jakarta.validation.Path getPropertyPath() {
                return new jakarta.validation.Path() {
                    @Override
                    public Iterator iterator() {
                        return new Iterator() {
                            private boolean hasNext = true;
                            
                            @Override
                            public boolean hasNext() {
                                return hasNext;
                            }
                            
                            @Override
                            public Node next() {
                                hasNext = false;
                                return new Node() {
                                    @Override
                                    public String getName() {
                                        return propertyPath;
                                    }
                                    
                                    @Override
                                    public boolean isInIterable() {
                                        return false;
                                    }
                                    
                                    @Override
                                    public Integer getIndex() {
                                        return null;
                                    }
                                    
                                    @Override
                                    public Object getKey() {
                                        return null;
                                    }
                                    
                                    @Override
                                    public ElementKind getKind() {
                                        return ElementKind.PROPERTY;
                                    }
                                    
                                    @Override
                                    public <T extends Node> T as(Class<T> nodeType) {
                                        return null;
                                    }
                                };
                            }
                        };
                    }
                };
            }
            
            @Override
            public jakarta.validation.ConstraintDescriptor<?> getConstraintDescriptor() {
                return null;
            }
            
            @Override
            public Object getInvalidValue() {
                return null;
            }
            
            @Override
            public Object unwrap(Class type) {
                return null;
            }
        };
    }
}
