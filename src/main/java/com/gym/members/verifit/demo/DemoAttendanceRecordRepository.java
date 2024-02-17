package com.gym.members.verifit.demo;

import com.gym.members.verifit.entity.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DemoAttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {

  //clear table
  @Modifying
  @Query(value = "TRUNCATE TABLE \"attendance_record\" ", nativeQuery = true)
  void emptyAttendanceRecord();
}