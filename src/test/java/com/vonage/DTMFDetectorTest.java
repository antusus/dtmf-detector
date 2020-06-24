package com.vonage;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

class DTMFDetectorTest {

  @Test
  void countDtmfDigitsOnly() {
    File wavFile = new File(this.getClass().getResource("/sampleFiles/GSM_MONO_TWO_PARTIES_ENTERING_DTMF.wav").getPath());
    DTMFDetector detector = new DTMFDetector(wavFile);

    Assertions.assertThat(detector.countDtmfDigits()).isEqualTo(16);
  }
}