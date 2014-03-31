package com.sample.cluster;

import com.probablity.utils.MatrixUtils;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

public class PcaAnalysis {

    private Matrix input;
    private Matrix output;
    private Matrix coVarMatrix;
    // private Matrix eigenValue;
    private Matrix egivenVector;
    private int topK = 2;

    public PcaAnalysis(Builder builder) {
        this.input = builder.input;
    }

    public void process() {

        this.coVarMatrix = this.input.times(this.input.transpose());

        MatrixUtils.printMatrix(this.coVarMatrix);
        EigenvalueDecomposition eig = this.coVarMatrix.eig();

        // eigenValue = eig.getD();
        egivenVector = eig.getV();

        Matrix result = new Matrix(topK, this.input.getColumnDimension());
        for (int i = 0; i < this.input.getColumnDimension(); i++) {

            Matrix pointXi = MatrixUtils.getMatrixColumn(this.input, i);

            double[][] kdim = new double[topK][1];
            for (int j = 0; j < topK; j++) {

                int columnDimension = egivenVector.getColumnDimension() - 1;
                Matrix eigvenI = MatrixUtils.getMatrixColumn(egivenVector,
                        columnDimension - j);

                Matrix times = eigvenI.transpose().times(pointXi);
                kdim[j][0] = times.get(0, 0);
                MatrixUtils.printMatrix(times);
            }
            Matrix kDimMatrix = new Matrix(kdim);
            MatrixUtils.setMatrixColumn(result, kDimMatrix, i);
        }

        this.output = result;

        // System.out.println("print topK dim:");
        // MatrixUtils.printMatrix(result);
        // System.out.println("print input:");
        // MatrixUtils.printMatrix(this.input);
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
