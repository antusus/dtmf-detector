package com.vonage;

import ie.corballis.sox.SoXEncoding;
import ie.corballis.sox.Sox;

import java.io.File;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

public class Application {
  public static void main(String[] args) throws Exception {
    decodeFile("with_dtmf_codes_123456789012345_mono.wav", 15);
    decodeFile("with_dtmf_codes_123456789012345_stereo.wav" ,15);
    decodeFile("with_dtmf_codes_12345_mono.wav", 5);
    decodeFile("with_dtmf_codes_12345_stereo.wav",5);
    decodeFile("output_200.wav",0);
    decodeFile("output_200_stereo.wav",0);
    decodeFile("output_1GB.wav",0);
    decodeFile("output_1GB_stereo.wav",0);
    decodeFile("MONO_998877665544332211.wav",18);
    decodeFile("STEREO_998877665544332211.wav",18);
    decodeFile("MONO_111_222_333_444_555_666_777_888_999_fast.wav", 27);
    decodeFile("STEREO_111_222_333_444_555_666_777_888_999_fast.wav", 27);
    decodeFile("GSM_MONO_DTMF_123456789.wav", 9);
    decodeFile("GSM_MONO_TWO_PARTIES_ENTERING_DTMF.wav", 16);
  }


  private static void decodeFile(String fileName, int expectedCount) throws Exception {
    System.out.println("-----------------------------------------------------------------------------------");
    File file = new File("/Users/kamil.berdychowski/Downloads/" + fileName);

    DTMFDetector detector = new DTMFDetector(file);
    System.out.println(String.format("Detected %d DTMF digits and expected %d", detector.countDtmfDigits(), expectedCount));
  }
}
