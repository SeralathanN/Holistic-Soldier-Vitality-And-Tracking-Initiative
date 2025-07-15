package com.holisticsoldier.app.service;

import com.holisticsoldier.app.model.SoldierData;
import com.holisticsoldier.app.repository.SoldierDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SoldierDataService {

    @Autowired
    private SoldierDataRepository soldierDataRepository;

    public SoldierData saveSoldierData(SoldierData soldierData) {
        return soldierDataRepository.save(soldierData);
    }

    public List<SoldierData> getAllSoldierData() {
        return soldierDataRepository.findAll();
    }

    public List<SoldierData> getDataByDateRange(LocalDateTime start, LocalDateTime end) {
        return soldierDataRepository.findByTimestampBetween(start, end);
    }

    public List<SoldierData> getRecentData(int days) {
        LocalDateTime date = LocalDateTime.now().minusDays(days);
        return soldierDataRepository.findByTimestampAfter(date);
    }
    
    public SoldierData getLatestData() {
        List<SoldierData> allData = getAllSoldierData();
        return allData.isEmpty() ? null : allData.get(allData.size() - 1); // or customize logic
    }
}
