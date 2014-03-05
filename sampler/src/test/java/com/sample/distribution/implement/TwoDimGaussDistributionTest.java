package com.sample.distribution.implement;

import java.util.Vector;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sample.distribution.Distribution;

public class TwoDimGaussDistributionTest {

    private Distribution towDimGaussion = null;

    @Before
    public void setUp() {

        towDimGaussion = new TwoDimGaussDistribution(1, 1, 0.3, 0.3, 0);
    }

    @Test
    public void testDensityFunction() {

        Vector<Double> x = new Vector<Double>();

        x.add(1.);
        x.add(1.);

        Double prob = towDimGaussion.pdf(x);

        Assert.assertEquals(1.76838, prob, 0.00001);

        x = new Vector<Double>();

        x.add(0.);
        x.add(0.);

        prob = towDimGaussion.pdf(x);
        Assert.assertEquals(2.6429161137785587E-5, prob, 0.00001);
        x = new Vector<Double>();

        x.add(5.);
        x.add(5.);

        prob = towDimGaussion.pdf(x);
        System.out.println("prob: " + prob);
        Assert.assertEquals(1.0956446647968073E-77, prob, 1E-77);

    }
    
    
    
    
    

}
