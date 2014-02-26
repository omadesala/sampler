package com.sample.sampler.implement.inverse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import java.util.Random;
import java.util.Vector;

import Jama.CholeskyDecomposition;
import Jama.Matrix;

import com.panayotis.gnuplot.GNUPlotParameters;
import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.probablity.utils.Constant;
import com.sample.sampler.ISampler;

/**
 * @ClassName: TwoDStandardGaussSampler
 * @Description: this class is a sampler to generate tow dimension gaussian
 *               sample by inverse method
 * @author omadesala@msn.com
 * @date 2014-1-27 ����11:55:24
 */
public class TwoDimGaussSampler extends ISampler<Vector<Double>> {

    static final long serialVersionUID = 1L;

    private Matrix mean = null;
    private Matrix variance = null;

    private Random random = new Random();

    public TwoDimGaussSampler() {
        super();
        mean = new Matrix(2, 1);

        mean.set(0, 0, 1);
        mean.set(1, 0, 0);

        variance = new Matrix(2, 2);

        variance.set(0, 0, 5);
        variance.set(0, 1, 2);
        variance.set(1, 0, 2);
        variance.set(1, 1, 1);

    }

    public TwoDimGaussSampler(Matrix mean, Matrix var) {
        super();
        this.setMean(mean);
        this.setVariance(var);
    }

    @Override
    public void doSample() {

        generate();
        // displayData();
    }

    /**
     * @Description:generate sample for the given distribution
     * @throws
     */
    private void generate() {
        int i = 0;
        Matrix matrixX = new Matrix(2, 1);

        while (i++ < getSamplePointNum()) {

            Double t1 = random.nextDouble();
            Double t2 = random.nextDouble();

            Double x = Math.sqrt(-2 * Math.log(t2))
                    * Math.cos(2 * Math.PI * t1);
            Double y = Math.sqrt(-2 * Math.log(t2))
                    * Math.sin(2 * Math.PI * t1);

            matrixX.set(0, 0, x);
            matrixX.set(1, 0, y);

            Queue<Vector<Double>> sampleValues = getSampleValues();

            /**
             * transation for new mean and variance.
             */

            CholeskyDecomposition dec = new CholeskyDecomposition(getVariance());

            Matrix matrixL = dec.getL();

            Matrix ret = getMean().plus(matrixL.times(matrixX));

            Double x1 = ret.get(0, 0);
            Double y1 = ret.get(1, 0);

            Vector<Double> element = new Vector<Double>();

            element.add(x1);
            element.add(y1);

            sampleValues.add(element);
        }
    }

    /**
     * @Description: display the result
     * @param 参数描述
     * @throws
     */
    @SuppressWarnings("unused")
    private void displayData() {

        GNUPlotParameters param = new GNUPlotParameters(false);

        ArrayList<String> preInit = param.getPreInit();

        // preInit.add("set contour base");// draw contour
        preInit.add("set xrange [-5:5]");// draw contour
        preInit.add("set yrange [-5:5]");// draw contour
        preInit.add("set size square");// draw contour

        JavaPlot p = new JavaPlot(param, Constant.GNUPLOT_PATH, null);

        p.setTitle("two dim gaussian Sample Demo");
        p.getAxis("x").setLabel("X1 axis", "Arial", 20);
        p.getAxis("y").setLabel("X2 axis");

        Queue<Vector<Double>> sampleValues = getSampleValues();

        double[][] points = new double[sampleValues.size()][2];

        Iterator<Vector<Double>> iterator = sampleValues.iterator();

        int i = 0;
        while (iterator.hasNext()) {

            Vector<Double> next = iterator.next();

            points[i][0] = next.firstElement();
            points[i][1] = next.lastElement();

            i++;
        }

        DataSetPlot s = new DataSetPlot(points);
        p.addPlot(s);

        p.addPlot("0; pause 1000;");
        p.plot();
    }

    public Matrix getMean() {
        return mean;
    }

    public void setMean(Matrix mean) {
        this.mean = mean;
    }

    public Matrix getVariance() {
        return variance;
    }

    public void setVariance(Matrix variance) {
        this.variance = variance;
    }
}
