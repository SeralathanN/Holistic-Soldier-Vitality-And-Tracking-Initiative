package com.holisticsoldier.app.service;

import com.holisticsoldier.app.model.SoldierData;
import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class LiveSensorService {
    private final AtomicReference<SoldierData> latestLiveData = new AtomicReference<>();

    public void updateLiveData(SoldierData data) {
        latestLiveData.set(data);
    }

    public SoldierData getLatestLiveData() {
        return latestLiveData.get();
    }
}
