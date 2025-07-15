package com.holisticsoldier.app.controller;

import com.holisticsoldier.app.model.SoldierData;
import com.holisticsoldier.app.service.SoldierDataService;
import com.holisticsoldier.app.service.LiveSensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
//import java.util.stream.Collectors;
//import com.holisticsoldier.app.service.PredictionService;
import java.util.List;

@RestController
@RequestMapping("/api/data")
@CrossOrigin(origins = "*")
public class SoldierDataController {

    @Autowired
    private SoldierDataService soldierDataService;

    @Autowired
    private LiveSensorService liveSensorService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    //@Autowired
   // private PredictionService predictionService;

    @PostMapping
    public SoldierData saveSoldierData(@RequestBody SoldierData soldierData) {
        SoldierData savedData = soldierDataService.saveSoldierData(soldierData);
        liveSensorService.updateLiveData(savedData);
        messagingTemplate.convertAndSend("/topic/soldierData", savedData);
        return savedData;
    }

    @GetMapping("/latest")
    public SoldierData getLatest() {
        SoldierData liveData = liveSensorService.getLatestLiveData();
        List<SoldierData> allData = soldierDataService.getAllSoldierData();
        return liveData != null ? liveData : (allData.isEmpty() ? null : allData.get(0));
    }

    @GetMapping
    public List<SoldierData> getAllSoldierData() {
        return soldierDataService.getAllSoldierData();
    }
}
