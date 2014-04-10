package com.application.audio;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.math3.complex.Complex;

import com.google.common.collect.Lists;

public class FileUtil {

    private static final int BUFFER_SIZE = 1024;
    private int channels;
    private float frameRate;
    private int frameSize;
    private float sampleRate;
    private int sampleSizeInBits;

    public void spectrum() throws Exception {

        String fileName = "d:\\music\\father.mp3";

        File file = new File(fileName);
        if (!file.exists()) {
            throw new Exception(fileName + " is not exist!");
        }
        AudioInputStream ais = null;
        AudioFormat baseFormat = null;
        DataLine.Info info = null;
        SourceDataLine line = null;
        List<Integer> vector = Lists.newArrayList();
        try {
            ais = AudioSystem.getAudioInputStream(file);
            baseFormat = ais.getFormat();

            info = new DataLine.Info(SourceDataLine.class, baseFormat);
            // 提取信息
            this.channels = baseFormat.getChannels();
            this.frameRate = baseFormat.getFrameRate();
            this.frameSize = baseFormat.getFrameSize();
            this.sampleRate = baseFormat.getSampleRate();
            this.sampleSizeInBits = baseFormat.getSampleSizeInBits();

            line = (SourceDataLine) AudioSystem.getLine(info);

            line.open(baseFormat);
            line.start();
            int intBytes = 0;
            vector.clear();

            while (intBytes != -1) {
                byte[] buf = new byte[BUFFER_SIZE];
                intBytes = ais.read(buf, 0, BUFFER_SIZE);// 从音频流读取指定的最大数量的数据字节，并将其放入给定的字节数组中。
                for (int c = 0; c < intBytes; c++) {
                    // System.out.println(buf[c]);
                    int value = buf[c] > 0 ? buf[c] : (buf[c] + 256);
                    vector.add(value);
                }
                // 播放代码
                // if (intBytes >= 0) {
                // int outBytes = line.write(buf, 0, intBytes);//
                // 通过此源数据行将音频数据写入混频器。
                // }
            }

        } catch (UnsupportedAudioFileException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        float[] f = new float[vector.size()];
        File ff = new File("e:\\music\\pcm.txt");
        FileWriter fw = null;
        try {
            fw = new FileWriter(ff);
            for (int c = 0; c < vector.size(); c++) {
                f[c] = vector.get(c);
                fw.write("" + f[c] + "\r\n");
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } finally {
            try {
                fw.flush();
                fw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        Complex[] comples = new Complex[1024];
        for (int c = 0; c < f.length; c++) {
            comples[c] = new Complex(f[c], 0);
        }
        for (int c = 971; c < 1024; c++) {
            comples[c] = new Complex(0, 0);
        }

        Complex[] values = FFT.fft(comples);
        double[] d = FFT.normalize(values);

        File fff = new File("e:\\music\\pcm2.txt");
        FileWriter fw2;
        try {
            fw2 = new FileWriter(fff);
            for (int c = 0; c < d.length; c++) {
                fw2.write("" + d[c] + "\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        FileUtil fileUtil = new FileUtil();
        try {
            fileUtil.spectrum();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
