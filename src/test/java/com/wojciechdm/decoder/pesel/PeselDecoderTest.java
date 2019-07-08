package com.wojciechdm.decoder.pesel;

import static com.wojciechdm.decoder.pesel.Gender.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

class PeselDecoderTest {

  private PeselDecoder peselDecoder;
  private PeselValidator peselValidatorMock = mock(PeselValidator.class);

  @BeforeEach
  void setUp() {
    peselDecoder = new PeselDecoder(peselValidatorMock);
  }

  @AfterEach
  void tearDown() {
    reset(peselValidatorMock);
  }

  @ParameterizedTest
  @MethodSource("gendersAndPesels")
  void shouldDecodeGender(Gender expectedGender, String pesel) {
    // given
    when(peselValidatorMock.hasValidFormat(anyString())).thenReturn(true);
    when(peselValidatorMock.hasValidChecksum(anyString())).thenReturn(true);
    when(peselValidatorMock.isValidDate(anyInt(), anyInt(), anyInt())).thenReturn(true);
    // when
    Gender actualGender = peselDecoder.decodeGender(pesel).get();
    // then
    assertEquals(expectedGender, actualGender);
  }

  @ParameterizedTest
  @MethodSource("correctPesels")
  void shouldDecodeBirthDate(int year, int month, int day, String pesel) {
    // given
    when(peselValidatorMock.hasValidFormat(anyString())).thenReturn(true);
    when(peselValidatorMock.hasValidChecksum(anyString())).thenReturn(true);
    when(peselValidatorMock.isValidDate(anyInt(), anyInt(), anyInt())).thenReturn(true);
    LocalDate expectedDate = LocalDate.of(year, month, day);
    // when
    LocalDate actualDate = peselDecoder.decodeBirthDate(pesel).get();
    // then
    assertEquals(expectedDate, actualDate);
  }

  @ParameterizedTest
  @MethodSource("incorrectPesels")
  void shouldReturnEmptyOptionalInsteadBirthDateWhenPeselIsIncorrect(String pesel) {
    // given
    when(peselValidatorMock.hasValidFormat(anyString())).thenReturn(false);
    when(peselValidatorMock.hasValidChecksum(anyString())).thenReturn(false);
    when(peselValidatorMock.isValidDate(anyInt(), anyInt(), anyInt())).thenReturn(false);
    Optional<LocalDate> expected = Optional.empty();
    // when
    Optional<LocalDate> actual = peselDecoder.decodeBirthDate(pesel);
    // then
    assertEquals(expected, actual);
  }

  @ParameterizedTest
  @MethodSource("incorrectPesels")
  void shouldReturnEmptyOptionaInsteadGenderWhenPeselIsIncorrect(String pesel) {
    // given
    when(peselValidatorMock.hasValidFormat(anyString())).thenReturn(false);
    when(peselValidatorMock.hasValidChecksum(anyString())).thenReturn(false);
    when(peselValidatorMock.isValidDate(anyInt(), anyInt(), anyInt())).thenReturn(false);
    Optional<Gender> expected = Optional.empty();
    // when
    Optional<Gender> actual = peselDecoder.decodeGender(pesel);
    // then
    assertEquals(expected, actual);
  }

  static Stream<Arguments> gendersAndPesels() {
    return Stream.of(Arguments.of(MALE, "89092420358"), Arguments.of(FEMALE, "89092420341"));
  }

  static Stream<Arguments> correctPesels() {
    return Stream.of(
        Arguments.of(1889, 9, 24, "89892420345"),
        Arguments.of(1989, 9, 24, "89092420341"),
        Arguments.of(2089, 9, 24, "89292420347"),
        Arguments.of(2189, 9, 24, "89492420343"));
  }

  static Stream<Arguments> incorrectPesels() {
    return Stream.of(
        Arguments.of("890924203"),
        Arguments.of("7800bbb"),
        Arguments.of("7474748888899990"),
        Arguments.of(""),
        Arguments.of("89092420345"));
  }
}
