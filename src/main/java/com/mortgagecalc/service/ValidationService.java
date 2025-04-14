package com.mortgagecalc.service;

import com.mortgagecalc.model.MortgageInput;
import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;

@Service
public class ValidationService {
    private final Validator validator;

    public ValidationService(Validator validator) {
        this.validator = validator;
    }

    public Set<ConstraintViolation<MortgageInput>> validateInput(MortgageInput input) {
        return validator.validate(input);
    }

    public boolean isValid(MortgageInput input) {
        return validator.validate(input).isEmpty();
    }
    
    public boolean isDownPaymentValid(MortgageInput input) {
        return input.getDownPayment() <= input.getPropertyPrice();
    }
}
