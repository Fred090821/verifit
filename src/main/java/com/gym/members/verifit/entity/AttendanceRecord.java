package com.gym.members.verifit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *This class is a holder of members attendance records
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class AttendanceRecord implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "member_id", nullable = false)
  private Long memberId;

  @Column(name = "attendance_date", nullable = false)
  private LocalDate attendanceDate;
}