package com.application.audio;

import org.apache.commons.math3.transform.FastFourierTransformer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.musicg.graphic.GraphicRender;
import com.musicg.wave.Wave;
import com.musicg.wave.extension.Spectrogram;

public class FFTTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testForward() {
        // String inFolder = "audio_work";
        String inFolder = "E:\\googlesvn\\ML\\repo\\sampler\\sampler\\src\\main\\resources\\";
        String outFolder = "E:\\googlesvn\\out";
        String filename = "cock_a_1.wav";

        // create a wave object
        Wave wave = new Wave(inFolder + "/" + filename);
        Spectrogram spectrogram = new Spectrogram(wave);

        // Graphic render
        GraphicRender render = new GraphicRender();
        // render.setHorizontalMarker(1);
        // render.setVerticalMarker(1);
        render.renderSpectrogram(spectrogram, outFolder + "/" + filename
                + ".jpg");

        // change the spectrogram representation
        int fftSampleSize = 1024;
        int overlapFactor = 0;
        spectrogram = new Spectrogram(wave, fftSampleSize, overlapFactor);
        render.renderSpectrogram(spectrogram, outFolder + "/" + filename
                + "2.jpg");
    }

}
