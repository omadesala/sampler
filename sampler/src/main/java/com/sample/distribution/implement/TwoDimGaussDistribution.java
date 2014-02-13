package com.sample.distribution.implement;

import com.sample.distribution.Distribution;

/**
 * the target distribution is gauss distribution f(x)=
 * 1/(sqrt(2*pi)*variance)exp(-(x-mean)^2/(2*variance^2))
 */
public class TwoDimGaussDistribution extends Distribution {

    // private Random random = new Random();

    private double mean1 = .0;
    private double mean2 = .0;
    private double delta1 = .0;
    private double delta2 = .0;
    private double rho = .0;

    public TwoDimGaussDistribution(double mu1, double mu2, double delta1,
            double delta2, double rho) {

        this.mean1 = mu1;
        this.mean2 = mu2;
        this.delta1 = delta1;
        this.delta2 = delta2;
        this.rho = rho;

    }

    @Override
    public double densityFunction(double... x) {

        double x1 = x[0], x2 = x[1];

        double constant = 1
                / 2.
                * Math.PI
                * Math.sqrt(delta1 * delta1 * delta2 * delta2 * (1 - rho * rho));

        return constant
                * Math.exp(-1.
                        / (2 * (1 - rho * rho))
                        * ((x1 - mean1)
                                * (x1 - mean1)
                                / (delta1 * delta1)
                                - (2 * rho * (x1 - mean1) * (x2 - mean2) / (delta1 * delta2)) + (x2 - mean2)
                                * (x2 - mean2) / (delta2 * delta2)));

    }

    @Override
    public double sampleOnePoint(double... x) {
        return 0;
        // return random.nextGaussian() * variance + mean;

    }
}
