package com.gym.members.verifit.controller;

import com.gym.members.verifit.entity.AttendanceRecord;
import com.gym.members.verifit.entity.StreakMetricsRecord;
import com.gym.members.verifit.model.Attendance;
import com.gym.members.verifit.service.AttendanceMetricsSynchronizer;
import com.gym.members.verifit.service.AttendanceRecordService;
import com.gym.members.verifit.service.DiscountService;
import com.gym.members.verifit.service.StreakMetricsService;
import com.gym.members.verifit.service.impl.DiscountServiceImpl;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Fred Assi
 * Controller which is the entrypoint for the application
 * Member can register their attendance
 * Owner/admin can check for discount or check for the status of members streak
 * streak = once a week for at least 3 consecutive weeks.
 */
@RestController
@RequestMapping("/api")
public class AttendanceController {

  private static final Logger LOGGER = LogManager.getLogger(AttendanceController.class);
  private AttendanceMetricsSynchronizer attendanceMetricsSynchronizer;
  private AttendanceRecordService attendanceRecordService;
  private final DiscountService discountService;
  private final StreakMetricsService streakService;

  @Autowired
  public AttendanceController(AttendanceMetricsSynchronizer attendanceMetricsSynchronizer, DiscountServiceImpl discountService,
      StreakMetricsService streakService, AttendanceRecordService attendanceRecordService) {
    this.attendanceMetricsSynchronizer = attendanceMetricsSynchronizer;
    this.discountService = discountService;
    this.streakService = streakService;
    this.attendanceRecordService = attendanceRecordService;
  }

  /**
   * POST  /attendance : Create a new attendance.
   *
   * @param attendance the attendance to create
   * @return the ResponseEntity with status 201 (Created) and with body the new attendance, you can create as many attendance as you want
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/attendance")
  public ResponseEntity<AttendanceRecord> addAttendance(@Valid @RequestBody Attendance attendance) throws URISyntaxException {
    LOGGER.debug("Request to add user attendance : {}", attendance.getMemberId());

    AttendanceRecord attendanceRecord = attendanceMetricsSynchronizer.recordAttendanceAndSyncWithStreaks(attendance.getMemberId(), attendance.getAttendanceDate());
    return ResponseEntity.created(new URI("/api/attendance/" + attendanceRecord.getId()))
        .body(attendanceRecord);
  }


  /**
   * GET   /admin/discountCheck/{memberId} : Retrieve an attendance.
   * @param memberId the id of the member whose discount is to be checked
   * @return boolean to indicate if found or not
   */
  @GetMapping("/admin/members/{memberId}/discount-check")
  public ResponseEntity<Boolean> checkDiscount(@PathVariable Long memberId) {
    if(memberId == null || !attendanceRecordService.hasMembership(memberId)){
      LOGGER.info("User is not a member of Verifit");
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    LOGGER.debug("REST request to checkDiscount for a member with id: {}", memberId);
    boolean eligible = discountService.checkDiscountEligibility(memberId);

    return ResponseEntity.ok(eligible);
  }

  /**
   * GET   /admin/streak/{memberId} : Retrieve given member current streak.
   * @param memberId the id of the member whose last streak is to be retrieved
   * @return Return the streak value wrapped in a ResponseEntity object.
   * The HTTP status code is set to 200 OK, indicating successful retrieval.
   */
    @GetMapping("/admin/members/{memberId}/stats/streak")
  public ResponseEntity<Integer> getStreak(@PathVariable Long memberId) {

    if (memberId == null || !attendanceRecordService.hasMembership(memberId)) {
      LOGGER.info("User is not a member of Verifit");
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    LOGGER.debug("Request member streak status: {}", memberId);
    final Optional<StreakMetricsRecord> streak = streakService.getLastStreakMetricsRecord(memberId);
    return streak.isEmpty() ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(streak.get().getWeeks(), HttpStatus.OK);
  }
}
