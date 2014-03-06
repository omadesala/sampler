package com.sample.cluster;

import com.probablity.utils.MatrixUtils;

import Jama.Matrix;

public class KMean {

    private int clusterNumber = 0;
    private int dimesion = 0;
    private int dataLength = 0;

    private Matrix mean = null;
    private Matrix input = null;
    private Matrix iPointBelongClusterK = null;

    // private Matrix updatedMean = null;

    public KMean(int clusternum, int dim) {

        this.clusterNumber = clusternum;
        this.dimesion = dim;

        setMean(Matrix.random(this.dimesion, this.clusterNumber));
        setiPointBelongClusterK(new Matrix(1, this.dataLength));

        // updatedMean = new Matrix(this.dimesion, this.clusterNumber);
    }

    public int stepE() {

        int bestNearIndex = 0;
        Double distance = Double.MAX_VALUE;
        for (int i = 0; i < dataLength; i++) {

            Matrix matrixColumn = MatrixUtils.getMatrixColumn(input, i);
            for (int k = 0; k < this.clusterNumber; k++) {

                Matrix minus = matrixColumn.minus(MatrixUtils.getMatrixColumn(this.mean, k));

                Double dis = minus.norm2();
                if (dis < distance) {
                    distance = dis;
                    bestNearIndex = k;
                }
            }

            System.out.println("belong to: " + bestNearIndex);
            getiPointBelongClusterK().set(0, i, bestNearIndex);
        }

        return bestNearIndex;

    }

    public void stepM() {

        int clusterCount[] = new int[this.clusterNumber];
        for (int i = 0; i < this.dataLength; i++) {

            Double cluster = MatrixUtils.getColumnMatrixElementAt(iPointBelongClusterK, i);
            int clusterIndex = cluster.intValue();
            clusterCount[clusterIndex]++;

            Matrix meank = MatrixUtils.getMatrixColumn(this.mean, clusterIndex);
            meank = meank.plus(MatrixUtils.getMatrixColumn(this.input, i));
            MatrixUtils.setMatrixColumn(this.mean, meank, clusterIndex);
        }

        for (int k = 0; k < this.clusterNumber; k++) {

            System.out.println("cluster count :" + clusterCount[k]);
            Matrix meank = MatrixUtils.getMatrixColumn(this.mean, k);
            Matrix times = meank.times(clusterCount[k]);

            MatrixUtils.printMatrix(times);

            MatrixUtils.setMatrixColumn(this.mean, times, k);

        }

    }

    public void train(Matrix input) {

        this.setInput(input);

        for (int i = 0; i < 10; i++) {
            stepE();
            stepM();
            printInfo();
        }

    }

    public int getClusterNumber() {

        return clusterNumber;

    }

    public Matrix getInput() {

        return input;

    }

    public void setInput(Matrix input) {

        this.input = input;

    }

    public Matrix getMean() {

        return mean;

    }

    public void setMean(Matrix mean) {

        this.mean = mean;

    }

    public Matrix getiPointBelongClusterK() {

        return iPointBelongClusterK.copy();

    }

    public void setiPointBelongClusterK(Matrix iPointBelongClusterK) {

        this.iPointBelongClusterK = iPointBelongClusterK;

    }

    public void printInfo() {
        MatrixUtils.printMatrix(this.mean);
    }

}
