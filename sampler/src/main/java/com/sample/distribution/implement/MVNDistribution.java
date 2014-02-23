package com.sample.distribution.implement;

import java.util.Vector;

import Jama.Matrix;

import com.sample.distribution.Distribution;

/**
 * the target distribution is multiple gauss distribution
 */
public class MVNDistribution extends Distribution {

	private Matrix mean;
	private Matrix var;

	public MVNDistribution() {

	}

	public MVNDistribution(Matrix mu, Matrix var) {
		this.mean = mu;
		this.var = var;
	}

	@Override
	public double densityFunction(Vector<Double> x) {

		int dimension = mean.getRowDimension();
		Matrix pt = new Matrix(dimension, 1);

		for (int i = 0; i < dimension; i++) {
			pt.set(i, 0, x.elementAt(i));
		}

		double constant = 1 / Math.pow((2. * Math.PI), dimension)
				* Math.sqrt(var.det());

		Matrix times = pt.minus(mean).transpose().times(var.inverse())
				.times(pt.minus(mean));
		Double exponent = Math.exp(times.get(0, 0));

		return constant * exponent;

	}

	@Override
	public Vector<Double> sampleOnePoint(double... x) {

		return null;
	}

	/**
	 * (�� Javadoc)
	 * <p>
	 * Title: setParameter
	 * </p>
	 * <p>
	 * Description:0 for mean,1 for variance
	 * </p>
	 * 
	 * @param param
	 * @see com.sample.distribution.Distribution#setParameter(double[])
	 */
	@Override
	public void setParameter(Vector<Double> param) {
		// Preconditions.checkNotNull(param);
		//
		// mean = param.elementAt(0);
		// variance = param.elementAt(1);

	}
}
