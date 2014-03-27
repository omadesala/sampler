package com.sample.cluster;

import com.google.common.base.Preconditions;
import com.probablity.utils.MatrixUtils;

import Jama.Matrix;

public class FactorAnalysis {

    private Matrix meanX;
    private Matrix meanZ;
    private Matrix varZZ;
    private Matrix varZX;
    private Matrix varXZ;
    private Matrix varXX;

    // result
    private Matrix mean;
    private Matrix lambda;
    private Matrix psi;
    private Matrix input;
    private int dataLength;
    private int dataDim;

    private int trainingtimes = 100;

    public FactorAnalysis(Builder builder) {

        this.meanX = builder.meanX;
        this.meanZ = builder.meanZ;
        this.varZZ = builder.varZZ;
        this.varZX = builder.varZX;
        this.varXZ = builder.varZX.transpose();
        this.varXX = builder.varXX;
        this.input = builder.input;
        this.dataDim = this.input.getRowDimension();
        this.dataLength = this.input.getColumnDimension();

    }

    public void train(Matrix input) {

        Preconditions.checkArgument(input != null, "the sample data should not be null");
        Preconditions.checkArgument(input.getColumnDimension() > 0, "please input at least one point to train");

        this.input = input;

        for (int i = 0; i < this.trainingtimes; i++) {

            trainOnce();

            // stepE();
            // stepM();

        }
    }

    private void trainOnce() {

        for (int i = 0; i < this.dataLength; i++) {

            // stepE
            Matrix updateMeanZConditionXi = updateMeanZConditionXi(MatrixUtils.getMatrixColumn(this.input, i));
            Matrix updateSigmaZConditionXi = updateSigmaZConditionXi(MatrixUtils.getMatrixColumn(this.input, i));
            // stepM

            
        }

    }

    private Matrix updateMeanZConditionXi(Matrix pointXi) {

        Matrix lambdaTranspose = this.lambda.transpose();
        Matrix inverse = this.lambda.times(lambdaTranspose).plus(this.psi).inverse();
        Matrix minus = pointXi.minus(this.mean);

        this.meanZ = lambdaTranspose.times(inverse).times(minus);

        return this.meanZ;

    }

    private Matrix updateSigmaZConditionXi(Matrix pointXi) {

        Matrix unitOfVarZZ = MatrixUtils.getUnitMatrix(pointXi);

        Matrix lambdaTranspose = this.lambda.transpose();
        Matrix inverse = this.lambda.times(lambdaTranspose).plus(this.psi).inverse();
        Matrix times = inverse.times(this.lambda);

        this.varZZ = unitOfVarZZ.minus(times);
        return this.varZZ;
    }

    private void stepM() {

        updateMean();
        updateLambda();
        updatePsi();

    }

    private Matrix updateMean() {

        Preconditions.checkState(this.input != null, "the input data is null");;
        Preconditions.checkState(this.input.getColumnDimension() > 0,
                "please make the input data at least have one point ");

        Preconditions.checkState(this.dataDim > 0, "data dimension is not found");
        Preconditions.checkState(this.dataLength > 0, "data length is not found");

        Matrix sum = new Matrix(this.dataDim, 1);
        for (int col = 0; col < this.dataLength; col++) {
            sum = sum.plus(MatrixUtils.getMatrixColumn(this.input, col));
        }

        this.mean = sum.times(1. / this.dataLength);
        return this.mean;
    }

    private void updatePsi() {

    }

    private void updateLambda() {

        for (int i = 0; i < this.dataLength; i++) {

            Matrix pointXi = MatrixUtils.getMatrixColumn(this.input, i);

        }
    }

    public static class Builder {

        public Matrix input;
        private Matrix meanX;
        private Matrix meanZ;
        private Matrix varZZ;
        private Matrix varZX;
        private Matrix varXX;

        public Builder setDatas(Matrix input) {
            this.input = input;
            return this;

        }

        public Builder setMean1(Matrix mean1) {
            this.meanX = mean1;
            return this;

        }

        public Builder setMean2(Matrix mean2) {
            this.meanZ = mean2;
            return this;

        }

        public Builder setVar11(Matrix var11) {
            this.varZZ = var11;
            return this;

        }

        public Builder setVar12(Matrix var12) {
            this.varZX = var12;
            return this;

        }

        public Builder setVar22(Matrix var22) {
            this.varXX = var22;
            return this;

        }

        public FactorAnalysis build() {

            Preconditions.checkArgument(this.input != null, "the train data should not be null");
            Preconditions.checkArgument(this.input.getColumnDimension() > 0, "the train data should not be empty");

            Preconditions.checkArgument(this.meanX != null, "the mean1 should not be null");

            Preconditions.checkArgument(this.meanZ != null, "the mean1 should not be null");
            Preconditions.checkArgument(this.varZZ != null, "the var11 should not be null");
            Preconditions.checkArgument(MatrixUtils.isSquareMatrix(this.varZZ), "the var11 must be square matrix");

            Preconditions.checkArgument(this.varZX != null, "the var12 should not be null");
            Preconditions.checkArgument(this.varXX != null, "the var22 should not be null");

            Preconditions.checkArgument(MatrixUtils.isSquareMatrix(this.varXX), "the var22 must square matrix");

            int dim = this.input.getRowDimension();

            Preconditions.checkArgument(this.meanX.getRowDimension() < dim, "mean1 dimension must less data dimension");
            Preconditions.checkArgument(this.meanZ.getRowDimension() < dim, "mean2 dimension must less data dimension");
            Preconditions.checkArgument(this.meanX.getRowDimension() + this.meanZ.getRowDimension() == dim,
                    "the mean1 and mean2 dim must equal train data");

            Preconditions.checkArgument(this.meanX.getRowDimension() == this.varZZ.getRowDimension(),
                    "mean1 dimension must match var11");

            return new FactorAnalysis(this);

        }
    }

}
