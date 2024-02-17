package com.gym.members.verifit.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.wavefront.WavefrontProperties.Application;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
public class DemoController {
  private static final Logger LOGGER = LogManager.getLogger(Application.class);
  private DemoService demoService;

  @Autowired
  public DemoController(DemoService demoService) {
    this.demoService = demoService;
  }

  /**
   * For demo purpose clear db
   */
  @PostMapping("/admin/truncate-tables")
  public ResponseEntity<String> truncate() {
    demoService.clearTables();
    return ResponseEntity.ok("Success");
  }
}