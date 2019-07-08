package com.wojciechdm.decoder.pesel;

import static com.wojciechdm.decoder.pesel.Gender.*;
import static java.lang.Integer.*;

import java.time.LocalDate;
import java.util.Optional;

class PeselDecoder {

  private PeselValidator peselValidator;

  PeselDecoder(PeselValidator peselValidator) {
    this.peselValidator = peselValidator;
  }

  Optional<Gender> decodeGender(final String pesel) {
    return Optional.of(pesel).filter(this::isValid).map(this::recognizeGender);
  }

  Optional<LocalDate> decodeBirthDate(final String pesel) {
    return Optional.of(pesel).filter(this::isValid).map(this::createDate);
  }

  private boolean isValid(String pesel) {
    return peselValidator.hasValidChecksum(pesel)
        && peselValidator.hasValidFormat(pesel)
        && peselValidator.isValidDate(
            extractYearOfBirth(pesel), extractMonthOfBirth(pesel), extractDayOfBirth(pesel));
  }

  private Gender recognizeGender(String pesel) {
    return (extractGenderDigit(pesel) % 2 == 0) ? FEMALE : MALE;
  }

  private LocalDate createDate(String pesel) {
    return LocalDate.of(
        extractYearOfBirth(pesel), extractMonthOfBirth(pesel), extractDayOfBirth(pesel));
  }

  private int extractGenderDigit(String pesel) {
    return parseInt(pesel.substring(9, 10));
  }

  private int extractYearOfBirth(String pesel) {
    int year = parseInt(pesel.substring(0, 2));
    int month = parseInt(pesel.substring(2, 4));
    int multiplier = month / 20;
    year = 1900 + year;
    year = (multiplier < 4 ? (year + multiplier * 100) : (year - 100));
    return year;
  }

  private int extractMonthOfBirth(String pesel) {
    int month = parseInt(pesel.substring(2, 4));
    while (month > 20) {
      month = month - 20;
    }
    return month;
  }

  private int extractDayOfBirth(String pesel) {
    return parseInt(pesel.substring(4, 6));
  }
}
