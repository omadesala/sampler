package com.sample.ui;

import com.sample.sampler.ISampler;
import com.sample.sampler.implement.GibbsSampler;

public class JavaGNUplot {

    private ISampler<Double> sampler = new GibbsSampler();;

    /**
     * 
     * @Description: main method for entry
     * @param args
     *            参数描述
     * @throws
     */
    public static void main(String[] args) {

        JavaGNUplot javaGNUplot = new JavaGNUplot();

        javaGNUplot.start();
    }

    /**
     * 
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param 参数描述
     * @throws
     */
    public void start() {

        sampler.doSample();
    }

}
