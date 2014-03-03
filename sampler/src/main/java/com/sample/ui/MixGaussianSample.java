package com.sample.ui;

import java.util.Iterator;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingDeque;

import Jama.Matrix;

import com.probablity.utils.GnuPlotDisplay;
import com.sample.sampler.ISampler;
import com.sample.sampler.implement.inverse.TwoDimGaussSampler;

public class MixGaussianSample {

    private Matrix mu1 = new Matrix(2, 1);
    private Matrix mu2 = new Matrix(2, 1);
    private Matrix mu3 = new Matrix(2, 1);

    private Matrix sigma1 = new Matrix(2, 2);
    private Matrix sigma2 = new Matrix(2, 2);
    private Matrix sigma3 = new Matrix(2, 2);

    private Queue<Vector<Double>> samples = new LinkedBlockingDeque<Vector<Double>>();

    private ISampler<Vector<Double>> sampler;

    /**
     * @Description: main method for entry point
     * @param args
     *            参数描述
     * @throws
     */
    public static void main(String[] args) {

        MixGaussianSample javaGNUplot = new MixGaussianSample();

        javaGNUplot.start();
    }

    /**
     * @Description: start to do some real work
     * @param 参数描述
     * @throws
     */
    public void start() {

        init();
        sampler = new TwoDimGaussSampler(mu1, sigma1);
        sampler.doSample();
        Queue<Vector<Double>> ret1 = sampler.getSampleValues();

        Iterator<Vector<Double>> iterator = ret1.iterator();
        while (iterator.hasNext()) {
            getSamples().add(iterator.next());
        }

        sampler = new TwoDimGaussSampler(mu2, sigma2);
        sampler.doSample();
        Queue<Vector<Double>> ret2 = sampler.getSampleValues();
        iterator = ret2.iterator();
        while (iterator.hasNext()) {
            getSamples().add(iterator.next());
        }

        sampler = new TwoDimGaussSampler(mu3, sigma3);
        sampler.doSample();
        Queue<Vector<Double>> ret3 = sampler.getSampleValues();
        iterator = ret3.iterator();
        while (iterator.hasNext()) {
            getSamples().add(iterator.next());
        }

        // displayData();

    }

    /**
     * @Description: initial the state
     * @throws
     */
    private void init() {

        /**
         * inital mean
         */
        mu1.set(0, 0, 1);
        mu1.set(1, 0, 2);

        mu2.set(0, 0, 5);
        mu2.set(1, 0, 8);

        mu3.set(0, 0, 5);
        mu3.set(1, 0, 12);

        /**
         * initial sigma matrix
         */
        sigma1.set(0, 0, 5);
        sigma1.set(0, 1, 2);
        sigma1.set(1, 0, 2);
        sigma1.set(1, 1, 1);

        sigma2.set(0, 0, 3);
        sigma2.set(0, 1, -2);
        sigma2.set(1, 0, -2);
        sigma2.set(1, 1, 3);

        sigma3.set(0, 0, 5);
        sigma3.set(0, 1, 2);
        sigma3.set(1, 0, 2);
        sigma3.set(1, 1, 1);

    }

    /**
     * @return TODO
     * @Description: display the data
     * @param 参数描述
     * @throws
     */
    @SuppressWarnings("unused")
    private void displayData() {

        GnuPlotDisplay.display2D(getSampleArray());

        // GNUPlotParameters param = new GNUPlotParameters(false);
        //
        // ArrayList<String> preInit = param.getPreInit();
        //
        // // preInit.add("set contour base");// draw contour
        // preInit.add("set xrange [-5:20]");// draw contour
        // preInit.add("set yrange [-5:20]");// draw contour
        // preInit.add("set size square");// draw contour
        //
        // JavaPlot p = new JavaPlot(param, Constant.GNUPLOT_PATH, null);
        //
        // p.setTitle("two dim gaussian Sample Demo");
        // p.getAxis("x").setLabel("X1 axis", "Arial", 20);
        // p.getAxis("y").setLabel("X2 axis");
        //
        // DataSetPlot s = new DataSetPlot(getSampleArray());
        // p.addPlot(s);
        //
        // p.addPlot("0; pause 1000;");
        // p.plot();

    }

    private double[][] getSampleArray() {

        Queue<Vector<Double>> sampleValues = getSamples();

        double[][] points = new double[sampleValues.size()][2];

        Iterator<Vector<Double>> iterator = sampleValues.iterator();

        int i = 0;
        while (iterator.hasNext()) {

            Vector<Double> next = iterator.next();

            points[i][0] = next.firstElement();
            points[i][1] = next.lastElement();

            i++;
        }
        return points;
    }

    @SuppressWarnings("unused")
    private Matrix getSampleMatrix() {

        Queue<Vector<Double>> sampleValues = getSamples();

        double[][] points = new double[2][sampleValues.size()];

        Iterator<Vector<Double>> iterator = sampleValues.iterator();

        int i = 0;
        while (iterator.hasNext()) {

            Vector<Double> next = iterator.next();

            points[0][i] = next.firstElement();
            points[1][i] = next.lastElement();

            i++;
        }
        return new Matrix(points);
    }

    public Queue<Vector<Double>> getSamples() {
        return samples;
    }

    public void setSamples(Queue<Vector<Double>> samples) {
        this.samples = samples;
    }

}
