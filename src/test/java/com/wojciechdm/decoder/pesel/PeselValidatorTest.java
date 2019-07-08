package com.wojciechdm.decoder.pesel;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

class PeselValidatorTest {

  private PeselValidator peselValidator;

  @BeforeEach
  void setUp() {
    peselValidator = new PeselValidator();
  }

  @ParameterizedTest
  @MethodSource("incorrectDates")
  void shoudSayDateIsInvalid(int year, int month, int day) {
    // given
    boolean expected = false;
    // when
    boolean actual = peselValidator.isValidDate(year, month, day);
    // then
    assertEquals(expected, actual);
  }

  @ParameterizedTest
  @MethodSource("correctDates")
  void shoudSayDateIsValid(int year, int month, int day) {
    // given
    boolean expected = true;
    // when
    boolean actual = peselValidator.isValidDate(year, month, day);
    // then
    assertEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"890924102", "8909241025667889", "890924xx566"})
  void shouldSayFormatIsInvalid(String pesel) {
    // given
    boolean expected = false;
    // when
    boolean actual = peselValidator.hasValidFormat(pesel);
    // then
    assertEquals(expected, actual);
  }

  @Test
  void shoudSayFormatIsValid() {
    // given
    boolean expected = true;
    // when
    boolean actual = peselValidator.hasValidFormat("04222920348");
    // then
    assertEquals(expected, actual);
  }

  @Test
  void shouldSayChecksumIsInvalid() {
    // given
    boolean expected = false;
    // when
    boolean actual = peselValidator.hasValidChecksum("04222920345");
    // then
    assertEquals(expected, actual);
  }

  @Test
  void shoudSayChecksumIsValid() {
    // given
    boolean expected = true;
    // when
    boolean actual = peselValidator.hasValidFormat("04222920348");
    // then
    assertEquals(expected, actual);
  }

  static Stream<Arguments> incorrectDates() {
    return Stream.of(Arguments.of(2017, 2, 29), Arguments.of(2017, 13, 20), Arguments.of(0, 0, 0));
  }

  static Stream<Arguments> correctDates() {
    return Stream.of(
        Arguments.of(2004, 2, 29), Arguments.of(2016, 10, 31), Arguments.of(2016, 3, 30));
  }
}
