package com.sample.distribution;

public abstract class Distribution {

    protected double mean = 0;
    protected double variance = 1;

    abstract public double densityFunction(double... x);

    /**
     * some times ,we need condition sample this is not a good understand but it
     * suit to multi-variance random
     *
     * @param x
     * @return
     */
    abstract public double sampleOnePoint(double... x);

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getVariance() {
        return variance;
    }

    public void setVariance(double variance) {
        this.variance = variance;
    }
}
