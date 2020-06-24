package com.vonage;

import com.tino1b2be.audio.AudioFileException;
import com.tino1b2be.dtmfdecoder.DTMFDecoderException;
import com.tino1b2be.dtmfdecoder.DTMFUtil;
import ie.corballis.sox.SoXEncoding;
import ie.corballis.sox.Sox;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

public class DTMFDetector {

  private final DTMFUtil dtmfUtil;

  public DTMFDetector(File wavFile) {
    try {
      System.out.println("Detecting in file: " + wavFile.getName());
      File convertedFile = convertFile(wavFile);
      long start = System.currentTimeMillis();
      dtmfUtil = new DTMFUtil(convertedFile);
      boolean decoded = dtmfUtil.decode();
      convertedFile.delete();
      System.out.println(String.format("Detection took: %dms", System.currentTimeMillis() - start));
      if (!decoded) {
        throw new RuntimeException(String.format("Could not decode file '%s'", wavFile.getName()));
      }
    } catch (Exception e) {
      throw new RuntimeException("Couldn't create detector", e);
    }
  }

  private File convertFile(File file) throws Exception {
    System.out.println("Converting to " + PCM_SIGNED);
    long conversionStartTime = System.currentTimeMillis();
    String outputFileName = file.getName().substring(0, file.getName().length() - 4) + "_converted.wav";
    new Sox("/usr/local/bin/sox")
        .inputFile(file.getAbsolutePath())
        .bits(16)
        .encoding(SoXEncoding.SIGNED_INTEGER)
        .outputFile(outputFileName)
        .execute();
    System.out.println(String.format("Conversion took: %dms", System.currentTimeMillis() - conversionStartTime));
    return new File(outputFileName);
  }

  public int countDtmfDigits() {
    try {
      String bothChannels = dtmfUtil.getDecoded()[0] + dtmfUtil.getDecoded()[1];
      return (int) bothChannels.chars().filter(v -> v >= 48 && v <= 57).count();
    } catch (Exception e) {
      throw new RuntimeException("Could not count DTMF digits", e);
    }
  }
}
