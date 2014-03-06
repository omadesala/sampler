package com.sample.cluster;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import Jama.Matrix;

import com.google.common.base.Preconditions;
import com.probablity.utils.MatrixUtils;
import com.sample.distribution.Distribution;
import com.sample.distribution.implement.MVNDistribution;

/**
 * @ClassName: MixGanssianEM
 * @Description: this calss is use EM (expected maximise) to estimate the
 *               parameter of mixtured gaussian model
 * @author omadesala@msn.com
 * @date 2014-1-29 9:41:46
 */
public class MixGanssianEM {

    /**
     * component
     */
    private Integer componentNumber;// the count of component
    private Matrix component; // 1 x N

    /**
     * mean and varience
     */
    private List<Matrix> mean; // the size as same with componentNumber
    private List<Matrix> var; // the size as same with componentNumber, an it's
                              // a symmetric matrix

    /**
     * input data and dimension of point
     */
    private Matrix inputData; // D x N
    private Integer pointDimension;
    private Integer dataLength;

    // p(C_k|X_i)
    // component row and N column
    private Matrix iPointBelongKComponent;// component x N

    public MixGanssianEM() {
        this.componentNumber = 3;
        this.mean = new ArrayList<Matrix>();
        this.var = new ArrayList<Matrix>();
    }

    public MixGanssianEM(List<Matrix> mu, List<Matrix> var,
            int numberOfcomponent) {

        this.componentNumber = numberOfcomponent;
        this.mean = mu;
        this.var = var;
    }

    /**
     * @Title: train
     * @Description: train the data to get the parameter for maxmize the log
     *               likehood.
     * @param input data is D x N matrix ,each point is have D elements, and
     *            total N points
     * @return void 返回类型
     * @throws
     */
    public void train(Matrix data) {

        this.setInputData(data);

        System.out.println(" start to train ... ");
        InitialContext(this.getInputData().getRowDimension(), this
                .getInputData().getColumnDimension());

        printInfo();

        // the times is just for test
        // the convergence condition need optimal
        System.out.println("start train ...... ");
        for (int i = 0; i < 200; i++) {

            stepE();
            stepM();
        }
        printInfo();

    }

    private void printInfo() {
        printMean();
        printVar();
        printComponent();

    }

    private void printComponent() {
        System.out.println(" ************* print Component ************");
        MatrixUtils.printMatrix(this.component);
    }

    private void printVar() {

        System.out.println(" ************* print var ************");
        for (int i = 0; i < componentNumber; i++) {
            System.out.println(i + " component var:");
            MatrixUtils.printMatrix(this.var.get(i));
        }

    }

    private void printMean() {

        System.out.println(" ************* print mean ************");

        for (int i = 0; i < componentNumber; i++) {
            System.out.println(i + " component mean:");
            MatrixUtils.printMatrix(this.mean.get(i));
        }

    }

    public void stepM() {

        updateMean();
        updateVar();
        updateComponent();

    }

    private void updateComponent() {

        for (int k = 0; k < componentNumber; k++) {

            Matrix iPointIsComponentK = MatrixUtils.getMatrixRow(
                    getiPointBelongKComponent(), k);
            Double numerator = MatrixUtils
                    .getSumOfMatrixRow(iPointIsComponentK);

            this.component.set(0, k, numerator
                    / this.getInputData().getColumnDimension());
        }

    }

    public void updateVar() {

        Matrix numerator = null;
        for (int k = 0; k < componentNumber; k++) {

            numerator = new Matrix(pointDimension, pointDimension);
            Matrix iPointIsComponentK = MatrixUtils.getMatrixRow(
                    getiPointBelongKComponent(), k);
            Matrix meanOfComponentK = this.mean.get(k);

            for (int i = 0; i < getInputData().getColumnDimension(); i++) {

                Matrix iPoint = MatrixUtils.getMatrixColumn(getInputData(), i);

                Matrix minusMean = iPoint.minus(meanOfComponentK);

                Matrix var1 = minusMean.times(minusMean.transpose());

                Double probablity = MatrixUtils.getRowMatrixElementAt(
                        iPointIsComponentK, i);
                numerator = numerator.plus(var1.times(probablity));

            }

            Double denominator = MatrixUtils
                    .getSumOfMatrixRow(iPointIsComponentK);
            Matrix updatedVar = numerator.times(1. / denominator);
            this.var.set(k, updatedVar);

        }

    }

