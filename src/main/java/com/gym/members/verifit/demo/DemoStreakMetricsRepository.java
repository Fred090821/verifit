package com.gym.members.verifit.demo;

import com.gym.members.verifit.entity.StreakMetricsRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DemoStreakMetricsRepository extends JpaRepository<StreakMetricsRecord, Long> {

  @Modifying
  @Query(value = "TRUNCATE TABLE \"attendance_record\" ", nativeQuery = true)
  void emptyStreakMetricsRecord();
}