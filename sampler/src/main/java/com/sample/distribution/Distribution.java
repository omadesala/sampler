package com.sample.distribution;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public abstract class Distribution {

	protected double mean = 0;
	protected double variance = 1;

	private List<Vector<Double>> param = new ArrayList<Vector<Double>>();

	abstract public void setParameter(Vector<Double> param);

	abstract public double densityFunction(Vector<Double> x);

	/**
	 * some times ,we need condition sample this is not a good understand but it
	 * suit to multi-variance random
	 * 
	 * @param x
	 * @return
	 */
	abstract public Vector<Double> sampleOnePoint(double... x);

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

	public List<Vector<Double>> getParam() {
		return param;
	}

	public void setParam(List<Vector<Double>> param) {
		this.param = param;
	}

}
