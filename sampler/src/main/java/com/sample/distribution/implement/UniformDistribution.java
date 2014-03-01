package com.sample.distribution.implement;

import java.util.Random;
import java.util.Vector;

import com.google.common.base.Preconditions;
import com.sample.distribution.Distribution;

public class UniformDistribution extends Distribution {

    private double a = 0;
    private double b = 1;

    private Random random = new Random();

    @Override
    public Double densityFunction(Vector<Double> x) {
        // the special distribute is uniform distribution which across the
        // interval [-5,5]
        return 1. / (b - a);
    }

    @Override
    public Vector<Double> sampleOnePoint(double... x) {

        Vector<Double> point = new Vector<Double>();
        point.add(a + (b - a) * random.nextDouble());

        return point;
    }

    @Override
    public void setParameter(Vector<Double> param) {
        Preconditions.checkNotNull(param);

        Preconditions.checkNotNull(param);
        Preconditions.checkArgument(param.size() == 2);

        setParameter(param);

        a = param.firstElement();
        b = param.elementAt(1);

    }

}
