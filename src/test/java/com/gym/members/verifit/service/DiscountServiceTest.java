package com.gym.members.verifit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gym.members.verifit.base.BaseServiceTest;
import com.gym.members.verifit.repository.AttendanceRecordRepository;
import com.gym.members.verifit.repository.StreakMetricsRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@BootstrapWith(SpringBootTestContextBootstrapper.class)
@ExtendWith({SpringExtension.class})
@ComponentScan("com.gym.members.verifit")
  @Transactional
  @DisplayName("AttendanceMetricsSynchronizerTest.")
  class DiscountServiceTest extends BaseServiceTest {

  @Autowired
  private AttendanceMetricsSynchronizer attendanceMetricsSynchronizer;
  @Autowired
  private DiscountService DiscountService;
  @Autowired
  private StreakMetricsService streakMetricsService;

    @Autowired
    private AttendanceRecordRepository attendanceRecordRepository;
    @Autowired
    private StreakMetricsRepository streakMetricsRepository;

    @AfterEach
    void tearDown() {
      attendanceRecordRepository.deleteAll();
      streakMetricsRepository.deleteAll();
    }

    @Test
    void whenNewMembersAreCreatedShouldHaveSevenRelatedStreakMetrics() {

      //given
      /**
       * WEEK 2
       * we have member with id =1l
       */

      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(1L, LocalDate.parse("2024-01-08"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(1L, LocalDate.parse("2024-01-09"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(1L, LocalDate.parse("2024-01-10"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(1L, LocalDate.parse("2024-01-11"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(1L, LocalDate.parse("2024-01-12"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(1L, LocalDate.parse("2024-01-13"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(1L, LocalDate.parse("2024-01-14"));

      /**
       * WEEK 3
       * we have memberId = 1l
       * we have memberId = 3l
       */
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(1L, LocalDate.parse("2024-01-15"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(1L, LocalDate.parse("2024-01-16"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(3L, LocalDate.parse("2024-01-17"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(1L, LocalDate.parse("2024-01-18"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(1L, LocalDate.parse("2024-01-19"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(3L, LocalDate.parse("2024-01-20"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(1L, LocalDate.parse("2024-01-21"));

      /**
       * WEEK 4
       * we have memberId = 1l
       * we have memberId = 3l
       * we have memberId = 2l
       */
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(1L, LocalDate.parse("2024-01-22"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(1L, LocalDate.parse("2024-01-23"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(1L, LocalDate.parse("2024-01-24"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(2L, LocalDate.parse("2024-01-25"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(3L, LocalDate.parse("2024-01-26"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(3L, LocalDate.parse("2024-01-27"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(1L, LocalDate.parse("2024-01-28"));

      /**
       * WEEK 5
       * we have memberId = 1l
       * we have memberId = 2l
       */
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(2L, LocalDate.parse("2024-01-29"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(1L, LocalDate.parse("2024-01-30"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(1L, LocalDate.parse("2024-01-31"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(1L, LocalDate.parse("2024-02-01"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(1L, LocalDate.parse("2024-02-02"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(1L, LocalDate.parse("2024-02-03"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(1L, LocalDate.parse("2024-02-04"));

      /**
       * WEEK 6
       * we have memberId = 1l
       * we have memberId = 3l
       * we have memberId = 2l
       */
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(2L, LocalDate.parse("2024-02-05"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(1L, LocalDate.parse("2024-02-06"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(3L, LocalDate.parse("2024-02-07"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(1L, LocalDate.parse("2024-02-08"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(1L, LocalDate.parse("2024-02-09"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(1L, LocalDate.parse("2024-02-10"));
      attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(1L, LocalDate.parse("2024-02-11"));

      //when
      // Returns all entities to check the actual result with the expected.
      /**
       * with 5 consecutive weeks streak
       * member with id = 1 has a discount
       */
      assertEquals(5, streakMetricsService.getLastStreak(1l).get().getWeeks());
      assertTrue(DiscountService.checkDiscountEligibility(1l));

      /**
       * with only 1 week streak due to a gap
       * member with id = 1 has no discount
       */
      assertEquals(1, streakMetricsService.getLastStreak(3l).get().getWeeks());
      assertFalse(DiscountService.checkDiscountEligibility(3l));

      /**
       * with 3 weeks streak
       * member with id = 2 has a discount
       */
      assertEquals(3, streakMetricsService.getLastStreak(2l).get().getWeeks());
      assertTrue(DiscountService.checkDiscountEligibility(2l));
    }
}