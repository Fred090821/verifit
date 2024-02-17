package com.gym.members.verifit.repository;

import com.gym.members.verifit.entity.StreakMetricsRecord;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * repository dedicated to save StreakMetrics records to database
 */
@Repository
public interface StreakMetricsRepository extends JpaRepository<StreakMetricsRecord, Long> {

  //Retrieve the most recent streakMetrics record for a given memberId from the StreakMetricsRecord table.
  Optional<StreakMetricsRecord> findFirstByMemberIdOrderByLastActiveWeekDesc(Long memberId);
}