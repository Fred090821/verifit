package com.gym.members.verifit.service.impl;

import com.gym.members.verifit.entity.StreakMetricsRecord;
import com.gym.members.verifit.repository.StreakMetricsRepository;
import com.gym.members.verifit.service.DiscountService;
import com.gym.members.verifit.util.FitnessDateUtils;
import java.time.LocalDate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class DiscountServiceImpl implements DiscountService {
  private static final Logger LOGGER = LogManager.getLogger(DiscountServiceImpl.class);

  private final StreakMetricsRepository streakMetricsRepository;

  public DiscountServiceImpl(StreakMetricsRepository streakMetricsRepository) {
    this.streakMetricsRepository = streakMetricsRepository;
  }

  @Override
  public boolean checkDiscountEligibility(Long memberId) {


    return streakMetricsRepository.findFirstByMemberIdOrderByLastActiveWeekDesc(memberId)
        .filter(metrics -> isEligibleForDiscount(metrics, LocalDate.now()))
        .isPresent();
  }

  private boolean isEligibleForDiscount(StreakMetricsRecord metrics, LocalDate currentDate) {
    int lastActiveWeek = metrics.getLastActiveWeek();
    int currentWeek = FitnessDateUtils.getWeekOfYear(currentDate);

    //if there is a gap after a streak and the current week, the streak tracker is reset
    // so the streak no more valid
    return metrics.getWeeks() >= 3 && isWithinEligibleWeekRange(lastActiveWeek, currentWeek);
  }

  //either current week is the latest streak week or is the week after the last streak so
  //continuation of streak (there is no gap between the last streak and this week to invalidate the streak)
  private boolean isWithinEligibleWeekRange(int lastActiveWeek, int currentWeek) {
    return currentWeek == lastActiveWeek || currentWeek - lastActiveWeek == 1;
  }
}