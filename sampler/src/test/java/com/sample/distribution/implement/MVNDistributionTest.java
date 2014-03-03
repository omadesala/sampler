package com.sample.distribution.implement;

import java.util.Random;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Jama.Matrix;

import com.probablity.utils.GnuPlotDisplay;
import com.sample.distribution.Distribution;

public class MVNDistributionTest {

    Distribution mvn = null;

    @Before
    public void setUp() throws Exception {

        double[][] mean = new double[2][1];

        mean[0][0] = 1.;
        mean[1][0] = 1.;

        double[][] variance = new double[2][2];

        variance[0][0] = 0.9;
        variance[0][1] = 0.;
        variance[1][0] = 0.;
        variance[1][1] = 0.9;

        Matrix mu = new Matrix(mean);
        Matrix var = new Matrix(variance);
        mvn = new MVNDistribution(mu, var);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDensityFunction() {

        int length = 10;
        double[][] samples = new double[length][3];

        Random random = new Random();
        for (int i = 0; i < length; i++) {

            samples[i][0] = random.nextDouble() - 0.5;// * 10. - 5.;
            samples[i][1] = random.nextDouble() - 0.5;// * 10. - 5.;

            Vector<Double> pointDoubles = new Vector<Double>();
            pointDoubles.add(samples[i][0]);
            pointDoubles.add(samples[i][1]);

            samples[i][2] = mvn.densityFunction(pointDoubles);

            System.out.println("v1:" + samples[i][0]);
            System.out.println("v2:" + samples[i][1]);
            System.out.println("p:" + samples[i][2]);

        }

        // GnuPlotDisplay.display3D(samples);

    }
}
