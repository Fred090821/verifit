package com.gym.members.verifit.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * member's attendance data holder
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Attendance {
  @NonNull
  private Long memberId;

  //user date of attendance which will be used to calculate the week
  //of attendance, this will in the end determine if member should avail of a discount
  @NonNull
  private LocalDate attendanceDate;

}
