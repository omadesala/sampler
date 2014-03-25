package com.sample.distribution.implement;

import java.util.Vector;

import Jama.Matrix;

import com.google.common.base.Preconditions;
import com.probablity.utils.MatrixUtils;
import com.sample.distribution.Distribution;

/**
 * the target distribution is multiple gauss distribution
 */
public class MVNConditionDistribution extends Distribution {

    private Matrix meanx1;
    private Matrix meanx2;

    private Matrix var11;
    private Matrix var12;
    private Matrix var21;
    private Matrix var22;

    private int dim1;
    private int dim2;

    public MVNConditionDistribution(Matrix mu1, Matrix mu2, Matrix var11, Matrix var12, Matrix var22) {

        this.meanx1 = mu1;
        this.meanx1 = mu1;
        this.var11 = var11;
        this.var12 = var12;
        this.var21 = var12.transpose();
        this.var22 = var22;

        this.dim1 = this.meanx1.getRowDimension();
        this.dim2 = this.meanx2.getRowDimension();

    }

    @Override
    public Double pdf(Vector<Double> pointVector) {

        // Preconditions.checkNotNull(pointVector);
        // Matrix point = MatrixUtils.getPointOfMatrix(pointVector);
        //
        // Double dominator = Math.pow((2. * Math.PI), dimension / 2.) *
        // Math.sqrt(Math.abs(var.det()));
        // Double constant = 1. / dominator;
        //
        // Matrix times =
        // point.minus(mean).transpose().times(var.inverse()).times(point.minus(mean));
        //
        // Double exponent = Math.exp(-(1. / 2) * times.get(0, 0));

        // return constant * exponent;
        return 0.;
    }

    @Override
    public Vector<Double> sampleOnePoint(double... x) {

        return null;
    }

    /**
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
