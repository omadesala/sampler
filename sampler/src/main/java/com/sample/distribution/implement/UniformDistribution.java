package com.sample.distribution.implement;

import com.sample.distribution.Distribution;

public class UniformDistribution extends Distribution {

    @Override
    public double densityFunction(double... x) {
        // the special distribute is uniform distribution which across the
        // interval [-5,5]
        return 1 / 10.;
    }

    @Override
    public double sampleOnePoint(double... x) {
        return 10. * (Math.random() - 0.5);
    }

}
