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

    private List<Matrix> mean;
    private List<Matrix> var;
    private Matrix component; // 1 x N
    private Matrix inputData; // D x N
    private Integer dimensionOfPoint;
    private Integer componentNumber;

    private Double epson = 1e-6;
    // temporary variance for calculate

    private Matrix posteriorLatent;

    MixGanssianEM() {

        // this.mean.add(new Matrix(2, 1));
        // this.mean.add(new Matrix(2, 1));
        // this.mean.add(new Matrix(2, 1));
        //
        // this.var.add(new Matrix(2, 2));
        // this.var.add(new Matrix(2, 2));
        // this.var.add(new Matrix(2, 2));

        this.componentNumber = 3;
        // this.component = new Matrix(1, 3);

    }

    MixGanssianEM(List<Matrix> mu, List<Matrix> var, int numberOfcomponent) {

        this.mean = mu;
        this.var = var;
        this.componentNumber = numberOfcomponent;
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

        // posteriorLatent = new Matrix(1, this.inputData.size());

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

    }

    private void updateVar() {

        // for each component
        for (int k = 0; k < this.component.getColumnDimension(); k++) {

            for (int i = 0; i < this.inputData.size(); i++) {
                Matrix point = MatrixUtils.getMatrix(this.inputData.get(i));
                // the mean of k component
                Matrix kmean = this.mean.get(k);

                // N X 1
                Matrix pointMinusMean = point.minus(kmean);

                // N x N
                Matrix times = pointMinusMean.times(pointMinusMean.transpose());

                times.times(posteriorLatent.get(1, i));

            }
        }

    }

    private void updateMean() {

        Double denominator = 0.;

        // for each component
        for (int k = 0; k < this.component.getColumnDimension(); k++) {

            Matrix kMean = this.mean.get(k);
            Matrix temp = new Matrix(kMean.getRowDimension(), 1);

            for (int i = 0; i < this.inputData.size(); i++) {

                // get numerator
                double posteriorPdf = posteriorLatent.get(1, i);
                Vector<Double> point = inputData.get(i);

                for (int j = 0; j < point.size(); j++) {
                    temp.set(j, 1, posteriorPdf * point.get(j));
                }

                // get denominator
                denominator += posteriorLatent.get(1, i);

            }

            // update the k-th mean
            this.mean.set(k, temp.times(1 / denominator));
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

        for (int k = 0; k < component.getColumnDimension(); k++) {

            component.getMatrix(0, 1, 0, 0);

            for (int i = 0; i < inputData.size(); i++) {

                Double posteriorPdfValue = getPosteriorPdfForLatentVar(
                        inputData.get(i), k);
                posteriorLatent.set(1, i, posteriorPdfValue);
            }
        }
    }

    private void InitialContext() {

        Random random = new Random();
        // get the dimension of point
        dimensionOfPoint = this.inputData.getRowDimension();

        /**
         * initial component
         */
        component = Matrix.random(1, componentNumber);
        Double totalNumberDouble = 0.;
        for (int i = 0; i < componentNumber; i++) {
            totalNumberDouble += component.get(0, i);
        }

        // regular to one
        component.times(1. / totalNumberDouble);

        /**
         * initial mean vector
         */

        // componentNumber
        Matrix.random(componentNumber, 1);

        for (int k = 0; k < componentNumber; k++) {

            Matrix matrix = mean.get(k);
            for (int i = 0; i < matrix.getRowDimension(); i++) {
                matrix.set(i, 0, 1);
            }
        }

        /**
         * initial variance matrix with random
         */

        Matrix random2 = Matrix.random(dimensionOfPoint, dimensionOfPoint);

        for (int k = 0; k < length; k++) {

            Matrix matrix = var.get(k);
            for (int i = 0; i < matrix.getRowDimension(); i++) {
                for (int j = 0; j < matrix.getColumnDimension(); j++) {
                    matrix.set(i, j, 1.);
                }
            }
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
    private Double getPosteriorPdfForLatentVar(Vector<Double> point,
                                               int kComponent) {

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
