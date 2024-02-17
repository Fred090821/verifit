package com.gym.members.verifit.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DemoService {
  private static final Logger LOGGER = LogManager.getLogger(DemoService.class);

  private final DemoStreakMetricsRepository demoStreakMetricsRepository;
  private DemoAttendanceRecordRepository demoAttendanceRecordRepository;

  public DemoService(DemoStreakMetricsRepository demoStreakMetricsRepository, DemoAttendanceRecordRepository demoAttendanceRecordRepository) {
    this.demoStreakMetricsRepository =  demoStreakMetricsRepository;
    this.demoAttendanceRecordRepository =  demoAttendanceRecordRepository;
  }

  @Transactional
  public void clearTables() {
    demoStreakMetricsRepository.emptyStreakMetricsRecord();
    demoAttendanceRecordRepository.emptyAttendanceRecord();
  }
}