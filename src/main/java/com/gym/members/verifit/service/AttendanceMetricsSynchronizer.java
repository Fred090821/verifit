package com.gym.members.verifit.service;

import com.gym.members.verifit.entity.AttendanceRecord;
import java.time.LocalDate;

/**
 * class to sync attendance and streak
 */
public interface AttendanceMetricsSynchronizer {

  /**
   * this class is used to manage consistency while creating member streak record and attendance record
   * @param memberId member whose id will be used to populate attendance and streak
   * @param localDate member whose date will be used to populate attendance and streak
   * @return AttendanceRecord persisted and returned
   */
  public AttendanceRecord recordAttendanceAndSyncWithStreaks(final Long memberId, final LocalDate localDate);
}
