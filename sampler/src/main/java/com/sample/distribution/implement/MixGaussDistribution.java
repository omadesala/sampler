package com.sample.distribution.implement;

import java.util.Vector;

import com.google.common.base.Preconditions;
import com.sample.distribution.Distribution;

/**
 * two 1D mixed gauss distribution
 */
public class MixGaussDistribution extends Distribution {

    @Override
    public Double pdf(Vector<Double> x) {

        Distribution gauss1 = new GaussDistribution();
        gauss1.setMean(3);
        gauss1.setVariance(1);

        Distribution gauss2 = new GaussDistribution();
        gauss2.setMean(0);
        gauss2.setVariance(1);

        Vector<Double> point1 = new Vector<Double>();
        Vector<Double> point2 = new Vector<Double>();

        point1.add(x.firstElement() + 0.7);
        point1.add(x.firstElement());

        return 0.3 * gauss1.pdf(point1)
                * gauss2.pdf(point2);
    }

    @Override
    public Vector<Double> sampleOnePoint(double... x) {
        // TODO this need to be improvement, it violate Liskov Substitution
        // principle
        return new Vector<Double>();
    }

    @Override
    public void setParameter(Vector<Double> param) {
        Preconditions.checkNotNull(param);

        mean = param.firstElement();
        variance = param.elementAt(1);

    }

}
