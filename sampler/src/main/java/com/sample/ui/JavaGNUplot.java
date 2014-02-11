package com.sample.ui;

import com.sample.sampler.ISampler;
import com.sample.sampler.implement.GibbsSampler;

public class JavaGNUplot {

    private ISampler<Double> sampler = new GibbsSampler();;

    public static void main(String[] args) {

        JavaGNUplot javaGNUplot = new JavaGNUplot();

        javaGNUplot.start();
    }

    public void start() {

        sampler.doSample();
    }

}
