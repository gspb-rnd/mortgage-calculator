package com.mortgagecalc.controller;

import com.mortgagecalc.model.MortgageInput;
import com.mortgagecalc.model.MortgageOption;
import com.mortgagecalc.service.RateCalculationService;
import com.mortgagecalc.service.ValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mortgage")
public class MortgageController {
    private final ValidationService validationService;
    private final RateCalculationService rateCalculationService;
    
    public MortgageController(ValidationService validationService, RateCalculationService rateCalculationService) {
        this.validationService = validationService;
        this.rateCalculationService = rateCalculationService;
    }
    
    @PostMapping("/calculate")
    public ResponseEntity<?> calculate(@RequestBody MortgageInput input) {
        Set<ConstraintViolation<MortgageInput>> violations = validationService.validateInput(input);
        
        if (!violations.isEmpty()) {
            List<String> errors = violations.stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        
        if (!validationService.isDownPaymentValid(input)) {
            return ResponseEntity.badRequest().body("Down payment cannot exceed property price");
        }
        
        List<MortgageOption> options = rateCalculationService.calculateMortgageOptions(input);
        
        return ResponseEntity.ok(options);
    }
}