    public void updateMean() {

        Double denominator = 0.;

        for (int k = 0; k < componentNumber; k++) {

            Matrix numerator = new Matrix(pointDimension, 1);
            Matrix iPointIsComponentK = MatrixUtils.getMatrixRow(
                    getiPointBelongKComponent(), k);

            // System.out.println("mean of component:" + k);
            for (int i = 0; i < this.dataLength; i++) {

                Double probablity = MatrixUtils.getRowMatrixElementAt(
                        iPointIsComponentK, i);
                Matrix iPoint = MatrixUtils.getMatrixColumn(getInputData(), i);

                Matrix times = iPoint.times(probablity);
                numerator = numerator.plus(times);

            }

            denominator = MatrixUtils.getSumOfMatrixRow(iPointIsComponentK);

            Matrix updatedMean = numerator.times(1. / denominator);
            // MatrixUtils.printMatrix(updatedMean);

            this.mean.set(k, updatedMean);
        }
    }

    /**
     * @Title: stepE
     * @Description: calculate the posterior probability about latent var
     * @param
     * @return void 返回类型
     * @throws
     */
    public void stepE() {

        List<Vector<Double>> listDataVector = MatrixUtils
                .getListVector(getInputData());

        for (int k = 0; k < componentNumber; k++) {

            for (int i = 0; i < listDataVector.size(); i++) {

                Vector<Double> point = listDataVector.get(i);
                Double kComponentGeneratePoint = getLatentVarPosterior(point, k);

                getiPointBelongKComponent().set(k, i, kComponentGeneratePoint);
            }
        }
    }

    public void InitialContext(int pointDimension, int length) {

        this.pointDimension = pointDimension;
        this.dataLength = length;

        this.iPointBelongKComponent = new Matrix(this.componentNumber,
                this.dataLength);

        /**
         * initial component
         */
        this.component = Matrix.random(1, this.componentNumber);

        // regular to one
        Double totalNumberDouble = MatrixUtils.getSumOfMatrixRow(component);
        this.component = this.component.times(1. / totalNumberDouble);

        for (int k = 0; k < this.componentNumber; k++) {
            /**
             * initial mean vector
             */
            Matrix meanK = Matrix.random(pointDimension, 1);
            this.mean.add(meanK);

            /**
             * initial variance matrix with random
             */

            Matrix varK = getCorrectVar(pointDimension);

            this.var.add(varK);

        }

    }

    public Matrix getCorrectVar(int pointDimension) {

        Matrix varK = null;
        while (true) {
            varK = Matrix.random(pointDimension, pointDimension);

            for (int i = 0; i < pointDimension; i++) {
                for (int j = 0; j < pointDimension; j++) {

                    varK.set(j, i, varK.get(i, j));
                }
            }

            if (varK.det() > 0) {
                break;
            }

        }
        return varK;
    }

    /**
     * @Title: getPosteriorPdfForLatentVar
     * @Description: calculate the current point belongs to the k-th component
     *               probability
     * @param point current point x_i i = 1,2,3,...,n
     * @param kComponent it's the k-th component
     * @return Double the pdf value
     * @throws
     */
    public Double getLatentVarPosterior(Vector<Double> point,
                                        int componentIndexK) {

        Preconditions.checkNotNull(point);

        Preconditions.checkArgument(componentIndexK >= 0
                && componentIndexK < componentNumber);

        Double denominator = 0.;

        Matrix meanOfComponentK = this.mean.get(componentIndexK);
        Matrix varOfComponentK = this.var.get(componentIndexK);
        Double componentK = this.component.get(0, componentIndexK);

        Distribution distributionOfcomponentK = new MVNDistribution(
                meanOfComponentK, varOfComponentK);
        Double probablity = distributionOfcomponentK.pdf(point);

        Double numerator = probablity * componentK;

        for (int i = 0; i < this.componentNumber; i++) {

            // the i-th component value
            double componentI = this.component.get(0, i);
            // the i-th component distribution
            Distribution distributionOfcomponentI = new MVNDistribution(
                    this.mean.get(i), this.var.get(i));
            // calculate the posterior value
            double prob = distributionOfcomponentI.pdf(point);

            if (prob > 1E10) {
                System.out.println("prob:" + prob);
                MatrixUtils.printVectorPoint(point);
                System.out.println("mean:");
                MatrixUtils.printMatrix(this.mean.get(i));
                System.out.println("var:");
                MatrixUtils.printMatrix(this.var.get(i));
            }

            denominator += prob * componentI;

        }
        double ret = numerator / denominator;

        if (Double.isNaN(numerator) || Double.isNaN(denominator)
                || Double.isNaN(ret)) {
            System.out.println("numerator=" + numerator + " denominator="
                    + denominator);

        }
        return ret;

    }

    public Matrix getiPointBelongKComponent() {

        return iPointBelongKComponent;

    }

    public Matrix getInputData() {

        return inputData;

    }

    public void setInputData(Matrix inputData) {

        this.inputData = inputData;

    }

}
