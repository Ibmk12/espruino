package com.nihal.espruino.service;

import com.nihal.espruino.config.DataLoader;
import com.nihal.espruino.dto.CorrelationRequest;
import com.nihal.espruino.model.Measurement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MeasurementServiceImpl implements MeasurementService{

    @Autowired
    private DataLoader dataLoader;

    @Override
    public Double calculateAverageTemperature() {
        return dataLoader.loadData().stream()
                .mapToDouble(Measurement::getTemperature)
                .average()
                .orElse(Double.NaN);
    }

//    @Override
//    public List<Measurement> getVoltageTrends(String startDate, String endDate) {
//        LocalDate start = LocalDate.parse(startDate);
//        LocalDate end = LocalDate.parse(endDate);
//        return measurementRepository.findByTimestampBetween(start.atStartOfDay(), end.plusDays(1).atStartOfDay());
//    }

    @Override
    public Map<String, Object> analyzeBatteryLevels() {
        List<Measurement> measurements = dataLoader.loadData();
        double averageBattery = measurements.stream()
                .mapToInt(Measurement::getBattery)
                .average()
                .orElse(Double.NaN);

        Map<String, Object> analysis = new HashMap<>();
        analysis.put("averageBattery", averageBattery);
        analysis.put("batteryLevels", measurements.stream().collect(Collectors.groupingBy(Measurement::getBattery)));

        return analysis;
    }

    @Override
    public Map<String, Double> analyzeAcceleration() {
        List<Measurement> measurements = dataLoader.loadData();
        double averageAccX = measurements.stream().filter(m -> m.getAccel() != null).mapToDouble(m -> m.getAccel().getData().getAcc().getX()).average().orElse(Double.NaN);
        double averageAccY = measurements.stream().filter(m -> m.getAccel() != null).mapToDouble(m -> m.getAccel().getData().getAcc().getY()).average().orElse(Double.NaN);
        double averageAccZ = measurements.stream().filter(m -> m.getAccel() != null).mapToDouble(m -> m.getAccel().getData().getAcc().getZ()).average().orElse(Double.NaN);

        Map<String, Double> analysis = new HashMap<>();
        analysis.put("averageAccX", averageAccX);
        analysis.put("averageAccY", averageAccY);
        analysis.put("averageAccZ", averageAccZ);

        return analysis;
    }
//
//    @Override
//    public Map<String, Double> analyzeMagneticField() {
//        List<Measurement> measurements = dataLoader.loadData();
//        double averageMagX = measurements.stream().mapToDouble(m -> m.getMag().getData().getX()).average().orElse(Double.NaN);
//        double averageMagY = measurements.stream().mapToDouble(m ->  m.getMag().getData().getY()).average().orElse(Double.NaN);
//        double averageMagZ = measurements.stream().mapToDouble(m ->  m.getMag().getData().getZ()).average().orElse(Double.NaN);
//
//        Map<String, Double> analysis = new HashMap<>();
//        analysis.put("averageMagX", averageMagX);
//        analysis.put("averageMagY", averageMagY);
//        analysis.put("averageMagZ", averageMagZ);
//
//        return analysis;
//    }

    @Override
    public List<Measurement> filterMeasurements(Map<String, String> filters) {
        List<Measurement> measurements = dataLoader.loadData();
        if (filters.containsKey("startDate")) {
            LocalDate startDate = LocalDate.parse(filters.get("startDate"));
            measurements = measurements.stream()
                    .filter(m -> !m.getTimestamp().isBefore(startDate.atStartOfDay()))
                    .collect(Collectors.toList());
        }
        if (filters.containsKey("endDate")) {
            LocalDate endDate = LocalDate.parse(filters.get("endDate"));
            measurements = measurements.stream()
                    .filter(m -> !m.getTimestamp().isAfter(endDate.plusDays(1).atStartOfDay()))
                    .collect(Collectors.toList());
        }
        if (filters.containsKey("minTemperature")) {
            double minTemperature = Double.parseDouble(filters.get("minTemperature"));
            measurements = measurements.stream()
                    .filter(m -> m.getTemperature() >= minTemperature)
                    .collect(Collectors.toList());
        }
        if (filters.containsKey("maxTemperature")) {
            double maxTemperature = Double.parseDouble(filters.get("maxTemperature"));
            measurements = measurements.stream()
                    .filter(m -> m.getTemperature() <= maxTemperature)
                    .collect(Collectors.toList());
        }

        return measurements;
    }

    @Override
    public List<Measurement> getAllMeasurements() {
        return dataLoader.loadData();
    }

    @Override
    public Measurement getMeasurementById(Long id) {
        for (Measurement measurement : dataLoader.loadData()){
            if(measurement.getMeasurementNumber() == id)
                return measurement;
        };
        return new Measurement();
    }

    @Override
    public Map<Integer, Float> getTemperatureTrend() {
        List<Measurement> measurements = dataLoader.loadData();
        Map<Integer, Float> temperatureTrend = new LinkedHashMap<>();
        for (int i = 0; i < measurements.size(); i++) {
            temperatureTrend.put(i, measurements.get(i).getTemperature());
        }
        return temperatureTrend;
    }

    @Override
    public Map<Integer, Map<String, Float>> getAccelerationTrend() {
        List<Measurement> measurements = dataLoader.loadData();
        Map<Integer, Map<String, Float>> accelerationTrend = new LinkedHashMap<>();
        for (int i = 0; i < measurements.size(); i++) {
            Measurement measurement = measurements.get(i);
            if (measurement.getAccel() != null) {
                Map<String, Float> accelData = new HashMap<>();
                accelData.put("x", measurement.getAccel().getData().getAcc().getX());
                accelData.put("y", measurement.getAccel().getData().getAcc().getY());
                accelData.put("z", measurement.getAccel().getData().getAcc().getZ());
                accelerationTrend.put(i, accelData);
            }
        }
        return accelerationTrend;
    }

    @Override
    public List<Measurement> detectAnomalies(double temperatureThreshold) {
        List<Measurement> measurements = dataLoader.loadData();
        List<Measurement> anomalies = new ArrayList<>();
        for (Measurement measurement : measurements) {
            if (measurement.getTemperature() > temperatureThreshold) {
                anomalies.add(measurement);
            }
        }
        return anomalies;
    }

    @Override
    public double calculateCorrelation(CorrelationRequest request) {
        // Extract data for the given indicators from measurements
        List<Measurement> measurements = dataLoader.loadData();
        String indicator1 = request.getIndicator1();
        String indicator2 = request.getIndicator2();
        double[] dataIndicator1 = measurements.stream()
                .mapToDouble(measurement -> getIndicatorValue(measurement, indicator1))
                .toArray();
        double[] dataIndicator2 = measurements.stream()
                .mapToDouble(measurement -> getIndicatorValue(measurement, indicator2))
                .toArray();

        // Calculate means of each indicator
        double meanIndicator1 = calculateMean(dataIndicator1);
        double meanIndicator2 = calculateMean(dataIndicator2);

        // Calculate the numerator and denominator for the Pearson correlation coefficient
        double numerator = 0;
        double denom1 = 0;
        double denom2 = 0;

        for (int i = 0; i < measurements.size(); i++) {
            double deviation1 = dataIndicator1[i] - meanIndicator1;
            double deviation2 = dataIndicator2[i] - meanIndicator2;

            numerator += deviation1 * deviation2;
            denom1 += Math.pow(deviation1, 2);
            denom2 += Math.pow(deviation2, 2);
        }

        // Calculate the correlation coefficient
        double correlation;
        if (denom1 == 0 || denom2 == 0) {
            correlation = 0; // Return 0 if one of the indicators has no variability
        } else {
            correlation = numerator / (Math.sqrt(denom1) * Math.sqrt(denom2));
        }

        return correlation;
    }

    // Helper method to calculate the mean of an array of values
    private double calculateMean(double[] data) {
        double sum = 0;
        for (double value : data) {
            sum += value;
        }
        return sum / data.length;
    }

    // Helper method to get the value of a specific indicator from a Measurement object
    private double getIndicatorValue(Measurement measurement, String indicator) {
        switch (indicator) {
            case "temperature":
                return measurement.getTemperature();
            case "voltage":
                return measurement.getVoltage();
            case "lightLevel":
                return measurement.getLightLevel();
            case "battery":
                return measurement.getBattery();
            default:
                throw new IllegalArgumentException("Invalid indicator: " + indicator);
        }
    }
}
