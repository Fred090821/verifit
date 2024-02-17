package com.gym.members.verifit.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gym.members.verifit.entity.AttendanceRecord;
import com.gym.members.verifit.model.Attendance;
import com.gym.members.verifit.repository.AttendanceRecordRepository;
import com.gym.members.verifit.repository.StreakMetricsRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;



@SpringBootTest
@Testcontainers
class GymControllerTest {

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private StreakMetricsRepository streakMetricsRepository;
  @Autowired
  private AttendanceRecordRepository attendanceRecordRepository;

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

  @Test
  void connectionEstablished() {
    assertThat(postgres.isCreated()).isTrue();
    assertThat(postgres.isRunning()).isTrue();
  }

  @BeforeEach
  void setUp() {
    attendanceRecordRepository.deleteAll();
    streakMetricsRepository.deleteAll();
  }

  @Test
  void when_A_ValidAttendanceIsPosted_HttpCreatedIsReturned() {
    String ApiUrl = "http://localhost:8080/api/attendance";

    Attendance attendance = Attendance.builder()
        .memberId(1l)
        .attendanceDate(LocalDate.parse("2024-01-09"))
        .build();

    ResponseEntity<AttendanceRecord> responseEntity = restTemplate.postForEntity(ApiUrl, attendance,
        AttendanceRecord.class);

    assertThat(responseEntity.getBody().getAttendanceDate()).isEqualTo(LocalDate.parse("2024-01-09"));
    assertThat(responseEntity.getBody().getMemberId()).isEqualTo(1l);
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
  }

  @Test
  void when_An_inValidAttendanceIsPosted_HttpCreatedIsReturned() {
    String ApiUrl = "http://localhost:8080/api/attendance";

    Exception exception = assertThrows(NullPointerException.class, () -> {
      Attendance attendance = Attendance.builder()
          .attendanceDate(LocalDate.parse("2024-01-09"))
          .build();
      ResponseEntity<AttendanceRecord> responseEntity = restTemplate.postForEntity(ApiUrl, attendance,
          AttendanceRecord.class);

    });

    String expectedMessage = "memberId is marked non-null but is null";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void whenTheMemberDoesNotExist_theSearchForHisStreakShouldReturn_NotFound() {
    Exception exception = assertThrows(HttpClientErrorException.class, () -> {
      Integer streak = restTemplate.getForObject("http://localhost:8080/api/admin/members/12/stats/streak", Integer.class);
    });

    String expectedMessage = "404";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }
}
