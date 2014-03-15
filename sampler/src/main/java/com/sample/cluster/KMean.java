package com.sample.cluster;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import Jama.Matrix;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.probablity.utils.MatrixUtils;

public class KMean {

    private int clusterNumber = 0;
    private int dimesion = 0;
    private int dataLength = 0;

    private Matrix mean = null;
    private Matrix input = null;
    private Matrix iPointBelongClusterK = null;
    private List<Matrix> clusteredDataList = null;
    private List<Matrix> clusteredCovariance = null;

    private int clusterCount[] = null;

    public KMean(int clusternum, int dim) {

        this.clusterNumber = clusternum;
        this.dimesion = dim;
        clusteredDataList = new ArrayList<Matrix>();
        clusteredCovariance = new ArrayList<Matrix>();

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

        clusterCount = new int[this.clusterNumber];
        for (int i = 0; i < this.dataLength; i++) {

            Double cluster = MatrixUtils.getRowMatrixElementAt(
                    iPointBelongClusterK, i);
            int clusterIndex = cluster.intValue();
            clusterCount[clusterIndex]++;

            Matrix meank = MatrixUtils.getMatrixColumn(this.mean, clusterIndex);
            meank = meank.plus(MatrixUtils.getMatrixColumn(this.input, i));
            MatrixUtils.setMatrixColumn(this.mean, meank, clusterIndex);
        }

        for (int k = 0; k < this.clusterNumber; k++) {

            Matrix meank = MatrixUtils.getMatrixColumn(this.mean, k);

            if (clusterCount[k] != 0) {
                Matrix times = meank.times(1. / clusterCount[k]);
                MatrixUtils.setMatrixColumn(this.mean, times, k);
            }
        }
    }

    public void train() {

        for (int i = 0; i < 100; i++) {
            // System.out.println("train times: " + i);
            stepE();
            stepM();
        }

        generateComponentElements();
        generateCovarianceMatrix();
        printInfo();
    }

    /**
     * @Title: generateComponentElements
     * @Description: split the input into k component
     * @param
     * @return void
     * @throws
     */
    private void generateComponentElements() {

        List<List<Vector<Double>>> cluster = Lists.newArrayList();
        for (int k = 0; k < this.clusterNumber; k++) {
            cluster.add(new ArrayList<Vector<Double>>());
        }

        for (int i = 0; i < this.dataLength; i++) {

            int blongClusterK = MatrixUtils.getRowMatrixElementAt(
                    iPointBelongClusterK, i).intValue();

            List<Vector<Double>> list = cluster.get(blongClusterK);
            Matrix dataPoint = MatrixUtils.getMatrixColumn(this.input, i);
            List<Vector<Double>> listVector = MatrixUtils
                    .getListVector(dataPoint);

            list.add(listVector.get(0));

        }

        for (int j = 0; j < this.clusterNumber; j++) {

            List<Vector<Double>> listVector = cluster.get(j);

            if (listVector.size() > 0) {
                Matrix matrix = MatrixUtils.getMatrix(listVector);
                this.clusteredDataList.add(matrix);
            } else {
                Matrix emptyMatrix = new Matrix(0, 0);
                this.clusteredDataList.add(emptyMatrix);
            }
        }

    }

    public int getClusterNumber() {

        return clusterNumber;

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

        System.out.println("Kmean train result:");
        printMean();
        printCovariance();
        printComponent();

    }

    private void printComponent() {

        Double ratio = 0.;
        for (int i = 0; i < clusterCount.length; i++) {
            ratio += clusterCount[i];
        }

        for (int i = 0; i < clusterCount.length; i++) {
            System.out.println("component k = " + i + " ratio is: "
                    + clusterCount[i] / ratio);
        }

    }

    public List<Matrix> getMean() {

        List<Matrix> meanList = Lists.newArrayList();

        for (int k = 0; k < this.clusterNumber; k++) {
            Matrix meanK = MatrixUtils.getMatrixColumn(this.mean, k);
            meanList.add(meanK);
        }
        return meanList;

    }

    public List<Matrix> getCovariance() {
        return this.clusteredCovariance;
    }

    private void printMean() {

        List<Matrix> meanList = getMean();
        for (int k = 0; k < this.clusterNumber; k++) {
            System.out.println("component k = " + k + " mean matrix");
            MatrixUtils.printMatrix(meanList.get(k));
        }

    }

    private void printCovariance() {
        System.out.println("print covariance:");
        for (int k = 0; k < this.clusterNumber; k++) {
            System.out.println("cluster k = " + k + " covariance matrix:");
            MatrixUtils.printMatrix(this.clusteredCovariance.get(k));
        }
    }

    private void generateCovarianceMatrix() {

        for (int k = 0; k < this.clusterNumber; k++) {

            Matrix clusterK = this.clusteredDataList.get(k);
            if (MatrixUtils.isEmptyOrNull(clusterK)) {

                clusteredCovariance
                        .add(new Matrix(this.dimesion, this.dimesion));
                continue;
            }

            Matrix covarianceMatrix = MatrixUtils.getCovarianceMatrix(clusterK);
            clusteredCovariance.add(covarianceMatrix);
        }
    }
}
