package com.gym.members.verifit.service.impl;

import com.gym.members.verifit.entity.AttendanceRecord;
import com.gym.members.verifit.repository.AttendanceRecordRepository;
import com.gym.members.verifit.service.AttendanceRecordService;
import java.time.LocalDate;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class AttendanceRecordServiceImpl implements AttendanceRecordService {
  private static final Logger LOGGER = LogManager.getLogger(AttendanceRecordServiceImpl.class);

  private final AttendanceRecordRepository attendanceRecordRepository;

  public AttendanceRecordServiceImpl(AttendanceRecordRepository attendanceRecordRepository) {
    this.attendanceRecordRepository = attendanceRecordRepository;
  }

  /**
   * hasMembership verify if user has membership
   *
   * @param memberId the id the member whose membership is to be validated
   * @return boolean as a feedback on membership existance
   */
  @Override
  public boolean hasMembership(Long memberId) {
    return attendanceRecordRepository.findByMemberId(memberId).isEmpty()? false : true;
  }

  /**
   *
   * @param memberId
   * @return
   */
  @Override
  public AttendanceRecord create(final Long memberId, final LocalDate localDate) {
    AttendanceRecord newAttendanceRecord = AttendanceRecord.builder().memberId(memberId).attendanceDate(localDate).build();
    return attendanceRecordRepository.saveAndFlush(newAttendanceRecord);
  }

  /**
   *
   * @param memberId
   * @return
   */
  @Override
  public Optional<AttendanceRecord> getLastRecordAttendance(final Long memberId) {
    return attendanceRecordRepository.findFirstByMemberIdOrderByAttendanceDateDesc(memberId);
  }
}



