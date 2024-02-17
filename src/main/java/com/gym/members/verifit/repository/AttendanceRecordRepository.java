package com.gym.members.verifit.repository;

import com.gym.members.verifit.entity.AttendanceRecord;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * repository dedicated to save attendance records to database
 */
@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
//Used to check if given user is a member
  List<AttendanceRecord> findByMemberId(Long memberId);
  //Retrieve the most recent attendance record for a given memberId from the attendanceRecord table.
  Optional<AttendanceRecord> findFirstByMemberIdOrderByAttendanceDateDesc(Long memberId);
}