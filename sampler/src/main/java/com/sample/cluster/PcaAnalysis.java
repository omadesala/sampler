package com.sample.cluster;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import Jama.SingularValueDecomposition;

import com.probablity.utils.MatrixUtils;

/**
 * 
 * @ClassName: PcaAnalysis
 * @Description: when the dimension is very large, svd should be used
 * @author huangguiping
 * 
 */
public class PcaAnalysis {

    private Matrix input;
    private Matrix output;
    private Matrix coVarMatrix;
    private Matrix egivenVector;
    private int topK = 2;

    public PcaAnalysis(Builder builder) {
        this.input = builder.input;
    }

    private void preProcess() {

        Matrix matrixMean = MatrixUtils.getMatrixMean(this.input);

        for (int i = 0; i < this.input.getColumnDimension(); i++) {
            MatrixUtils.setMatrixColumn(this.input, MatrixUtils.getMatrixColumn(this.input, i).minus(matrixMean), i);
        }

        double[] varSquare = new double[this.input.getRowDimension()];
        for (int i = 0; i < this.input.getRowDimension(); i++) {
            Matrix matrixRow = MatrixUtils.getMatrixRow(this.input, i);
            Matrix times = matrixRow.times(matrixRow.transpose());
            varSquare[i] = times.get(0, 0) / this.input.getColumnDimension();
        }

        for (int j = 0; j < this.input.getRowDimension(); j++) {
            double var = Math.sqrt(varSquare[j]);
            Matrix matrixRow = MatrixUtils.getMatrixRow(input, j);
            Matrix updatedRow = matrixRow.times(1. / var);
            MatrixUtils.setMatrixRow(input, updatedRow, j);
        }
    }

    public void process() {

        preProcess();

        this.coVarMatrix = MatrixUtils.getCovarianceMatrix(this.input);

        EigenvalueDecomposition eig = this.coVarMatrix.eig();

        egivenVector = eig.getV();

        Matrix result = new Matrix(topK, this.input.getColumnDimension());
        for (int i = 0; i < this.input.getColumnDimension(); i++) {

            Matrix pointXi = MatrixUtils.getMatrixColumn(this.input, i);

            double[][] kdim = new double[topK][1];
            for (int j = 0; j < topK; j++) {

                int columnDimension = egivenVector.getColumnDimension() - 1;
                Matrix eigvenI = MatrixUtils.getMatrixColumn(egivenVector, columnDimension - j);

                Matrix times = eigvenI.transpose().times(pointXi);
                kdim[j][0] = times.get(0, 0);
            }
            Matrix kDimMatrix = new Matrix(kdim);
            MatrixUtils.setMatrixColumn(result, kDimMatrix, i);
        }

        this.output = result;

    }

    public void processWithSVD() {

        preProcess();

        coVarMatrix = MatrixUtils.getCovarianceMatrix(this.input);
        SingularValueDecomposition svd = this.input.transpose().svd();
        egivenVector = svd.getV();

        Matrix result = new Matrix(topK, this.input.getColumnDimension());
        for (int i = 0; i < this.input.getColumnDimension(); i++) {

            Matrix pointXi = MatrixUtils.getMatrixColumn(this.input, i);

            double[][] kdim = new double[topK][1];
            for (int j = 0; j < topK; j++) {

                int columnDimension = egivenVector.getColumnDimension() - 1;
                Matrix eigvenI = MatrixUtils.getMatrixColumn(egivenVector, columnDimension - j);

                Matrix times = eigvenI.transpose().times(pointXi);
                kdim[j][0] = times.get(0, 0);
            }
            Matrix kDimMatrix = new Matrix(kdim);
            MatrixUtils.setMatrixColumn(result, kDimMatrix, i);
        }

        this.output = result;

    }

    public Matrix getTopKProject(int topK) {
        return null;
    }

    public Matrix getOutput() {
        return output;
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
