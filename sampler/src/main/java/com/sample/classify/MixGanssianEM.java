package com.sample.classify;

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
    private List<Matrix> var; // the size as same with componentNumber

    /**
     * input data and dimension of point
     */
    private Matrix inputData; // D x N
    private Integer pointDimension;

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

        this.inputData = data;

        System.out.println(" start to train ... ");
        InitialContext();

        // the times is just for test
        // the convergence condition need optimal
        for (int i = 0; i < 1000; i++) {
            System.out.println("step E");
            stepE();
            System.out.println("step M");
            stepM();
            printInfo();
        }

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

    private void stepM() {

        updateMean();
        updateVar();
        updateComponent();

    }

    private void updateComponent() {

        System.out.println("updateComponent");
        for (int k = 0; k < componentNumber; k++) {

            Double numerator = MatrixUtils.getSumOfMatrixRow(MatrixUtils
                    .getMatrixRow(iPointBelongKComponent, k));

            this.component.set(0, k,
                    numerator / this.inputData.getColumnDimension());
        }

    }

    private void updateVar() {

        System.out.println("updateVar");
        Matrix numerator = new Matrix(pointDimension, pointDimension);

        for (int k = 0; k < componentNumber; k++) {
            for (int i = 0; i < inputData.getColumnDimension(); i++) {

                Matrix dataPoint = MatrixUtils.getMatrixColumn(inputData, i);

                Matrix minusMean = dataPoint.minus(this.mean.get(k));
                Matrix var1 = minusMean.times(minusMean.transpose());

                double d = MatrixUtils.getMatrixRow(iPointBelongKComponent, k)
                        .get(0, i);

                numerator.plus(var1.times(d));
            }

            Double denominator = MatrixUtils.getSumOfMatrixRow(MatrixUtils
                    .getMatrixRow(iPointBelongKComponent, k));

            this.var.set(k, numerator.times(1. / denominator));

        }

    }

    private void updateMean() {

        System.out.println("updateMean");
        Double denominator = 0.;
        Matrix numerator = new Matrix(pointDimension, 1);

        for (int k = 0; k < componentNumber; k++) {
            for (int i = 0; i < inputData.getColumnDimension(); i++) {

                Matrix row = MatrixUtils
                        .getMatrixRow(iPointBelongKComponent, k);

                MatrixUtils.printMatrix(row);

                Matrix dataPoint = MatrixUtils.getMatrixColumn(inputData, i);
                MatrixUtils.printMatrix(dataPoint);

                numerator.plus(row.times(dataPoint));
            }

            denominator = MatrixUtils.getSumOfMatrixRow(MatrixUtils
                    .getMatrixRow(iPointBelongKComponent, k));

            this.mean.set(k, numerator.times(1. / denominator));
        }
    }

    /**
     * @Title: stepE
     * @Description: calculate the posterior probability about latent var
     * @param
     * @return void 返回类型
     * @throws
     */
    private void stepE() {

        List<Vector<Double>> listDataVector = MatrixUtils
                .getListVector(inputData);

        for (int k = 0; k < componentNumber; k++) {

            for (int i = 0; i < listDataVector.size(); i++) {

                Vector<Double> point = listDataVector.get(i);
                Double kComponentGeneratePoint = getLatentVarPosterior(point, k);

                iPointBelongKComponent.set(k, i, kComponentGeneratePoint);
            }
        }
    }

    private void InitialContext() {

        pointDimension = this.inputData.getRowDimension();

        iPointBelongKComponent = new Matrix(componentNumber,
                inputData.getColumnDimension());

        /**
         * initial component
         */
        component = Matrix.random(1, componentNumber);

        // regular to one
        Double totalNumberDouble = MatrixUtils.getSumOfMatrixRow(component);
        component = component.times(1. / totalNumberDouble);

        for (int k = 0; k < componentNumber; k++) {
            /**
             * initial mean vector
             */
            Matrix meanK = Matrix.random(pointDimension, 1);
            this.mean.add(meanK);

            /**
             * initial variance matrix with random
             */

            Matrix varK = Matrix.random(pointDimension, pointDimension);
            this.var.add(varK);

        }

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
    private Double getLatentVarPosterior(Vector<Double> point,
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
        double probablity = distributionOfcomponentK.densityFunction(point);

        Double numerator = probablity * componentK;

        for (int i = 0; i < this.component.getColumnDimension(); i++) {

            // the i-th component value
            double componentI = this.component.get(0, i);
            // the i-th component distribution
            Distribution distributionOfcomponentI = new MVNDistribution(
                    this.mean.get(i), this.var.get(i));
            // calculate the posterior value
            double prob = distributionOfcomponentI.densityFunction(point);

            denominator += prob * componentI;

        }

        return numerator / denominator;

    }
}
