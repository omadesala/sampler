package com.sample.cluster;

import java.util.Random;

import Jama.Matrix;

import com.google.common.base.Preconditions;
import com.probablity.utils.MatrixUtils;

/**
 * @ClassName: FactorAnalysis
 * @Description: there are two way to estimate the parameter.one is include the
 *               mean and the psi estimate can't use andrew ng given
 *               fomula.</br> tow. we can let each xi minus mean and use andrew
 *               ng given formula.because of the mean of input x is not changed
 *               in the iterator.so the second way is a good way</br> but here
 *               we use the first method to illustrate the principle.
 * @author omadesala@msn.com
 * @date 2014-3-30 ÉÏÎç10:11:44
 */
public class FactorAnalysis {

    private Matrix meanX;
    private Matrix meanZ;
    private Matrix varZZ;

    // result
    private Matrix lambda;// map matrix, which map k dim to m dim
    private Matrix psi; // precision matrix of m dimension
    private Matrix input; // train set
    private int dataLength;// train set length
    private int dataDim; // data dimension

    private int trainingtimes = 1000;

    private Random random = new Random();

    public FactorAnalysis(Builder builder) {

        this.meanX = builder.meanX;
        this.meanZ = builder.meanZ;
        this.varZZ = builder.varZZ;
        this.input = builder.input;
        this.dataDim = this.input.getRowDimension();
        this.dataLength = this.input.getColumnDimension();

        this.lambda = Matrix.random(input.getRowDimension(),
                this.meanZ.getRowDimension());
        // this.lambda = new Matrix(input.getRowDimension(),
        // this.meanZ.getRowDimension());

        // this.lambda.set(0, 0, 4.9);
        // this.lambda.set(1, 0, 2.1);

        this.psi = new Matrix(this.dataDim, this.dataDim);

        psi.set(0, 0, random.nextDouble());
        psi.set(0, 1, 0.);
        psi.set(1, 0, 0.);
        psi.set(1, 1, random.nextDouble());

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

        // 1. get mean of factor given x_i,use the last time update lambda and
        // other parameter
        Matrix meanSetOfZiGivenX = new Matrix(this.meanZ.getRowDimension(),
                this.dataLength);
        for (int i = 0; i < this.dataLength; i++) {
            Matrix pointX = MatrixUtils.getMatrixColumn(this.input, i);
            Matrix updatedMeanZConditionXi = updateMeanZiGivenXi(pointX);
            MatrixUtils.setMatrixColumn(meanSetOfZiGivenX,
                    updatedMeanZConditionXi, i);
        }

        // 2. get factor precision matrix from last time updated lambda and
        // other parameter
        Matrix updatedSigmaZiGivenXi = updateSigmaZiGivenXi();
        Matrix lastLambda = new Matrix(this.meanX.getRowDimension(),
                this.meanZ.getRowDimension());

        // stepM
        // 1. update lambda.
        lastLambda = updateLambda(meanSetOfZiGivenX, updatedSigmaZiGivenXi);
        System.out.println("print lastLambda:");
        MatrixUtils.printMatrix(lastLambda);
        // 2. update factor precision matrix.
        // updatePsi(meanSetOfZiGivenX, updatedSigmaZiGivenXi, lastLambda);
        updatePsi(meanSetOfZiGivenX, updatedSigmaZiGivenXi, lastLambda);
    }

