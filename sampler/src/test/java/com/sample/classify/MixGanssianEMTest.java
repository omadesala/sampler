package com.sample.classify;

import java.util.List;
import java.util.Vector;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import Jama.Matrix;

import com.probablity.utils.CollectionUtils;
import com.probablity.utils.MatrixUtils;
import com.sample.ui.MixGaussianSample;

public class MixGanssianEMTest {

    MixGanssianEM mixGanssianEM = null;

    @Before
    public void setUp() {
        mixGanssianEM = new MixGanssianEM();
    }

    @Test
    @Ignore
    public void testTrain() {

        MixGaussianSample mix = new MixGaussianSample();

        mix.start();

        System.out.println("print samples mix: ");
        List<Vector<Double>> samples = CollectionUtils.getList(mix.getSamples());

        Matrix matrix = MatrixUtils.getMatrix(samples);
        System.out.println("print  mix row: " + matrix.getRowDimension());
        System.out.println("print  mix col: " + matrix.getColumnDimension());

        mixGanssianEM.train(matrix);
    }

    @Test
    @Ignore
    public void testGetPosterior() {

        MixGaussianSample mix = new MixGaussianSample();

        mix.start();

        System.out.println("print samples mix: ");
        List<Vector<Double>> samples = CollectionUtils.getList(mix.getSamples());

        Matrix matrix = MatrixUtils.getMatrix(samples);
        int length = samples.size();

        System.out.println("the data length is " + length);

        mixGanssianEM.InitialContext(matrix.getRowDimension(), length);

        Matrix componentret = new Matrix(3, length);

        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < length; i++) {
                Double prob = mixGanssianEM.getLatentVarPosterior(samples.get(i), k);
                componentret.set(k, i, prob);
            }
        }

        for (int m = 0; m < length; m++) {

            Matrix matrixColumn = MatrixUtils.getMatrixColumn(componentret, m);
            Assert.assertEquals(1., MatrixUtils.getSumOfMatrixColumn(matrixColumn), 1E-5);
        }

    }

    @Test
    @Ignore
    public void testGetStepE() {

        MixGaussianSample mix = new MixGaussianSample();

        mix.start();

        List<Vector<Double>> samples = CollectionUtils.getList(mix.getSamples());

        Matrix matrix = MatrixUtils.getMatrix(samples);
        int length = samples.size();

        mixGanssianEM.setInputData(matrix);
        mixGanssianEM.InitialContext(matrix.getRowDimension(), length);

        mixGanssianEM.stepE();

        Matrix getiPointBelongKComponent = mixGanssianEM.getiPointBelongKComponent();

        for (int m = 0; m < length; m++) {
            Matrix matrixColumn = MatrixUtils.getMatrixColumn(getiPointBelongKComponent, m);
            Assert.assertEquals(1., MatrixUtils.getSumOfMatrixColumn(matrixColumn), 1E-5);
        }

    }

    @Test
    @Ignore
    public void testupdateMean() {

        MixGaussianSample mix = new MixGaussianSample();

        mix.start();

        System.out.println("print samples mix: ");
        List<Vector<Double>> samples = CollectionUtils.getList(mix.getSamples());

        Matrix matrix = MatrixUtils.getMatrix(samples);
        int length = samples.size();

        System.out.println("the data length is " + length);

        mixGanssianEM.setInputData(matrix);
        mixGanssianEM.InitialContext(matrix.getRowDimension(), length);

        mixGanssianEM.stepE();

        for (int i = 0; i < 10; i++) {
            System.out.println("column :" + i);
            MatrixUtils.printMatrix(MatrixUtils.getMatrixColumn(mixGanssianEM.getiPointBelongKComponent(), i));
        }

        mixGanssianEM.updateMean();
    }

    @Test
    public void testUpdateVar() {

        MixGaussianSample mix = new MixGaussianSample();

        mix.start();

        System.out.println("print samples mix: ");
        List<Vector<Double>> samples = CollectionUtils.getList(mix.getSamples());

        Matrix matrix = MatrixUtils.getMatrix(samples);
        int length = samples.size();

        System.out.println("the data length is " + length);

        mixGanssianEM.setInputData(matrix);
        mixGanssianEM.InitialContext(matrix.getRowDimension(), length);

        mixGanssianEM.stepE();

        for (int i = 0; i < 10; i++) {
            System.out.println("column :" + i);
            MatrixUtils.printMatrix(MatrixUtils.getMatrixColumn(mixGanssianEM.getiPointBelongKComponent(), i));
        }

        mixGanssianEM.updateMean();
        mixGanssianEM.updateVar();
    }

}
