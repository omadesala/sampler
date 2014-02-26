package com.sample.sampler.implement.inverse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import java.util.Random;
import java.util.Vector;

import com.panayotis.gnuplot.GNUPlotParameters;
import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.probablity.utils.Constant;
import com.sample.sampler.ISampler;

/**
 * @ClassName: TwoDStandardGaussSampler
 * @Description: this class is a sampler to generate tow dimension standard
 *               gaussian sample by inverse method
 * @author omadesala@msn.com
 * @date 2014-1-27 11:55:24
 */
public class TwoDimStandardGaussSampler extends ISampler<Vector<Double>> {

    static final long serialVersionUID = 1L;

    private Random random = new Random();

    public TwoDimStandardGaussSampler() {
        super();
        // setTargetDistribution(null);
        // UniformDistribution uniformDistribution = new UniformDistribution();
        // uniformDistribution.setParameter(0., 1.);
        // setProposalDistribution(uniformDistribution);
    }

    public TwoDimStandardGaussSampler(Double mean, Double variance) {
        super();
        // setTargetDistribution(null);
        // UniformDistribution uniformDistribution = new UniformDistribution();
        // uniformDistribution.setParameter(0., 1.);
        // setProposalDistribution(uniformDistribution);
    }

    @Override
    public void doSample() {

        generate();
        displayData();
    }

    /**
     * @Description: generate the sample for the given distribution
     * @param 参数描述
     */
    private void generate() {
        int i = 0;
        while (i++ < getSamplePointNum()) {

            Double t1 = random.nextDouble();
            Double t2 = random.nextDouble();

            Double x = Math.sqrt(-2 * Math.log(t2))
                    * Math.cos(2 * Math.PI * t1);
            Double y = Math.sqrt(-2 * Math.log(t2))
                    * Math.sin(2 * Math.PI * t1);

            Queue<Vector<Double>> sampleValues = getSampleValues();

            Vector<Double> element = new Vector<Double>();

            element.add(x);
            element.add(y);

            sampleValues.add(element);
        }
    }

    /**
     * @Description: display the data for user
     * @throws
     */
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
}
