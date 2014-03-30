package com.sample.cluster;

import com.probablity.utils.MatrixUtils;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

public class PcaAnalysis {

    private Matrix input;
    private Matrix eigenMatrix;
    private Matrix eigenValue;
    private Matrix egivenVector;

    public PcaAnalysis(Builder builder) {
        this.input = builder.input;
    }

    public void process() {

        this.eigenMatrix = this.input.times(this.input.transpose());
        EigenvalueDecomposition eig = this.eigenMatrix.eig();

        eigenValue = eig.getD();
        egivenVector = eig.getV();

        double[] realEigenvalues = eig.getRealEigenvalues();
        for (double d : realEigenvalues) {
            System.out.println("eigen real value:" + d);
        }

        double[] imagEigenvalues = eig.getImagEigenvalues();
        for (double d : imagEigenvalues) {
            System.out.println("eigen image value:" + d);
        }

        MatrixUtils.printMatrix(eigenValue);
        MatrixUtils.printMatrix(egivenVector);

        // Matrix vec1 = MatrixUtils.getMatrixColumn(egivenVector, 0);
        // Matrix vec2 = MatrixUtils.getMatrixColumn(egivenVector, 1);
        //
        // MatrixUtils.printMatrix(vec2.times(vec1.transpose()));

    }

    public Matrix getTopKProject(int topK) {
        return null;
    }

    public static class Builder {

        private Matrix input;

        public Builder setData(Matrix data) {

            this.input = data;
            return this;
        }

        public PcaAnalysis build() {
            return new PcaAnalysis(this);
        }
    }

}
