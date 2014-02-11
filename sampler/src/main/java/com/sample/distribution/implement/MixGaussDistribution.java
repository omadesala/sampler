package com.sample.distribution.implement;

import com.sample.distribution.Distribution;

/**
 * two 1D mixed gauss distribution
 */
public class MixGaussDistribution extends Distribution {

    @Override
    public double densityFunction(double... x) {

        Distribution gauss1 = new GaussDistribution();
        gauss1.setMean(3);
        gauss1.setVariance(1);

        Distribution gauss2 = new GaussDistribution();
        gauss2.setMean(0);
        gauss2.setVariance(1);

        return 0.3 * gauss1.densityFunction(x[0]) + 0.7
                * gauss2.densityFunction(x[0]);
    }

    @Override
    public double sampleOnePoint(double... x) {
        // TODO this need to be improvement, it violate Liskov Substitution
        // principle
        return 0;
    }

}
