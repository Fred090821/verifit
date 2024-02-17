package com.gym.members.verifit.service;

/**
 * Service for discount management
 */
public interface DiscountService {

  /**
   * checkDiscountEligibility method to check eligibility
   * @param userId the id of the member whose discount eligibility is to be checked
   * @return boolean true or false
   */
  boolean checkDiscountEligibility(final Long userId);
}
