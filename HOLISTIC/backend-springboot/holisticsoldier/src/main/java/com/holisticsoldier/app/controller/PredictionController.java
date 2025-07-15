package com.holisticsoldier.app.controller;

import com.holisticsoldier.app.service.PredictionService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/predict")
@CrossOrigin(origins = "*")
public class PredictionController {

    private final PredictionService predictionService;

    public PredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @GetMapping("/latest")
    public String predictLatest() {
        return predictionService.predictLatestData();
    }

    @GetMapping("/stored")
    public String predictStored() {
        return predictionService.predictStoredData();
    }
}
