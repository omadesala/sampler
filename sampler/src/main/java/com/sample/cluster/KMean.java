package com.sample.cluster;

import java.util.Collections;

import com.google.common.base.Preconditions;
import com.probablity.utils.MatrixUtils;

import Jama.Matrix;

public class KMean {

    private int clusterNumber = 0;
    private int dimesion = 0;
    private int dataLength = 0;

    private Matrix mean = null;
    private Matrix input = null;
    private Matrix iPointBelongClusterK = null;

    public KMean(int clusternum, int dim) {

        this.clusterNumber = clusternum;
        this.dimesion = dim;

    }

    public void init(Matrix input) {

        Preconditions.checkNotNull(input);

        this.input = input;
        this.dataLength = input.getColumnDimension();
        setMean(Matrix.random(this.dimesion, this.clusterNumber));
        setiPointBelongClusterK(new Matrix(1, this.dataLength));

    }

    public int stepE() {

        int bestNearIndex = 0;
        for (int i = 0; i < this.dataLength; i++) {

            Double distance = Double.MAX_VALUE;
            Matrix matrixColumn = MatrixUtils.getMatrixColumn(input, i);
            for (int k = 0; k < this.clusterNumber; k++) {

                Matrix minus = matrixColumn.minus(MatrixUtils.getMatrixColumn(
                        this.mean, k));

                Double dis = minus.norm2();

                if (dis < distance) {
                    distance = dis;
                    bestNearIndex = k;
                }
            }

            getiPointBelongClusterK().set(0, i, bestNearIndex);
        }

        return bestNearIndex;

    }

    public void stepM() {

        int clusterCount[] = new int[this.clusterNumber];
        for (int i = 0; i < this.dataLength; i++) {

            Double cluster = MatrixUtils.getRowMatrixElementAt(
                    iPointBelongClusterK, i);
            int clusterIndex = cluster.intValue();
            clusterCount[clusterIndex]++;

            // FIXME there is a problem to fix, error calculate the mean
            Matrix meank = MatrixUtils.getMatrixColumn(this.mean, clusterIndex);
            meank = meank.plus(MatrixUtils.getMatrixColumn(this.input, i));

            MatrixUtils.printMatrix(meank);

            System.out.println("cluster index : " + clusterIndex);
            MatrixUtils.printMatrix(this.mean);
            MatrixUtils.setMatrixColumn(this.mean, meank, clusterIndex);
            MatrixUtils.printMatrix(this.mean);
        }

        for (int k = 0; k < this.clusterNumber; k++) {

            System.out.println("cluster count :" + clusterCount[k]);
            Matrix meank = MatrixUtils.getMatrixColumn(this.mean, k);
            System.out.println("mean of k");
            MatrixUtils.printMatrix(meank);

            if (clusterCount[k] == 0) {
                Matrix times = meank.times(1. / clusterCount[k]);
                MatrixUtils.setMatrixColumn(this.mean, times, k);
            }
        }

        MatrixUtils.printMatrix(this.mean);
    }

    public void train() {

        for (int i = 0; i < 10; i++) {
            System.out.println("train times: " + i);
            stepE();
            stepM();
            printInfo();
        }

    }

    public int getClusterNumber() {

        return clusterNumber;

    }

    public Matrix getMean() {

        return mean;

    }

    public void setMean(Matrix mean) {

        this.mean = mean;

    }

    public Matrix getiPointBelongClusterK() {

        return iPointBelongClusterK;

    }

    public void setiPointBelongClusterK(Matrix iPointBelongClusterK) {

        this.iPointBelongClusterK = iPointBelongClusterK;

    }

    public void printInfo() {
        MatrixUtils.printMatrix(this.mean);
    }

}
