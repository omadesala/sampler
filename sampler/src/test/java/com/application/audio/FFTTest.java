package com.application.audio;

import static org.junit.Assert.*;

import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FFTTest {

    private FastFourierTransformer fft = null;
    
    
    @Before
    public void setUp() throws Exception {
        
        DftNormalization normalization;
        fft = new FastFourierTransformer(normalization);
        
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testForward() {

        
        
    }

}
