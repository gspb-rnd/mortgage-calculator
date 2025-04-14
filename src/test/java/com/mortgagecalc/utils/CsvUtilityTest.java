package com.mortgagecalc.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvUtilityTest {
    
    private CsvUtility csvUtility;
    
    @BeforeEach
    void setUp() throws IOException {
        createTestCsvFile("csv/fixed_30.csv", "Points,Rate\n-1.0,7.5\n-0.5,7.2\n0.0,7.0\n0.5,6.8\n1.0,6.6");
        createTestCsvFile("csv/fixed_15.csv", "Points,Rate\n-1.0,6.8\n-0.5,6.5\n0.0,6.3\n0.5,6.1\n1.0,5.9");
        createTestCsvFile("csv/arm_5_1.csv", "Points,Rate\n-1.0,6.5\n-0.5,6.2\n0.0,6.0\n0.5,5.8\n1.0,5.6");
        createTestCsvFile("csv/arm_7_1.csv", "Points,Rate\n-1.0,6.7\n-0.5,6.4\n0.0,6.2\n0.5,6.0\n1.0,5.8");
        
        csvUtility = new CsvUtility();
        csvUtility.init();
    }
    
    @Test
    void testGetRatePoints_Fixed30() {
        List<CsvUtility.RatePoint> ratePoints = csvUtility.getRatePoints("fixed_30");
        
        assertNotNull(ratePoints);
        assertEquals(5, ratePoints.size());
        assertEquals(-1.0, ratePoints.get(0).getPoints());
        assertEquals(7.5, ratePoints.get(0).getRate());
        assertEquals(1.0, ratePoints.get(4).getPoints());
        assertEquals(6.6, ratePoints.get(4).getRate());
    }
    
    @Test
    void testGetRatePoints_Fixed15() {
        List<CsvUtility.RatePoint> ratePoints = csvUtility.getRatePoints("fixed_15");
        
        assertNotNull(ratePoints);
        assertEquals(5, ratePoints.size());
        assertEquals(-1.0, ratePoints.get(0).getPoints());
        assertEquals(6.8, ratePoints.get(0).getRate());
    }
    
    @Test
    void testGetRatePoints_Arm51() {
        List<CsvUtility.RatePoint> ratePoints = csvUtility.getRatePoints("arm_5_1");
        
        assertNotNull(ratePoints);
        assertEquals(5, ratePoints.size());
        assertEquals(-1.0, ratePoints.get(0).getPoints());
        assertEquals(6.5, ratePoints.get(0).getRate());
    }
    
    @Test
    void testGetRatePoints_Arm71() {
        List<CsvUtility.RatePoint> ratePoints = csvUtility.getRatePoints("arm_7_1");
        
        assertNotNull(ratePoints);
        assertEquals(5, ratePoints.size());
        assertEquals(-1.0, ratePoints.get(0).getPoints());
        assertEquals(6.7, ratePoints.get(0).getRate());
    }
    
    @Test
    void testGetRatePoints_NonExistentType() {
        List<CsvUtility.RatePoint> ratePoints = csvUtility.getRatePoints("non_existent");
        
        assertNotNull(ratePoints);
        assertTrue(ratePoints.isEmpty());
    }
    
    @Test
    void testRatePointClass() {
        CsvUtility.RatePoint ratePoint = new CsvUtility.RatePoint(0.5, 6.8);
        
        assertEquals(0.5, ratePoint.getPoints());
        assertEquals(6.8, ratePoint.getRate());
    }
    
    private void createTestCsvFile(String resourcePath, String content) throws IOException {
        Path tempFile = Files.createTempFile("test", ".csv");
        Files.writeString(tempFile, content);
        
        Resource resource = new ClassPathResource(resourcePath);
        try (InputStream is = Files.newInputStream(tempFile)) {
            Files.copy(is, resource.getFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Could not create test CSV file in classpath: " + e.getMessage());
        }
    }
}
