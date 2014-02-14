package com.sample.sampler.implement;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import com.google.common.collect.Queues;
import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.AbstractPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.FillStyle;
import com.panayotis.gnuplot.style.NamedPlotColor;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.gnuplot.style.FillStyle.Fill;
import com.sample.distribution.implement.TwoDimGaussDistribution;
import com.sample.sampler.ISampler;

/**
 * just only implement ISampler can builder all kinds of distribution.</br>
 * <p>
 * first, implement a target distribution.
 * </p>
 * <p>
 * second,implement the proposal distribution.
 * </p>
 * <p>
 * third,choose the constant a for sampler
 * </p>
 */
public class GibbsSampler extends ISampler<Double> {

    static final long serialVersionUID = 1L;
    private static final long N = 100000;
    private Random random = new Random();
    private Map<Double, Double> record = new HashMap<Double, Double>();

    public GibbsSampler() {
        super();

        setTargetDistribution(new TwoDimGaussDistribution(1., 2., 1., 1., 0.1));

    }

    @Override
    public void doSample() {

        burnIn();
        displayData();
    }

    /**
     * 
     * @Description: training until get the station phrase
     * @throws
     */
    private void burnIn() {

        // step 1. initinal x1 and x2 with zero
        double x1 = 0, x2 = 0;
        // step 2. loop until converge
        for (int t = 0; t < N; t++) {

            // step 2.1 sample x1(t+1) from x2(t);
            x1 = sampleX1GivenX2(x2);
            // step 2.2 sample x2(t+1) from x1(t+1);
            x2 = sampleX2GivenX1(x1);
            // save the sample point

            saveSample(x1, x2);
        }

    }

    /**
     * 
     * @Description: save the sample point
     * @param x1
     * @param x2
     * @throws
     */
    private void saveSample(double x1, double x2) {
        record.put(x1, x2);
    }

    /**
     * 
     * @Description: sample x1 by given x2
     * @param x2
     * @return double return sample point
     * @throws
     */
    private double sampleX1GivenX2(double x2) {

        double mu1 = 0.1 * x2 + 0.8;
        double delta1 = Math.sqrt(0.99);

        return random.nextGaussian() * delta1 + mu1;

    }

    private double sampleX2GivenX1(double x1) {

        double mu2 = 0.1 * x1 + 1.9;
        double delta2 = Math.sqrt(0.99);
        return random.nextGaussian() * delta2 + mu2;
    }

    private void displayData() {

        JavaPlot p = new JavaPlot("E:/gnuplot/bin/gnuplot.exe", false);

        p.setTitle("Gibbs Sample Demo");
        p.getAxis("x").setLabel("X1 axis", "Arial", 20);
        p.getAxis("y").setLabel("X2 axis");

        p.getAxis("x").setBoundaries(-5, 5);
        p.getAxis("y").setBoundaries(-5, 8);

        int size = record.size();

        double points[][] = new double[size][2];

        Iterator<Double> iterator = record.keySet().iterator();

        int i = 0, j = 0;
        while (iterator.hasNext()) {

            Double x = iterator.next();
            points[i][0] = x;
            Double y = record.get(x);
            points[i][1] = y;

            i++;
        }

        double[][] plot = { { 1, 1.1 }, { 2, 2.2 }, { 3, 3.3 }, { 4, 4.3 } };

        // points.

        double sample[][] = new double[1000][2];

        i = 0;
        for (int k = 0; k < points.length; k++) {

            if (k < N - 1000)
                continue;

            sample[i][0] = points[k][0];
            sample[i++][1] = points[k][1];
        }

        // DataSetPlot s = new DataSetPlot(plot);
        // DataSetPlot s = new DataSetPlot(points);
        DataSetPlot s = new DataSetPlot(sample);
        p.addPlot(s);

        p.addPlot("0; pause 1000;");
        p.plot();
    }
}