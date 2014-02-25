package com.sample.classify;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import Jama.Matrix;

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
    private Integer dimensionOfPoint;

    private Double epson = 1e-6;

    // temporary variance for calculate

    private Matrix pointBelongKComponent;
    private Matrix kComponentEachPointEstiamation;

    public MixGanssianEM() {

        this.componentNumber = 3;
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
     * @param input
     *            data is D x N matrix ,each point is have D elements, and total
     *            N points
     * @return void 返回类型
     * @throws
     */
    public void train(Matrix data) {

        this.inputData = data;

        InitialContext();

        // the times is just for test
        // the convergence condition need optimal
        for (int i = 0; i < 1000; i++) {
            stepE();
            stepM();
        }

    }

    private void stepM() {

        updateMean();
        updateVar();
        updateComponent();

    }

    private void updateComponent() {

        for (int k = 0; k < componentNumber; k++) {

            Double numerator = MatrixUtils.getSumOfMatrixRow(MatrixUtils
                    .getMatrixRow(kComponentEachPointEstiamation, k));

            this.component.set(0, k,
                    numerator / this.inputData.getColumnDimension());
        }

    }

    private void updateVar() {

        Matrix numerator = new Matrix(dimensionOfPoint, dimensionOfPoint);

        for (int k = 0; k < componentNumber; k++) {
            for (int i = 0; i < inputData.getColumnDimension(); i++) {

                Matrix dataPoint = MatrixUtils.getMatrixColumn(inputData, i);

                Matrix minusMean = dataPoint.minus(this.mean.get(k));
                Matrix var1 = minusMean.times(minusMean.transpose());

                double d = MatrixUtils.getMatrixRow(
                        kComponentEachPointEstiamation, k).get(0, i);

                numerator.plus(var1.times(d));
            }

            Double denominator = MatrixUtils.getSumOfMatrixRow(MatrixUtils
                    .getMatrixRow(kComponentEachPointEstiamation, k));

            this.var.set(k, numerator.times(1. / denominator));

        }

    }

    private void updateMean() {

        Double denominator = 0.;
        Matrix numerator = new Matrix(dimensionOfPoint, 1);

        for (int k = 0; k < componentNumber; k++) {
            for (int i = 0; i < inputData.getColumnDimension(); i++) {

                Matrix row = MatrixUtils.getMatrixRow(
                        kComponentEachPointEstiamation, k);
                Matrix dataPoint = MatrixUtils.getMatrixColumn(inputData, i);

                numerator.plus(row.times(dataPoint));
            }

            denominator = MatrixUtils.getSumOfMatrixRow(MatrixUtils
                    .getMatrixRow(kComponentEachPointEstiamation, k));

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

                kComponentEachPointEstiamation.set(k, i,
                        kComponentGeneratePoint);
            }
        }

    }

    private void InitialContext() {

        dimensionOfPoint = this.inputData.getRowDimension();

        pointBelongKComponent = new Matrix(1,
                this.inputData.getColumnDimension());

        kComponentEachPointEstiamation = new Matrix(componentNumber,
                inputData.getColumnDimension());

        /**
         * initial component
         */
        component = Matrix.random(1, componentNumber);

        // regular to one
        Double totalNumberDouble = MatrixUtils.getSumOfMatrixColumn(component);
        component.times(1. / totalNumberDouble);

        for (int k = 0; k < componentNumber; k++) {
            /**
             * initial mean vector
             */
            Matrix meanK = Matrix.random(dimensionOfPoint, 1);
            this.mean.add(meanK);

            /**
             * initial variance matrix with random
             */

            Matrix varK = Matrix.random(dimensionOfPoint, dimensionOfPoint);
            this.var.add(varK);

        }

    }

    /**
     * @Title: getPosteriorPdfForLatentVar
     * @Description: calculate the current point belongs to the k-th component
     *               probability
     * @param point
     *            current point x_i i = 1,2,3,...,n
     * @param kComponent
     *            it's the k-th component
     * @return Double the pdf value
     * @throws
     */
    private Double getLatentVarPosterior(Vector<Double> point, int kComponent) {

        Double denominator = 0.;

        Distribution kComponentPDF = new MVNDistribution(
                this.mean.get(kComponent), this.var.get(kComponent));

        double pdfValue = kComponentPDF.densityFunction(point);
        double kcomponent = this.component.get(1, kComponent);

        Double numerator = pdfValue * kcomponent;

        for (int i = 0; i < this.component.getColumnDimension(); i++) {

            // the i-th component value
            double iComponent = this.component.get(1, i);
            // the i-th component distribution
            Distribution iComponentPDF = new MVNDistribution(this.mean.get(i),
                    this.var.get(i));
            // calculate the posterior value
            double ipdfValue = iComponentPDF.densityFunction(point);

            denominator += ipdfValue * iComponent;

        }

        return numerator / denominator;

    }
}