    /**
     * @Description: // \Phi = \frac{1}{m}\sum_{i=0}^{m}x_i
     *               x_i^T-x_i\mu_{z_i|x_i}^T // \Lambda^T - //
     *               \Lambda\mu_{z_i|x_i
     *               }x_i^T+\Lambda(\mu_{z_i|x_i}\mu_{z_i|x_i}^T + //
     *               {\sum}_{z_i|x_i} )\Lambda^T
     * @param @param meanSetOfZiGivenX
     * @param @param updatedSigmaZiGivenXi
     * @param @param lastLambda
     * @throws
     */
    private void updatePsi(Matrix meanSetOfZiGivenX,
                           Matrix updatedSigmaZiGivenXi, Matrix lastLambda) {
        Matrix updatedPsi = new Matrix(this.psi.getRowDimension(),
                this.psi.getColumnDimension());
        for (int i = 0; i < this.dataLength; i++) {

            Matrix pointX = MatrixUtils.getMatrixColumn(this.input, i);
            Matrix updatedMeanZiGivenXi = MatrixUtils.getMatrixColumn(
                    meanSetOfZiGivenX, i);

            Matrix item1 = pointX.times(pointX.transpose());
            Matrix item2 = pointX.times(this.meanX.transpose());
            Matrix item3 = pointX.times(updatedMeanZiGivenXi.transpose())
                    .times(lastLambda.transpose());
            Matrix item4 = this.meanX.times(pointX.transpose());
            Matrix item5 = this.meanX.times(this.meanX.transpose());
            Matrix item6 = this.meanX.times(updatedMeanZiGivenXi.transpose())
                    .times(lastLambda.transpose());
            Matrix item7 = lastLambda.times(updatedMeanZiGivenXi).times(
                    pointX.transpose());
            Matrix item8 = lastLambda.times(updatedMeanZiGivenXi).times(
                    this.meanX.transpose());

            Matrix temp = updatedMeanZiGivenXi.times(
                    updatedMeanZiGivenXi.transpose()).plus(
                    updatedSigmaZiGivenXi);

            Matrix item9 = lastLambda.times(temp).times(lastLambda.transpose());

            Matrix psiTemp = item1.minus(item2).minus(item3).minus(item4)
                    .plus(item5).plus(item6).minus(item7).plus(item8)
                    .plus(item9);
            updatedPsi = updatedPsi.plus(psiTemp);
        }

        updatedPsi = updatedPsi.times(1. / this.dataLength);
        for (int j = 0; j < updatedPsi.getColumnDimension(); j++) {
            this.psi.set(j, j, updatedPsi.get(j, j));
        }
    }

    /**
     * @Description: // (\sum_{i=0}^{m}
     *               (x_i-\mu)\mu_{z|x_i}^T)(\mu_{z|x_i}\mu_{z|x_i}^T+ //
     *               \sigma_{z|x_i}^{-1} )^{-1}
     * @param @param meanSetOfZiGivenX
     * @param @param updatedSigmaZiGivenXi
     * @param @return
     * @return Matrix updated lambda
     * @throws
     */
    private Matrix updateLambda(Matrix meanSetOfZiGivenX,
                                Matrix updatedSigmaZiGivenXi) {
        Matrix lastLambda;
        Matrix partOneMatrix = new Matrix(this.meanX.getRowDimension(),
                this.meanZ.getColumnDimension());

        for (int i = 0; i < this.dataLength; i++) {

            Matrix pointX = MatrixUtils.getMatrixColumn(this.input, i);
            Matrix minus = pointX.minus(this.meanX);
            Matrix meanZiGivenXi = MatrixUtils.getMatrixColumn(
                    meanSetOfZiGivenX, i);

            Matrix times = minus.times(meanZiGivenXi.transpose());

            partOneMatrix = partOneMatrix.plus(times);
        }

        Matrix partTwoMatrix = new Matrix(this.varZZ.getRowDimension(),
                this.varZZ.getColumnDimension());
        for (int i = 0; i < this.dataLength; i++) {
            Matrix meanZigivenXi = MatrixUtils.getMatrixColumn(
                    meanSetOfZiGivenX, i);
            Matrix part2i = meanZigivenXi.times(meanZigivenXi.transpose())
                    .plus(updatedSigmaZiGivenXi);

            partTwoMatrix = partTwoMatrix.plus(part2i);

        }

        partTwoMatrix = partTwoMatrix.inverse();

        lastLambda = partOneMatrix.times(partTwoMatrix);
        this.lambda = lastLambda;
        return lastLambda;
    }

    private Matrix updateMeanZiGivenXi(Matrix pointXi) {

        Matrix lambdaTranspose = this.lambda.transpose();
        Matrix plus = this.lambda.times(lambdaTranspose).plus(this.psi);
        Matrix inverse = plus.inverse();
        Matrix minus = pointXi.minus(this.meanX);
        this.meanZ = lambdaTranspose.times(inverse).times(minus);

        return this.meanZ;

    }

    private Matrix updateSigmaZiGivenXi() {

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
