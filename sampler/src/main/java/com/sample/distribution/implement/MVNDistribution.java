package com.sample.distribution.implement;

import java.util.Vector;

import Jama.Matrix;

import com.google.common.base.Preconditions;
import com.probablity.utils.MatrixUtils;
import com.sample.distribution.Distribution;

/**
 * the target distribution is multiple gauss distribution
 */
public class MVNDistribution extends Distribution {

    private Matrix mean;
    private Matrix var;
    private int dimension;

    public MVNDistribution(Matrix mu, Matrix var) {
        this.mean = mu;
        this.var = var;

        this.dimension = this.mean.getRowDimension();

    }

    @Override
    public Double pdf(Vector<Double> pointVector) {

        Preconditions.checkNotNull(pointVector);
        Matrix point = MatrixUtils.getPointOfMatrix(pointVector);

        Double dominator = Math.pow((2. * Math.PI), dimension / 2.) * Math.sqrt(Math.abs(var.det()));
        Double constant = 1. / dominator;

        Matrix times = point.minus(mean).transpose().times(var.inverse()).times(point.minus(mean));

        Double exponent = Math.exp(-(1. / 2) * times.get(0, 0));

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
