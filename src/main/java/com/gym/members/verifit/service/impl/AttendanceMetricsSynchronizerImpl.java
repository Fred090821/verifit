package com.gym.members.verifit.service.impl;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

import com.gym.members.verifit.entity.AttendanceRecord;
import com.gym.members.verifit.entity.StreakMetricsRecord;
import com.gym.members.verifit.service.AttendanceMetricsSynchronizer;
import com.gym.members.verifit.service.AttendanceRecordService;
import com.gym.members.verifit.service.StreakMetricsService;
import com.gym.members.verifit.util.FitnessDateUtils;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AttendanceMetricsSynchronizerImpl implements AttendanceMetricsSynchronizer {
  private static final Logger LOGGER = LogManager.getLogger(AttendanceMetricsSynchronizerImpl.class);

  private final AttendanceRecordService attendanceRecordService;
  private final StreakMetricsService streakMetricsService;

  @Autowired
  public AttendanceMetricsSynchronizerImpl(
      AttendanceRecordService attendanceRecordService, StreakMetricsService streakMetricsService) {
    this.attendanceRecordService = attendanceRecordService;
    this.streakMetricsService = streakMetricsService;
  }

  @Override
  @Transactional(rollbackFor = {SQLException.class})
  public AttendanceRecord recordAttendanceAndSyncWithStreaks(final Long memberId, final LocalDate localDate) {

    // Retrieve the previous most recent attendance
    final AttendanceRecord previousWeek = attendanceRecordService.getLastRecordAttendance(memberId)
        .orElse(new AttendanceRecord());

    //Create a new attendance record
    final AttendanceRecord newAttendanceEntry = attendanceRecordService.create(memberId, localDate);
    assertNotNull(newAttendanceEntry);

    //set previousWeekAttendance and thisWeekAttendance which will be used to determine streak or not
    //previousWeekAttendance could be same as thisWeekAttendance if attended the gym more than once in same week
    //previousWeekAttendance could be 0 if member registering for first time
    final int previousWeekAttendance = previousWeek.getMemberId() != null ? FitnessDateUtils.getWeekOfYear(
        previousWeek.getAttendanceDate()) : 0;
    final int thisWeekAttendance = FitnessDateUtils.getWeekOfYear(newAttendanceEntry.getAttendanceDate());
    //create associated streak record
    createAssociatedStreakMetrics(memberId, previousWeekAttendance, thisWeekAttendance);

    return newAttendanceEntry;
  }

  private void createAssociatedStreakMetrics(Long memberId, int lastAttendanceWeek, int newAttendanceWeek) {

    //Condition met if member is attending first time or if there is a GAP OF 1 WEEK at least
    if (isFirstWeek(lastAttendanceWeek) || isNewStreakRequired(lastAttendanceWeek, newAttendanceWeek)) {
      StreakMetricsRecord streakMetrics = StreakMetricsRecord.builder()
          .lastActiveWeek(newAttendanceWeek)
          .weeks(1)
          .memberId(memberId)
          .startStreakWeek(newAttendanceWeek)
          .build();
      StreakMetricsRecord retrievedStreakMetrics = streakMetricsService.create(streakMetrics);
      assertNotNull(retrievedStreakMetrics);
    }

    if (isContinuationOfStreak(lastAttendanceWeek, newAttendanceWeek)) {
      Optional<StreakMetricsRecord> streakMetrics = streakMetricsService.getLastStreakMetricsRecord(memberId);


      if (streakMetrics.isPresent()) {
        final StreakMetricsRecord sMetrics = streakMetrics.get();
        //increment streak indicator
        sMetrics.setWeeks( sMetrics.getWeeks() + 1);
        sMetrics.setMemberId(memberId);
        sMetrics.setLastActiveWeek(newAttendanceWeek);
        StreakMetricsRecord retrievedStreakMetrics = streakMetricsService.create(sMetrics);
        assertNotNull(retrievedStreakMetrics);
      }
    }
  }

  private boolean isFirstWeek(int lastAttendanceWeek) {
    return lastAttendanceWeek == 0;
  }

  private boolean isContinuationOfStreak(int lastAttendanceWeek, int newAttendanceWeek) {
    return newAttendanceWeek - lastAttendanceWeek == 1;
  }

  private boolean isNewStreakRequired(int lastAttendanceWeek, int newAttendanceWeek) {
    return newAttendanceWeek - lastAttendanceWeek > 1;
  }


}
