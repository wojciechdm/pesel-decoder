package com.wojciechdm.decoder.pesel;

import java.time.LocalDate;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;

public class PeselDecoderFacade {

  private PeselDecoder peselDecoder;

  public PeselDecoderFacade() {
    peselDecoder = new PeselDecoder(new PeselValidator());
  }

  public Optional<Gender> decodeGender(final String pesel) {

    checkArgument(nonNull(pesel), "PESEL can't be null");

    return peselDecoder.decodeGender(pesel);
  }

  public Optional<LocalDate> decodeBirthDate(final String pesel) {

    checkArgument(nonNull(pesel), "PESEL can't be null");

    return peselDecoder.decodeBirthDate(pesel);
  }
}
