package com.tino1b2be;

import com.tino1b2be.audio.AudioFileException;
import com.tino1b2be.audio.WavFile;
import com.tino1b2be.dtmfdecoder.DTMFDecoderException;
import com.tino1b2be.dtmfdecoder.DTMFUtil;
import com.tino1b2be.dtmfdecoder.FileUtil;

import java.io.File;
import java.io.IOException;

public class Application {
  public static void main(String[] args) throws DTMFDecoderException, IOException, AudioFileException {
    decodeFile("with_dtmf_codes_123456789012345_mono.wav");
    decodeFile("with_dtmf_codes_12345_mono.wav");
//    decodeFile("output_200.wav");
//    decodeFile("output_1GB.wav");
  }

  private static void decodeFile(String fileName) throws AudioFileException, IOException, DTMFDecoderException {
    System.out.println("-----------------------------------------------------------------------------------");
    ((WavFile) FileUtil.readAudioFile("/Users/kamil.berdychowski/Downloads/" + fileName)).display();
    File file = new File("/Users/kamil.berdychowski/Downloads/" + fileName);
    System.out.println(String.format("File size %dKB", file.length() / 1024));
    long start = System.currentTimeMillis();
    DTMFUtil dtmf = new DTMFUtil("/Users/kamil.berdychowski/Downloads/" + fileName);
    boolean decode = dtmf.decode();
    System.out.println("0: " + dtmf.getDecoded()[0]);
    System.out.println("1: " + dtmf.getDecoded()[1]);
    System.out.println(String.format("Took: %dms", System.currentTimeMillis() - start));
  }
}
