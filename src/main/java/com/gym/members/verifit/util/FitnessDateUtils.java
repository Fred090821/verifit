package com.gym.members.verifit.util;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

//This utility class is fundamental to how the application manages the weeks and gap and discounts.

/**
 * a week of attendance has 7 days, during them 7 days a member can attend the gym more than once
 * but for the sake of streak, only one attendance a week count toward a streak.
 * when a user attend the gym within a week, that week is validated toword a potential streak.
 *
 * a week is one of the 52 weeks a year.
 *
 * for example if you take a 2024 calendar, 28 Jan 2024 to 3rd february belongs to the same week
 * if a member attend the gym anyday of the week 14 jan to 20 Jan, even once that week will count toward a streak
 *
 * if a user attend the gym even once a week between 7 Jan 2024 and 27 Jan 2024 then that is a streak and a discount
 * can be applied.
 *
 * but for the system simplicity, the counter is reset if the member has a gap afterward
 *
 *  Limits of the system
 *  There is no discount management capability
 *  The system is valid while within jan and december first and last week,
 */
public final class FitnessDateUtils {

  private static final WeekFields weekFields = WeekFields.of(Locale.getDefault());
  public static int getWeekOfYear(LocalDate date) {
    return date.get(weekFields.weekOfWeekBasedYear());
  }

}
