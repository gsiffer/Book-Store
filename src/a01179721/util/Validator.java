/**
 * Project: A00123456Lab2
 * File: Validator.java
 * Copyright 2017 Sam Cirka. All rights reserved.
 */

package a01179721.util;

import java.time.LocalDate;

/**
 * Validate data.
 * 
 * @author Sam Cirka, A00123456
 *
 */
public class Validator {

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String YYYYMMDD_PATTERN = "(20\\d{2})(\\d{2})(\\d{2})"; // valid for years 2000-2099
	private static final int MIN_YEAR = 1000;
	private static final int MAX_YEAR = 2099;

	private Validator() {
	}

	/**
	 * Validate an email string.
	 * 
	 * @param email
	 *            the email string.
	 * @return true if the email address is valid, false otherwise.
	 */
	public static boolean validateEmail(final String email) {
		return email.matches(EMAIL_PATTERN);
	}

	public static boolean validateJoinedDate(String yyyymmdd) {
		boolean validate = false;

		if (yyyymmdd.matches(YYYYMMDD_PATTERN)) {
			validate = true;
			try {
				int year = Integer.parseInt(yyyymmdd.substring(0, 4));
				int month = Integer.parseInt(yyyymmdd.substring(4, 6));
				int day = Integer.parseInt(yyyymmdd.substring(6, 8));
				LocalDate.of(year, month, day);
			} catch (Exception ex) {
				validate = false;
			}
		}

		return validate;
	}

	public static boolean validateYear(int yyyy) {
		return yyyy >= MIN_YEAR && yyyy <= MAX_YEAR;
	}

}
