package com.wojciechdm.decoder.pesel;

import static java.lang.Integer.*;

import java.time.YearMonth;
import java.util.regex.*;

class PeselValidator {

  private static final Pattern PESEL_REGEX = Pattern.compile("\\d{11}");

  boolean hasValidFormat(String pesel) {
    Matcher matcher = PESEL_REGEX.matcher(pesel);
    return matcher.matches();
  }

  boolean hasValidChecksum(String pesel) {
    return calculateChecksum(pesel) == extractChecksumDigit(pesel);
  }

  boolean isValidDate(int year, int month, int day) {
    boolean isPositive = year > 0 && month > 0 && day > 0;
    return isPositive && month <= 12 && day <= calculateMaxDaysForMonth(year, month);
  }

  private int calculateChecksum(String pesel) {
    int checksum;
    int[] digits = extractAllDigits(pesel);
    checksum =
        9 * digits[0]
            + 7 * digits[1]
            + 3 * digits[2]
            + digits[3]
            + 9 * digits[4]
            + 7 * digits[5]
            + 3 * digits[6]
            + digits[7]
            + 9 * digits[8]
            + 7 * digits[9];
    checksum = checksum % 10;
    return checksum;
  }

  private int extractChecksumDigit(String pesel) {
    return parseInt(pesel.substring(10));
  }

  private int calculateMaxDaysForMonth(int year, int month) {
    YearMonth yearMonth = YearMonth.of(year, month);
    return yearMonth.lengthOfMonth();
  }

  private int[] extractAllDigits(String pesel) {
    int[] digits = new int[11];
    for (int i = 0; i < pesel.length(); i++) {
      digits[i] = parseInt(pesel.substring(i, i + 1));
    }
    return digits;
  }
}
