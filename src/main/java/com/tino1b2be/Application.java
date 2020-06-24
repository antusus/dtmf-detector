package com.tino1b2be;

import com.tino1b2be.audio.AudioFileException;
import com.tino1b2be.audio.WavFile;
import com.tino1b2be.dtmfdecoder.DTMFDecoderException;
import com.tino1b2be.dtmfdecoder.DTMFUtil;
import com.tino1b2be.dtmfdecoder.FileUtil;

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
    decodeFile("with_dtmf_codes_123456789012345_mono.wav");
    decodeFile("with_dtmf_codes_123456789012345_stereo.wav");
    decodeFile("with_dtmf_codes_12345_mono.wav");
    decodeFile("with_dtmf_codes_12345_stereo.wav");
    decodeFile("output_200.wav");
    decodeFile("output_200_stereo.wav");
    decodeFile("output_1GB.wav");
    decodeFile("output_1GB_stereo.wav");
  }

  private static void decodeFile(String fileName) throws Exception {
    System.out.println("-----------------------------------------------------------------------------------");
    File file = new File("/Users/kamil.berdychowski/Downloads/" + fileName);
    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
    AudioFormat format = audioInputStream.getFormat();
    System.out.println("Format: " +  format);
    boolean requiresConversion = format.getEncoding() != PCM_SIGNED;
    if(requiresConversion) {
      file = convertFile(file, audioInputStream);
    }
    audioInputStream.close();
    ((WavFile) FileUtil.readAudioFile(file)).display();

    System.out.println(String.format("File size %dKB", file.length() / 1024));
    long start = System.currentTimeMillis();
    DTMFUtil dtmf = new DTMFUtil(file);
    boolean decode = dtmf.decode();
    System.out.println("Decoded: " + decode);
    System.out.println("DTMF in channel 0: " + dtmf.getDecoded()[0]);
    System.out.println("DTMF in channel 1: " + dtmf.getDecoded()[1]);
    System.out.println(String.format("Took: %dms", System.currentTimeMillis() - start));
    if(requiresConversion) {
      System.out.println("Removing TEMP file: " + file.delete());
    }
  }

  private static File convertFile(File file, AudioInputStream audioInputStream) throws IOException {
    System.out.println("Requires conversion to " + PCM_SIGNED);
    long conversionStartTime = System.currentTimeMillis();
    AudioInputStream pcmAudioStream = AudioSystem.getAudioInputStream(PCM_SIGNED, audioInputStream);
    File converted = new File(file.getName().substring(0, file.getName().length() - 4) + "_converted.wav");
    AudioSystem.write(pcmAudioStream, AudioFileFormat.Type.WAVE, converted);
    pcmAudioStream.close();
    System.out.println(String.format("Conversion took: %dms", System.currentTimeMillis() - conversionStartTime));
    return converted;
  }
}
