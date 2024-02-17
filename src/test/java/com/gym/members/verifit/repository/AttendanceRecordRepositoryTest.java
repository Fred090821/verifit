package com.gym.members.verifit.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gym.members.verifit.base.BaseRepositoryTest;
import com.gym.members.verifit.entity.AttendanceRecord;
import com.gym.members.verifit.service.AttendanceMetricsSynchronizer;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


@DisplayName("AttendanceMetricsSynchronizerTest.")
class AttendanceRecordRepositoryTest extends BaseRepositoryTest {

  private static final long MEMBER_ID_TO_BE_TESTED_FOR_CREATED = 412l;
  //all members attendance created/attended today have the same date registered as attendance date
  private static final LocalDate TODAY_ATTENDANCE_DATE = LocalDate.now();
  @Autowired
  private AttendanceMetricsSynchronizer attendanceMetricsSynchronizer;
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
  void recordAttendanceAndSyncWithStreaks() {

    attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(1l, TODAY_ATTENDANCE_DATE);
    attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(2l, TODAY_ATTENDANCE_DATE);
    attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(3l, TODAY_ATTENDANCE_DATE);
    attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(4l, TODAY_ATTENDANCE_DATE);
    attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(MEMBER_ID_TO_BE_TESTED_FOR_CREATED, TODAY_ATTENDANCE_DATE);
    attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(14l, TODAY_ATTENDANCE_DATE);
    attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(436l, TODAY_ATTENDANCE_DATE);

    // Returns all entities to check the actual result with the expected.
    List<AttendanceRecord> attendanceRecordEntities = attendanceRecordRepository.findAll();


    // Checks if the table has only one record, which we have just created.
    assertEquals(7, attendanceRecordEntities.size());
    assertEquals(true, attendanceRecordEntities.stream().anyMatch(x -> x.getMemberId() == 1));
    assertEquals(true, attendanceRecordEntities.stream().anyMatch(x -> x.getMemberId() == MEMBER_ID_TO_BE_TESTED_FOR_CREATED));

    assertEquals(true, attendanceRecordEntities.stream().allMatch(x -> x.getAttendanceDate().equals(TODAY_ATTENDANCE_DATE)));
  }

}