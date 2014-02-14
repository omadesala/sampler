package com.sample.ui;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.util.Iterator;
import java.util.Vector;

import com.sample.sampler.ISampler;
import com.sample.sampler.implement.inverse.TwoDimGaussSampler;

public class AcceptRejectSampleView extends Applet {

    private static final long serialVersionUID = 1L;
    private int[] histo; // histogram of the distribution
    private int histoCount = 100;
    private int displayWidth = 500;
    private int displayHeight = 500;
    private int maxValue = 0;
    private long acceptPtNumber = 0;

    // private ISampler<Double> sampler = null;
    private ISampler<Vector<Double>> sampler = null;

    public AcceptRejectSampleView() throws HeadlessException {
        super();
        this.sampler = new TwoDimGaussSampler();
        // this.sampler = new TwoDimStandardGaussSampler();
        // this.sampler = new MixdGaussMHSampler();
        // this.sampler = new GaussMHSampler();
        // this.sampler = new DoubleGaussSampler();

    }

    /**
     * draw the ui
     */
    @Override
    public void paint(Graphics g) {

        displaySimulatorDistribution(g);
        displaTargetDistribution(g);

        displayEfficiency(g);

    }

    /**
     * 
     * @Description: draw the sample efficiency for display
     * @param g
     *            参数描述
     * @return void 返回类型
     * @throws
     */
    private void displayEfficiency(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawString(
                "eff = " + ((double) calculateAccpetNumber())
                        / sampler.getSamplePointNum(), 30, 30);

    }

    /**
     * 
     * @Description: calculate the accept ratio
     * @param @return 参数描述
     * @return long 返回类型
     * @throws
     */
    private long calculateAccpetNumber() {
        for (int pt : histo) {
            acceptPtNumber += pt;
        }
        return acceptPtNumber;
    }

    /**
     * 
     * @Description: display the target distribution
     * @param @param g 参数描述
     * @return void 返回类型
     * @throws
     */
    private void displaTargetDistribution(Graphics g) {
        g.setColor(Color.BLUE);

        for (int i = 0; i < displayWidth; i++) {
            double b = displayHeight * Math.sqrt(2 * Math.PI);
            // the curve values are converted for the viewport (size = 500*500)

            /**
             * x1 is from [-5.0,5) x2 is from (-5.0,5]
             */
            // double x1 = (i - 250.) / 50;
            // double x2 = (i + 1 - 250.) / 50;

            Vector<Double> x1 = new Vector<Double>();
            Vector<Double> x2 = new Vector<Double>();
            x1.add((i - 250.) / 50);
            x2.add((i + 1 - 250.) / 50);

            int y1 = displayHeight
                    - (int) (b * sampler.getTargetDistribution()
                            .densityFunction(x1));
            int y2 = displayHeight
                    - (int) (b * sampler.getTargetDistribution()
                            .densityFunction(x2));

            g.drawLine(i, y1, i + 1, y2);

        }
    }

    /**
     * 
     * @Description: display the simulator distribution
     * @param @param g 参数描述
     * @return void 返回类型
     * @throws
     */
    private void displaySimulatorDistribution(Graphics g) {

        g.setColor(Color.RED);
        for (int i = 0; i < histoCount; i++) {
            int y = (int) (displayHeight * (1. - ((double) histo[i]) / maxValue));
            g.drawRect(5 * i, y, 5, displayHeight - y);
        }
    }

    @Override
    public void init() {
        super.init();
        this.setSize(displayWidth, displayHeight);

        sampler.doSample();

        initViewPoint();
        setViewPoint();
        findMaxValue();

    }

    /**
     * 
     * @Description:set view point
     * @param 参数描述
     * @return void 返回类型
     * @throws
     */
    private void setViewPoint() {

        Iterator<Vector<Double>> iterator = sampler.getSampleValues()
                .iterator();
        while (iterator.hasNext()) {

            int intValue = iterator.next().firstElement().intValue();
            if (intValue >= histo.length || intValue < 0) {
                continue;
            }
            histo[intValue]++;
        }

    }

    /**
     * 
     * @Description: init the view point
     * @param 参数描述
     * @return void 返回类型
     * @throws
     */
    private void initViewPoint() {
        histo = new int[histoCount];
        for (int k = 0; k < histoCount; k++) {
            histo[k] = 0;
        }
    }

    /**
     * 
     * @Description: find the max value
     * @param 参数描述
     * @return void 返回类型
     * @throws
     */
    private void findMaxValue() {
        // find the maximum of the histogram values for display
        maxValue = 0;
        for (int k = 0, len = histo.length; k < len; k++) {
            maxValue = Math.max(maxValue, histo[k]);
        }

    }

}
