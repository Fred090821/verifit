package com.gym.members.verifit.service.impl;

import com.gym.members.verifit.entity.StreakMetricsRecord;
import com.gym.members.verifit.repository.StreakMetricsRepository;
import com.gym.members.verifit.service.StreakMetricsService;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class StreakMetricsServiceImpl implements StreakMetricsService {
  private static final Logger LOGGER = LogManager.getLogger(StreakMetricsServiceImpl.class);

  private final StreakMetricsRepository streakMetricsRepository;

  public StreakMetricsServiceImpl(StreakMetricsRepository streakMetricsRepository) {
    this.streakMetricsRepository = streakMetricsRepository;
  }

  @Override
  public StreakMetricsRecord create(StreakMetricsRecord streakMetrics) {
    return this.streakMetricsRepository.saveAndFlush(streakMetrics);
  }

  @Override
  public Optional<StreakMetricsRecord> getLastStreak(long memberId) {

    //either the counter is reset because of a gap or this member is a new member
    return streakMetricsRepository.findFirstByMemberIdOrderByLastActiveWeekDesc(
        memberId);
  }

  @Override
  public Optional<StreakMetricsRecord> getLastStreakMetricsRecord(long memberId) {
    return streakMetricsRepository.findFirstByMemberIdOrderByLastActiveWeekDesc(memberId);
  }
}