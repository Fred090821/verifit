package com.gym.members.verifit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.gym.members.verifit.base.BaseServiceTest;
import com.gym.members.verifit.entity.AttendanceRecord;
import com.gym.members.verifit.entity.StreakMetricsRecord;
import com.gym.members.verifit.repository.AttendanceRecordRepository;
import com.gym.members.verifit.repository.StreakMetricsRepository;
import com.gym.members.verifit.util.FitnessDateUtils;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

// Annotation used to declare a custom display name for the annotated test class or test method.
@DisplayName("AttendanceMetricsSynchronizerTest")
class AttendanceMetricsSynchronizerTest extends BaseServiceTest {

  //all members attendance created/attended today have the same date registered as attendance date
  private static final LocalDate TODAY_ATTENDANCE_DATE = LocalDate.now();
  private static final long MEMBER_ID_TO_BE_TESTED = 412l;
  private static final int CURRENT_WEEK = FitnessDateUtils.getWeekOfYear(TODAY_ATTENDANCE_DATE);

  @Autowired
  private AttendanceMetricsSynchronizer attendanceMetricsSynchronizer;
  @Autowired
  private AttendanceRecordRepository attendanceRecordRepository;
  @Autowired
  private StreakMetricsRepository streakMetricsRepository;

  @AfterEach
  void tearDown() {
  }

  @BeforeEach
  void setUp() {
    attendanceRecordRepository.deleteAll();
    streakMetricsRepository.deleteAll();
  }

  /**
   * should have
   * 7 new members and 7 new StreakMetrics
   *
   */
  @Test
  void whenNewMembersAreCreatedShouldHaveSevenRelatedStreakMetrics() {
    //given
    attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(1l,TODAY_ATTENDANCE_DATE);
    attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(2l,TODAY_ATTENDANCE_DATE);
    attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(3l,TODAY_ATTENDANCE_DATE);
    attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(4l,TODAY_ATTENDANCE_DATE);
    attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(MEMBER_ID_TO_BE_TESTED,TODAY_ATTENDANCE_DATE);
    attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(14l,TODAY_ATTENDANCE_DATE);
    attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(436l,TODAY_ATTENDANCE_DATE);

    //when
    // Returns all entities to check the actual result with the expected.
    List<AttendanceRecord> attendanceRecordEntities = attendanceRecordRepository.findAll();

    //then
    /**
     * Tests for the 7 new members AttendanceRecords
     */
    // Checks if the table has only one record, which we have just created.
    assertEquals(7, attendanceRecordEntities.size());
    assertEquals(true, attendanceRecordEntities.stream().anyMatch(x -> x.getMemberId() == 1));
    assertEquals(true, attendanceRecordEntities.stream().anyMatch(x -> x.getMemberId() == MEMBER_ID_TO_BE_TESTED));
    assertEquals(true, attendanceRecordEntities.stream().allMatch(x -> x.getAttendanceDate().equals(TODAY_ATTENDANCE_DATE)));

    //test sanity
    assertNotEquals(true, attendanceRecordEntities.stream().anyMatch(x -> x.getMemberId() == 20));
    assertNotEquals(false, attendanceRecordEntities.stream().anyMatch(x -> x.getMemberId() == MEMBER_ID_TO_BE_TESTED));
    assertNotEquals(true, attendanceRecordEntities.stream().allMatch(x -> x.getAttendanceDate().equals(FitnessDateUtils.getWeekOfYear(LocalDate.of(2026, Month.JANUARY, 8)))));

    /**
     * Tests for associated 7 new members StreakMetrics
     */
    List<StreakMetricsRecord> streakMetricsRecordEntity = streakMetricsRepository.findAll();
    // Checks if the table has only one record, which we have just created.
    assertEquals(7, streakMetricsRecordEntity.size());
    assertEquals(true, streakMetricsRecordEntity.stream().anyMatch(x -> x.getMemberId() == 1));
    assertEquals(true, streakMetricsRecordEntity.stream().anyMatch(x -> x.getMemberId() == MEMBER_ID_TO_BE_TESTED));

    //all streak have start streak week and last active week set to current week as newly created.
    assertEquals(true, streakMetricsRecordEntity.stream().allMatch(x -> x.getStartStreakWeek() == CURRENT_WEEK));
    assertEquals(true, streakMetricsRecordEntity.stream().allMatch(x -> x.getLastActiveWeek() == CURRENT_WEEK));

    //since created today so streak is 1 week
    assertEquals(true, streakMetricsRecordEntity.stream().allMatch(x -> x.getWeeks() == 1));
  }

  @Test
  void noMatterHowManyTimeSameDaySameMemberAttend_StreakMetricsIsSameForSameDay() {

    attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(MEMBER_ID_TO_BE_TESTED,TODAY_ATTENDANCE_DATE);
    attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(MEMBER_ID_TO_BE_TESTED,TODAY_ATTENDANCE_DATE);
    attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(MEMBER_ID_TO_BE_TESTED,TODAY_ATTENDANCE_DATE);
    attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(MEMBER_ID_TO_BE_TESTED,TODAY_ATTENDANCE_DATE);
    attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(MEMBER_ID_TO_BE_TESTED,TODAY_ATTENDANCE_DATE);
    attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(MEMBER_ID_TO_BE_TESTED,TODAY_ATTENDANCE_DATE);
    attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(MEMBER_ID_TO_BE_TESTED,TODAY_ATTENDANCE_DATE);

    // Returns all entities to check the actual result with the expected.
    List<AttendanceRecord> attendanceRecordEntities = attendanceRecordRepository.findAll();

    /**
     * Tests 1 new member who attended 7 times on the same day
     */
    // Same member attended 7 times in the same day
    assertEquals(7, attendanceRecordEntities.size());
    assertEquals(true, attendanceRecordEntities.stream().allMatch(x -> x.getMemberId() == MEMBER_ID_TO_BE_TESTED));
    assertEquals(true, attendanceRecordEntities.stream().allMatch(x -> x.getAttendanceDate().equals(TODAY_ATTENDANCE_DATE)));

    /**
     * Tests for associated StreakMetricsRecord for 1 new members who attended 7 times on the same day
     */
    List<StreakMetricsRecord> streakMetricsRecordEntity = streakMetricsRepository.findAll();
    // Checks if the table has only one record, which we have just created.
    assertEquals(1, streakMetricsRecordEntity.size());
    assertEquals(true, streakMetricsRecordEntity.get(0).getMemberId() == MEMBER_ID_TO_BE_TESTED);

    //The only streak has start streak week and last active week set to current week as newly created.
    assertEquals(true, streakMetricsRecordEntity.get(0).getStartStreakWeek() == CURRENT_WEEK);
    assertEquals(true, streakMetricsRecordEntity.get(0).getLastActiveWeek() == CURRENT_WEEK);

    //7 attendance on the same day will not update the week streak
    assertEquals(true, streakMetricsRecordEntity.get(0).getWeeks() == 1);
  }
}