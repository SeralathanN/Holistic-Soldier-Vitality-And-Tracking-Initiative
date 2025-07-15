package com.holisticsoldier.app.service;

import com.holisticsoldier.app.model.SoldierData;
import com.holisticsoldier.app.repository.SoldierDataRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class PredictionService {

    @Value("${ml.api.url:http://localhost:5000/predict}")
    private String mlApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final SoldierDataRepository dataRepository;

    public PredictionService(SoldierDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public String predict(SoldierData data) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("atmosphericTemperature", data.getAtmosphericTemperature());
        payload.put("atmosphericHumidity", data.getAtmosphericHumidity());
        payload.put("bodyTemperature", data.getBodyTemperature());
        payload.put("heartRate", data.getHeartRate());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                mlApiUrl, HttpMethod.POST, request,
                new ParameterizedTypeReference<>() {}
            );
            return (String) response.getBody().get("message");

        } catch (Exception e) {
            return "Error contacting ML API: " + e.getMessage();
        }
    }

    public String predictLatestData() {
        Optional<SoldierData> latestData = dataRepository.findTopByOrderByTimestampDesc();
        if (latestData.isEmpty()) return "No data found.";
        return predict(latestData.get());
    }

    public String predictStoredData() {
        List<SoldierData> allData = dataRepository.findTop10ByOrderByTimestampDesc();
        if (allData.isEmpty()) return "No data found.";

        //int count = allData.size();
        SoldierData avg = new SoldierData();
        avg.setAtmosphericTemperature((float) allData.stream().mapToDouble(SoldierData::getAtmosphericTemperature).average().orElse(0));
        avg.setAtmosphericHumidity((float) allData.stream().mapToDouble(SoldierData::getAtmosphericHumidity).average().orElse(0));
        avg.setBodyTemperature((float) allData.stream().mapToDouble(SoldierData::getBodyTemperature).average().orElse(0));
        avg.setHeartRate((int) allData.stream().mapToInt(SoldierData::getHeartRate).average().orElse(0));

        return predict(avg);
    }
}
