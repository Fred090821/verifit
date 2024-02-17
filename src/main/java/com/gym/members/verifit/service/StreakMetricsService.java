package com.gym.members.verifit.service;

import com.gym.members.verifit.entity.StreakMetricsRecord;
import java.util.Optional;

/**
 * Service for streak metrics management
 */
public interface StreakMetricsService {

  /**
   * StreakMetricsRecord the received object to be saved.
   * This happens during the creation of attendance where related StreakMetricsRecord
   * is created within a transaction.
   * @param streakMetrics the received object to be persisted
   * @return StreakMetricsRecord the already persisted streak record object
   */
  StreakMetricsRecord create(StreakMetricsRecord streakMetrics);

  /**
   * get the last streak, this is achieved by ordering the streaks by date then selecting the most recent
   * @param memberId
   * @return int representing the number of weeks in the streak
   */
  Optional<StreakMetricsRecord> getLastStreak(long memberId);

  /**
   * getLastStreakMetricsRecord retrieve this member most recent streak
   * @param memberId the id of the member whose last streak is to be retrieved
   * @return StreakMetricsRecord the StreakMetricsRecord object of this member
   */
  Optional<StreakMetricsRecord> getLastStreakMetricsRecord(long memberId);
}
