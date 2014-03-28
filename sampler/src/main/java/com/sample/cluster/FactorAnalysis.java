package com.sample.cluster;

import Jama.Matrix;

import com.google.common.base.Preconditions;
import com.probablity.utils.MatrixUtils;

public class FactorAnalysis {

    private Matrix meanX;
    private Matrix meanZ;
    private Matrix varZZ;
    private Matrix varZX;
    private Matrix varXZ;
    private Matrix varXX;

    // result
    // private Matrix mean; //
    private Matrix lambda;// map matrix, which map k dim to m dim
    private Matrix psi; // precision matrix of m dimension
    private Matrix input; // train set
    private int dataLength;// train set length
    private int dataDim; // data dimension

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
        this.lambda = Matrix.random(input.getRowDimension(), this.meanZ.getRowDimension());
        this.psi = new Matrix(this.dataDim, this.dataDim);

        psi.set(0, 0, 0.8);
        psi.set(0, 1, 0.2);
        psi.set(1, 0, 0.2);
        psi.set(1, 1, 0.8);

    }

    public void train() {

        Preconditions.checkArgument(input != null, "the sample data should not be null");
        Preconditions.checkArgument(input.getColumnDimension() > 0, "please input at least one point to train");

        updateMean();

        for (int i = 0; i < this.trainingtimes; i++) {
            trainOnce();
        }

        printResult();

    }

    public void printResult() {

        System.out.println("print updated result:");
        System.out.println("print mean of X:");
        MatrixUtils.printMatrix(this.meanX);

        System.out.println("print lambda:");
        MatrixUtils.printMatrix(this.lambda);

        System.out.println("print psi:");
        MatrixUtils.printMatrix(this.psi);
    }

    private void trainOnce() {

        // stepE

        // 1. get factor precision matrix
        Matrix updatedSigmaZConditionXi = updateSigmaZConditionXi();
        // 2. get mean of factor condition x
        Matrix meanOfZConditionXSet = new Matrix(this.meanZ.getRowDimension(), this.dataLength);
        for (int i = 0; i < this.dataLength; i++) {
            Matrix pointX = MatrixUtils.getMatrixColumn(this.input, i);
            Matrix updatedMeanZConditionXi = updateMeanZConditionXi(pointX);
            MatrixUtils.setMatrixColumn(meanOfZConditionXSet, updatedMeanZConditionXi, i);
        }

        // System.out.println("print z|x_i set");
        // MatrixUtils.printMatrix(meanOfZConditionXSet);

        // stepM
        // 1. update lambda.
        // (\sum_{i=0}^{m} (x_i-\mu)\mu_{z|x_i}^T)(\mu_{z|x_i}\mu_{z|x_i}^T+
        // \sigma_{z|x_i}^{-1} )^{-1}

        // System.out.println("input row:" + this.input.getRowDimension());
        // System.out.println("input col:" + this.input.getColumnDimension());
        // System.out.println("meanOfZConditionXSet row:" +
        // meanOfZConditionXSet.getRowDimension());
        // System.out.println("meanOfZConditionXSet col:" +
        // meanOfZConditionXSet.getColumnDimension());
        // System.out.println("this.meanX row:" + this.meanX.getRowDimension());
        // System.out.println("this.meanX col:" +
        // this.meanX.getColumnDimension());

        Matrix meanXset = new Matrix(this.meanX.getRowDimension(), this.dataLength);
        for (int i = 0; i < this.dataLength; i++) {
            MatrixUtils.setMatrixColumn(meanXset, this.meanX, i);
        }

        Matrix duceMean = this.input.minus(meanXset);

        Matrix part1 = duceMean.times(meanOfZConditionXSet.transpose());

        Matrix part2 = meanOfZConditionXSet.times(meanOfZConditionXSet.transpose()).plus(
                updatedSigmaZConditionXi.inverse());
        Matrix part2Inverse = part2.inverse();

        this.lambda = part1.times(part2Inverse);
        System.out.println("print lambda");
        MatrixUtils.printMatrix(this.lambda);

        // 2. update factor precision matrix.
        // \Phi = \frac{1}{m}\sum_{i=0}^{m}x_i x_i^T-x_i\mu_{z_i|x_i}^T
        // \Lambda^T -
        // \Lambda\mu_{z_i|x_i}x_i^T+\Lambda(\mu_{z_i|x_i}\mu_{z_i|x_i}^T +
        // {\sum}_{z_i|x_i} )\Lambda^T
        Matrix updatedPsi = new Matrix(this.varXX.getRowDimension(), this.varXX.getColumnDimension());
        for (int i = 0; i < this.dataLength; i++) {

            Matrix pointX = MatrixUtils.getMatrixColumn(this.input, i);
            Matrix item1 = pointX.times(pointX.transpose());

            Matrix meanZConX = MatrixUtils.getMatrixColumn(meanOfZConditionXSet, i);
            Matrix item2 = pointX.times(meanZConX).times(this.lambda.transpose());

            Matrix item3 = this.lambda.times(meanZConX).times(pointX.transpose());

            // System.out.println("print this.lambda row " +
            // this.lambda.getRowDimension());
            // System.out.println("print this.lambda col " +
            // this.lambda.getColumnDimension());

            Matrix item4 = this.lambda.times(meanZConX.times(meanZConX.transpose()).plus(updatedSigmaZConditionXi))
                    .times(this.lambda.transpose());

            Matrix psiTemp = item1.minus(item2).minus(item3).plus(item4);
            // System.out.println("print psiTemp row " +
            // psiTemp.getRowDimension());
            // System.out.println("print psiTemp col " +
            // psiTemp.getColumnDimension());
            // System.out.println("print updatedPsi row " +
            // updatedPsi.getRowDimension());
            // System.out.println("print updatedPsi col " +
            // updatedPsi.getColumnDimension());
            // System.out.println("print psiTemp ");
            // MatrixUtils.printMatrix(psiTemp);

            updatedPsi = updatedPsi.plus(psiTemp).times(this.dataLength);

        }

        for (int j = 0; j < updatedPsi.getColumnDimension(); j++) {
            this.varXX.set(j, j, updatedPsi.get(j, j));
            this.psi.set(j, j, updatedPsi.get(j, j));
        }

        // System.out.println("print the matrix of varXX");
        // MatrixUtils.printMatrix(this.varXX);

    }

    private Matrix updateMeanZConditionXi(Matrix pointXi) {

        Matrix lambdaTranspose = this.lambda.transpose();
        Matrix plus = this.lambda.times(lambdaTranspose).plus(this.psi);
        Matrix inverse = plus.inverse();
        Matrix minus = pointXi.minus(this.meanX);
        this.meanZ = lambdaTranspose.times(inverse).times(minus);

        return this.meanZ;

    }

    private Matrix updateSigmaZConditionXi() {

        Matrix unitOfVarZZ = MatrixUtils.getUnitMatrix(this.varZZ);
        Matrix lambdaTranspose = this.lambda.transpose();
        Matrix plus = this.lambda.times(lambdaTranspose).plus(this.psi);
        Matrix second = lambdaTranspose.times(plus.inverse()).times(this.lambda);

        this.varZZ = unitOfVarZZ.minus(second);
        return this.varZZ;
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

        this.meanX = sum.times(1. / this.dataLength);
        return this.meanX;
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

        public Builder setMeanZ(Matrix meanZ) {
            this.meanZ = meanZ;
            return this;

        }

        public Builder setMeanX(Matrix meanX) {
            this.meanX = meanX;
            return this;

        }

        public Builder setVar11(Matrix varZZ) {
            this.varZZ = varZZ;
            return this;

        }

        public Builder setVar12(Matrix varZX) {
            this.varZX = varZX;
            return this;

        }

        public Builder setVar22(Matrix varXX) {
            this.varXX = varXX;
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

            Preconditions.checkArgument(this.meanX.getRowDimension() == dim,
                    "meanX dimension must equal data dimension");
            Preconditions.checkArgument(this.meanZ.getRowDimension() < dim, "meanZ dimension must less data dimension");

            Preconditions.checkArgument(this.meanX.getRowDimension() == this.varXX.getRowDimension(),
                    "meanX dimension must match varXX");

            return new FactorAnalysis(this);

        }
    }

}
