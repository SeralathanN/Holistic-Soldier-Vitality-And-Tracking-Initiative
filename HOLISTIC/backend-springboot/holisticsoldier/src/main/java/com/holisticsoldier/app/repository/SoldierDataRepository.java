package com.holisticsoldier.app.repository;

import com.holisticsoldier.app.model.SoldierData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SoldierDataRepository extends JpaRepository<SoldierData, Long> {
    
    List<SoldierData> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<SoldierData> findByTimestampAfter(LocalDateTime date);
    
    @Query(value = "SELECT * FROM soldier_data ORDER BY timestamp DESC LIMIT 1", nativeQuery = true)
    SoldierData findTop1ByOrderByTimestampDesc();
    
    Optional<SoldierData> findTopByOrderByTimestampDesc();
    List<SoldierData> findTop10ByOrderByTimestampDesc();
}
