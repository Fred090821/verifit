package com.gym.members.verifit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Positive;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class StreakMetricsRecord implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "member_id")
  private Long memberId;

  //the number of current weeks in a members streak
  //1 means either this a gap and the counter is reset
  //or the user is newly register on his first week.
  @Column(name = "weeks_in_row")
  @Positive
  private int weeks;

  //the week when the first streak has started
  @Column(name = "start_week")
  @Positive
  private int startStreakWeek;

  //this is the user last active week
  @Column(name = "last_active_week")
  @Positive
  private int lastActiveWeek;
}