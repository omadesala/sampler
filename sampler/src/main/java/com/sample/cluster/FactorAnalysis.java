package com.sample.cluster;

import com.google.common.base.Preconditions;
import com.probablity.utils.MatrixUtils;

import Jama.Matrix;

public class FactorAnalysis {

    private Matrix mean1;
    private Matrix mean2;
    private Matrix var11;
    private Matrix var12;
    private Matrix var21;
    private Matrix var22;

    // result
    private Matrix mean;
    private Matrix lambda;
    private Matrix psi;
    private int trainingtimes = 100;
    private Matrix input;
    private int dataLength;
    private int dataDim;

    public FactorAnalysis(Builder builder) {

        this.mean1 = builder.mean1;
        this.mean2 = builder.mean2;
        this.var11 = builder.var11;
        this.var12 = builder.var12;
        this.var21 = builder.var12.transpose();
        this.var22 = builder.var22;

    }

    public void train(Matrix input) {

        Preconditions.checkArgument(input != null, "the sample data should not be null");
        Preconditions.checkArgument(input.getColumnDimension() > 0, "please input at least one point to train");

        this.input = input;

        for (int i = 0; i < this.trainingtimes; i++) {

            stepE();
            stepM();

        }
    }

    private void stepE() {

    }

    private void stepM() {

        updateMean();
        updateLambda();
        updatePsi();

    }

    private void updateMean() {

        Preconditions.checkState(this.input != null, "the input data is null");;
        Preconditions.checkState(this.input.getColumnDimension() > 0,
                "please make the input data at least have one point ");

        Matrix sum = new Matrix(this.dataDim, 1);
        for (int col = 0; col < this.input.getColumnDimension(); col++) {
            Matrix matrixColumn = MatrixUtils.getMatrixColumn(this.input, col);
            sum = sum.plus(matrixColumn);
        }

        this.mean = sum.times(1. / this.dataLength);

    }

    private void updatePsi() {

    }

    private void updateLambda() {

    }

    public static class Builder {

        private Matrix mean1;
        private Matrix mean2;
        private Matrix var11;
        private Matrix var12;
        private Matrix var21;
        private Matrix var22;

        public Builder setMean1(Matrix mean1) {
            this.mean1 = mean1;
            return this;

        }

        public Builder setMean2(Matrix mean2) {
            this.mean2 = mean2;
            return this;

        }

        public Builder setVar11(Matrix var11) {
            this.var11 = var11;
            return this;

        }

        public Builder setVar12(Matrix var12) {
            this.var12 = var12;
            return this;

        }

        public Builder setVar22(Matrix var22) {
            this.var22 = var22;
            return this;

        }

        public FactorAnalysis build() {

            Preconditions.checkArgument(this.mean1 != null, "the mean1 should not be null");
            Preconditions.checkArgument(this.mean2 != null, "the mean1 should not be null");
            Preconditions.checkArgument(this.var11 != null, "the var11 should not be null");
            Preconditions.checkArgument(this.var12 != null, "the var12 should not be null");
            Preconditions.checkArgument(this.var22 != null, "the var22 should not be null");

            return new FactorAnalysis(this);

        }

    }

}
