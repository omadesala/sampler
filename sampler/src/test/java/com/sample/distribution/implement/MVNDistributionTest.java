package com.sample.distribution.implement;

import java.util.Random;
import java.util.Vector;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import Jama.Matrix;

import com.probablity.utils.MatrixUtils;
import com.sample.distribution.Distribution;

public class MVNDistributionTest {

    private Random random = new Random();
    private Distribution mvn = null;
    private Distribution towDimGaussion = null;

    @Before
    @Ignore
    public void setUp() throws Exception {

        double[][] mean = new double[2][1];

        mean[0][0] = 0.;
        mean[1][0] = 0.;

        double[][] variance = new double[2][2];

        variance[0][0] = 0.09;
        variance[0][1] = 0.001;
        variance[1][0] = 0.001;
        variance[1][1] = 0.09;

        Matrix mu = new Matrix(mean);
        Matrix var = new Matrix(variance);
        mvn = new MVNDistribution(mu, var);
        // notice that -1=<rho <= 1
        double rho = variance[0][1] / (Math.sqrt(variance[0][0] * variance[1][1]));

        towDimGaussion = new TwoDimGaussDistribution(mean[0][0], mean[1][0], Math.sqrt(variance[0][0]),
                Math.sqrt(variance[1][1]), rho);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    @Ignore
    public void testPdfValue() {

        Vector<Double> point = new Vector<Double>();

        for (int i = 0; i < 100; i++) {

            point = new Vector<Double>();
            point.add(random.nextDouble() * 20 - 10);
            point.add(random.nextDouble() * 20 - 10);

            // MatrixUtils.printVectorPoint(point);

            double actural = mvn.pdf(point);
            double expected = towDimGaussion.pdf(point);

            System.out.println("actural=" + actural);

            Assert.assertEquals(expected, actural, 1E-10);
        }

    }

    @Test
    public void testPdfValueWithMean() {

        Vector<Double> point = new Vector<Double>();

        point.add(0.2481290263490381);
        point.add(11.10630942556631);

        double[][] mean = new double[2][1];

        mean[0][0] = 0.6026331811823153;
        mean[1][0] = 0.9830870764949878;

        double[][] variance = new double[2][2];

        variance[0][0] = 0.3526403987542951;
        variance[0][1] = 0.8800544647645608;
        variance[1][0] = 0.8800544647645608;
        variance[1][1] = 0.8143063266204733;

        Matrix mu = new Matrix(mean);
        Matrix var = new Matrix(variance);
        mvn = new MVNDistribution(mu, var);
        // notice that -1=<rho <= 1
        double rho = variance[0][1] / (Math.sqrt(variance[0][0] * variance[1][1]));

        Assert.assertTrue(rho >= 0 && rho <= 1.);
        System.out.println("rho : " + rho);

        towDimGaussion = new TwoDimGaussDistribution(mean[0][0], mean[1][0], Math.sqrt(variance[0][0]),
                Math.sqrt(variance[1][1]), rho);

        double actural = mvn.pdf(point);
        System.out.println("actural=" + actural);

        Double pdf = towDimGaussion.pdf(point);
        System.out.println("twoG actural=" + pdf);

        Assert.assertEquals(2.09175490008312448E18, actural, 1E-10);

    }

    @Test
    @Ignore
    public void testDensityFunction() {

        Vector<Double> point = new Vector<Double>();

        for (int i = 0; i < 100; i++) {

            point = new Vector<Double>();
            point.add(random.nextDouble() * 0.1);
            point.add(random.nextDouble() * 0.1);

            MatrixUtils.printVectorPoint(point);

            double actural = mvn.pdf(point);
            double expected = towDimGaussion.pdf(point);

            Assert.assertEquals(expected, actural, 1E-10);
        }

    }
}
