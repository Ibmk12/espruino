package com.nihal.espruino.service;

import com.nihal.espruino.dto.CorrelationRequest;
import com.nihal.espruino.model.Measurement;

import java.util.List;
import java.util.Map;

 public interface MeasurementService {

     Double calculateAverageTemperature();
//     List<Measurement> getVoltageTrends(String startDate, String endDate);
     Map<String, Object> analyzeBatteryLevels();
     Map<String, Double> analyzeAcceleration();
//     Map<String, Double> analyzeMagneticField();
     List<Measurement> filterMeasurements(Map<String, String> filters);
     List<Measurement> getAllMeasurements();
     Measurement getMeasurementById(Long id);
     Map<Integer, Float> getTemperatureTrend();
     Map<Integer, Map<String, Float>> getAccelerationTrend();
     List<Measurement> detectAnomalies(double temperatureThreshold);
     double calculateCorrelation(CorrelationRequest request);
}
