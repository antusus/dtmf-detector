package com.tino1b2be;

import com.tino1b2be.audio.AudioFileException;
import com.tino1b2be.audio.WavFile;
import com.tino1b2be.dtmfdecoder.DTMFDecoderException;
import com.tino1b2be.dtmfdecoder.DTMFUtil;
import com.tino1b2be.dtmfdecoder.FileUtil;
import ie.corballis.sox.SoXEncoding;
import ie.corballis.sox.Sox;
import ie.corballis.sox.WrongParametersException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioPermission;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

import static javax.sound.sampled.AudioFormat.Encoding.ALAW;
import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

public class Application {
  public static void main(String[] args) throws Exception {
//    decodeFile("with_dtmf_codes_123456789012345_mono.wav");
//    decodeFile("with_dtmf_codes_123456789012345_stereo.wav");
//    decodeFile("with_dtmf_codes_12345_mono.wav");
//    decodeFile("with_dtmf_codes_12345_stereo.wav");
//    decodeFile("output_200.wav");
//    decodeFile("output_200_stereo.wav");
//    decodeFile("output_1GB.wav");
//    decodeFile("output_1GB_stereo.wav");
//    decodeFile("MONO_998877665544332211.wav");
//    decodeFile("STEREO_998877665544332211.wav");
//    decodeFile("MONO_111_222_333_444_555_666_777_888_999_fast.wav");
//    decodeFile("STEREO_111_222_333_444_555_666_777_888_999_fast.wav");
//    decodeFile("GSM_MONO_DTMF_123456789.wav");
//    decodeFile("GSM_MONO_TWO_PARTIES_ENTERING_DTMF.wav");
  }

  private static void decodeFile(String fileName) throws Exception {
    System.out.println("-----------------------------------------------------------------------------------");
    File file = new File("/Users/kamil.berdychowski/Downloads/" + fileName);
    file = convertFile(file);

    ((WavFile) FileUtil.readAudioFile(file)).display();
    System.out.println(String.format("File size %dKB", file.length() / 1024));
    long start = System.currentTimeMillis();

    DTMFUtil dtmf = new DTMFUtil(file);
    boolean decode = dtmf.decode();
    System.out.println("Decoded: " + decode);
    System.out.println("DTMF in channel 0: " + dtmf.getDecoded()[0]);
    System.out.println("DTMF in channel 1: " + dtmf.getDecoded()[1]);
    System.out.println(String.format("Took: %dms", System.currentTimeMillis() - start));
    System.out.println("Removing TEMP file: " + file.delete());
  }

  private static File convertFile(File file) throws Exception {
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

}
