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

    private int trainingtimes = 200;

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
        this.lambda = Matrix.random(input.getRowDimension(),
                this.meanZ.getRowDimension());
        this.psi = new Matrix(this.dataDim, this.dataDim);

        psi.set(0, 0, 0.8);
        psi.set(0, 1, 0.);
        psi.set(1, 0, 0.);
        psi.set(1, 1, 0.8);

    }

    public void train() {

        Preconditions.checkArgument(input != null,
                "the sample data should not be null");
        Preconditions.checkArgument(input.getColumnDimension() > 0,
                "please input at least one point to train");

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

        // 1. get factor precision matrix from last time updated lambda and
        // other parameter
        Matrix updatedSigmaZConditionXi = updateSigmaZConditionXi();
        // 2. get mean of factor given x_i,use the last time update lambda and
        // other parameter
        Matrix meanSetOfZgivenX = new Matrix(this.meanZ.getRowDimension(),
                this.dataLength);
        for (int i = 0; i < this.dataLength; i++) {
            Matrix pointX = MatrixUtils.getMatrixColumn(this.input, i);
            Matrix updatedMeanZConditionXi = updateMeanZConditionXi(pointX);
            MatrixUtils.setMatrixColumn(meanSetOfZgivenX,
                    updatedMeanZConditionXi, i);
        }

        // stepM
        // 1. update lambda.
        // (\sum_{i=0}^{m} (x_i-\mu)\mu_{z|x_i}^T)(\mu_{z|x_i}\mu_{z|x_i}^T+
        // \sigma_{z|x_i}^{-1} )^{-1}

        Matrix meanXset = new Matrix(this.meanX.getRowDimension(),
                this.dataLength);
        for (int i = 0; i < this.dataLength; i++) {
            MatrixUtils.setMatrixColumn(meanXset, this.meanX, i);
        }

        Matrix duceMean = this.input.minus(meanXset);
        Matrix part1 = duceMean.times(meanSetOfZgivenX.transpose());
        Matrix partTwoMatrix = new Matrix(this.varZZ.getRowDimension(),
                this.varZZ.getColumnDimension());

        for (int i = 0; i < this.dataLength; i++) {
            Matrix meanZgivenXi = MatrixUtils.getMatrixColumn(meanSetOfZgivenX,
                    i);
            Matrix part2i = meanZgivenXi.times(meanZgivenXi.transpose()).plus(
                    updatedSigmaZConditionXi);

            partTwoMatrix = partTwoMatrix.plus(part2i);

        }

        partTwoMatrix = partTwoMatrix.inverse();

        this.lambda = part1.times(partTwoMatrix);

        // 2. update factor precision matrix.
        // \Phi = \frac{1}{m}\sum_{i=0}^{m}x_i x_i^T-x_i\mu_{z_i|x_i}^T
        // \Lambda^T -
        // \Lambda\mu_{z_i|x_i}x_i^T+\Lambda(\mu_{z_i|x_i}\mu_{z_i|x_i}^T +
        // {\sum}_{z_i|x_i} )\Lambda^T

        Matrix updatedPsi = new Matrix(this.varXX.getRowDimension(),
                this.varXX.getColumnDimension());
        for (int i = 0; i < this.dataLength; i++) {

            Matrix pointX = MatrixUtils.getMatrixColumn(this.input, i);
            Matrix updatedMeanZGivenXi = updateMeanZConditionXi(pointX);
            Matrix updatedSigmaZGivenXi = updateSigmaZConditionXi();
            Matrix updatedLambda = this.lambda;

            Matrix item1 = pointX.times(pointX.transpose());
            Matrix item2 = pointX.times(updatedMeanZGivenXi.transpose()).times(
                    updatedLambda.transpose());
            Matrix item3 = updatedLambda.times(updatedMeanZGivenXi).times(
                    pointX.transpose());

            Matrix temp = updatedMeanZGivenXi.times(
                    updatedMeanZGivenXi.transpose()).plus(updatedSigmaZGivenXi);

            Matrix item4 = updatedLambda.times(temp).times(
                    updatedLambda.transpose());

            Matrix psiTemp = item1.minus(item2).minus(item3).plus(item4);
            updatedPsi = updatedPsi.plus(psiTemp);
        }

        updatedPsi = updatedPsi.times(1. / this.dataLength);
        for (int j = 0; j < updatedPsi.getColumnDimension(); j++) {
            this.varXX.set(j, j, updatedPsi.get(j, j));
            this.psi.set(j, j, updatedPsi.get(j, j));
        }
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
        Matrix second = lambdaTranspose.times(plus.inverse())
                .times(this.lambda);

        this.varZZ = unitOfVarZZ.minus(second);
        return this.varZZ;
    }

    private Matrix updateMean() {

        Preconditions.checkState(this.input != null, "the input data is null");;
        Preconditions.checkState(this.input.getColumnDimension() > 0,
                "please make the input data at least have one point ");

        Preconditions.checkState(this.dataDim > 0,
                "data dimension is not found");
        Preconditions.checkState(this.dataLength > 0,
                "data length is not found");

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

            Preconditions.checkArgument(this.input != null,
                    "the train data should not be null");
            Preconditions.checkArgument(this.input.getColumnDimension() > 0,
                    "the train data should not be empty");

            Preconditions.checkArgument(this.meanX != null,
                    "the mean1 should not be null");

            Preconditions.checkArgument(this.meanZ != null,
                    "the mean1 should not be null");
            Preconditions.checkArgument(this.varZZ != null,
                    "the var11 should not be null");
            Preconditions.checkArgument(MatrixUtils.isSquareMatrix(this.varZZ),
                    "the var11 must be square matrix");

            Preconditions.checkArgument(this.varZX != null,
                    "the var12 should not be null");
            Preconditions.checkArgument(this.varXX != null,
                    "the var22 should not be null");

            Preconditions.checkArgument(MatrixUtils.isSquareMatrix(this.varXX),
                    "the var22 must square matrix");

            int dim = this.input.getRowDimension();

            Preconditions.checkArgument(this.meanX.getRowDimension() == dim,
                    "meanX dimension must equal data dimension");
            Preconditions.checkArgument(this.meanZ.getRowDimension() < dim,
                    "meanZ dimension must less data dimension");

            Preconditions.checkArgument(
                    this.meanX.getRowDimension() == this.varXX
                            .getRowDimension(),
                    "meanX dimension must match varXX");

            return new FactorAnalysis(this);

        }
    }

}
