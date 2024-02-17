package com.gym.members.verifit.service;

import com.gym.members.verifit.entity.AttendanceRecord;
import java.time.LocalDate;
import java.util.Optional;

/**
 * class to create and retrieve an attendance
 */
public interface AttendanceRecordService {

  /**
   * hasMembership verify if user has membership
   * @param memberId the id the member whose membership is to be validated
   * @return boolean as a feedback on membership existance
   */
  boolean hasMembership(final Long memberId);

  /**
   * create a AttendanceRecord to persistence
   * @param memberId the id the member whose record is to be created
   * @param localDate the current date of the member whose record is to be created
   * @return AttendanceRecord persisted and returned
   */
  AttendanceRecord create(final Long memberId, final LocalDate localDate);

  /**
   * checkDiscountEligibility method to check eligibility
   * @param memberId the id of the member whose discount eligibility is to be checked
   * @return boolean true or false
   */
  Optional<AttendanceRecord> getLastRecordAttendance(final Long memberId);
}
