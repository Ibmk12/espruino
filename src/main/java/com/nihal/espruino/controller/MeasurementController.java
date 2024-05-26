package com.nihal.espruino.controller;

import com.nihal.espruino.dto.CorrelationRequest;
import com.nihal.espruino.model.Measurement;
import com.nihal.espruino.service.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/measurements")
public class MeasurementController {

    @Autowired
    private MeasurementService measurementService;

    @GetMapping("/averageTemperature")
    public Double getAverageTemperature() {
        return measurementService.calculateAverageTemperature();
    }

//    @GetMapping("/voltage-trends")
//    public List<Measurement> getVoltageTrends(@RequestParam String startDate, @RequestParam String endDate) {
//        return measurementService.getVoltageTrends(startDate, endDate);
//    }

    @GetMapping("/batteryLevelAnalysis")
    public Map<String, Object> analyzeBatteryLevels() {
        return measurementService.analyzeBatteryLevels();
    }

    @GetMapping("/accelerationAnalysis")
    public Map<String, Double> analyzeAcceleration() {
        return measurementService.analyzeAcceleration();
    }
//
//    @GetMapping("/magnetic-field-analysis")
//    public Map<String, Double> analyzeMagneticField() {
//        return measurementService.analyzeMagneticField();
//    }

    @GetMapping("/filter")
    public List<Measurement> filterMeasurements(@RequestParam Map<String, String> filters) {
        return measurementService.filterMeasurements(filters);
    }

    @GetMapping
    public List<Measurement> getAllMeasurements() {
        return measurementService.getAllMeasurements();
    }

    @GetMapping("/{id}")
    public Measurement getMeasurementById(@PathVariable Long id) {
        return measurementService.getMeasurementById(id);
    }

    @GetMapping("/temperatureTrend")
    public Map<Integer, Float> getTemperatureTrend() {
        return measurementService.getTemperatureTrend();
    }

    @GetMapping("/accelerationTrend")
    Map<Integer, Map<String, Float>> getAccelerationTrend() {
        return measurementService.getAccelerationTrend();
    }

    @GetMapping("/detectAnomalies/{temp}")
    public List<Measurement> detectAnomalies(@PathVariable Float temp){
        return measurementService.detectAnomalies(temp);
    }

    @GetMapping("/calculateCorrelation")
    public double calculateCorrelation(@RequestBody CorrelationRequest correlationRequest){
        return measurementService.calculateCorrelation(correlationRequest);
    }
}

